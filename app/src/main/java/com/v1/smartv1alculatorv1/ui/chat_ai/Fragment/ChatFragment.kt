package com.v1.smartv1alculatorv1.ui.chat_ai.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.calculator.customformula.base.BaseFragment
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.Model.UserMessage
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.api.ModuleChat
import com.v1.smartv1alculatorv1.databinding.FragmentChatBinding
import com.v1.smartv1alculatorv1.ui.chat_ai.Activity.ChatActivity
import com.v1.smartv1alculatorv1.ui.chat_ai.ViewModel.ChatViewModel
import com.v1.smartv1alculatorv1.ui.chat_ai.Adapter.ChatAdapter
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import java.util.*

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatRepository: ChatRepository


    private val handler = Handler(Looper.getMainLooper())
    private val resetConversationIdRunnable = Runnable {
        chatViewModel.currentConversationId = null
        clearChatList()
    }
    private var isWaitingForResponse: Boolean = false

    override fun inflateViewBinding() = FragmentChatBinding.inflate(layoutInflater)




    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRepository = ChatRepository(requireContext())
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        // Khởi tạo chatAdapter sau khi gán giá trị cho chatViewModel.chatList
        chatAdapter = ChatAdapter(chatViewModel.chatList.value!!, viewBinding.textViewEmpty)
        viewBinding.recyclerViewChat.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.recyclerViewChat.adapter = chatAdapter

//        if (SplashActivity.isClearn) {
//            clearChatList1()
//            SplashActivity.isClearn = false
//        }

        clearMessagesIfTimeout()
        if (chatViewModel.chatList.value!!.isEmpty()) {
            viewBinding.textViewEmpty.visibility = View.VISIBLE
        } else {
            viewBinding.textViewEmpty.visibility = View.GONE
        }
        viewBinding.actionButton.setOnClickListener {
            sendMessage()
        }
//        cardViewM2

        viewBinding.editTextUserInput.addTextChangedListener { s ->
            val inputText = s.toString().trim()
            if (inputText.isNotEmpty()) {
                viewBinding.actionButton.setImageResource(R.drawable.ic_send_ai_on)
                viewBinding.editTextUserInput.setBackgroundResource(R.drawable.bg_edit_text_on)
            } else {
                viewBinding.actionButton.setImageResource(R.drawable.ic_send_ai_off)
                viewBinding.editTextUserInput.setBackgroundResource(R.drawable.bg_edit_text_off)
            }
        }
        val userBmi = ChatActivity.bmiValue
        if (userBmi.toString().isNotEmpty() && userBmi > 0) {
            sendMessage("My BMI is $userBmi, what does that mean?")

        }
        viewBinding.recyclerViewChat.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            true
        }

    }


    private fun clearMessagesIfTimeout() {
        val sharedPref = requireContext().getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        val lastMessageTime = sharedPref.getLong("last_message_time", 0L)
        val currentTime = System.currentTimeMillis()

    }

    private fun updateLastMessageTime() {
        val sharedPref = requireContext().getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("last_message_time", System.currentTimeMillis())
            apply()
        }
    }

    private fun sendMessage(message: String? = null) {
        if (isWaitingForResponse) return

        val userMessageText = message ?: viewBinding.editTextUserInput.text.toString().trim()
        val deviceId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        if (userMessageText.isNotEmpty()) {
            isWaitingForResponse = true
            viewBinding.llPleaseWait.visibility = View.VISIBLE

            val userMessage = ChatAnswer(
                createdAt = (System.currentTimeMillis() / 1000L).toString(),
                answer = userMessageText,
                conversationId = chatViewModel.currentConversationId.orEmpty(),
                isBot = false
            )

            chatViewModel.chatList.value!!.add(userMessage)
            chatRepository.insertChat(userMessage)

            if (chatViewModel.chatList.value!!.isEmpty()) {
                viewBinding.textViewEmpty.visibility = View.VISIBLE
            } else {
                viewBinding.textViewEmpty.visibility = View.GONE
            }

            requireActivity().runOnUiThread {
                chatAdapter.notifyItemInserted(chatViewModel.chatList.value!!.size - 1)
                viewBinding.recyclerViewChat.scrollToPosition(chatViewModel.chatList.value!!.size - 1)
            }

            val userMessageToSend = UserMessage(
                inputs = HashMap(),
                query = userMessageText,
                responseMode = "streaming",
                conversationId = "",
                user = deviceId
            )

            Log.d("conversationId1", userMessage.conversationId)
            ModuleChat.apiService.sendMessage(userMessageToSend)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(Schedulers.io())
                ?.subscribe(object : DisposableObserver<ResponseBody>() {
                    override fun onNext(responseBody: ResponseBody) {
                        try {
                            val responseString = responseBody.string()
                            val (answer, conversationId) = parseResponse(responseString)
                            if (userMessage.conversationId == "") {
                                chatRepository.updateConversationId("", conversationId)
                            }

                            Log.d("conversationId2", conversationId)
                            if (conversationId.isNotEmpty() && conversationId != chatViewModel.currentConversationId) {
                                chatViewModel.currentConversationId = conversationId
                                storeConversationId(conversationId)
                            }

                            val botMessage = ChatAnswer(
                                createdAt = (System.currentTimeMillis() / 1000L).toString(),
                                answer = answer,
                                conversationId = chatViewModel.currentConversationId.orEmpty(),
                                isBot = true
                            )

                            chatViewModel.chatList.value!!.add(botMessage)
                            chatRepository.insertChat(botMessage)

                            if (isAdded) {
                                requireActivity().runOnUiThread {
                                    chatAdapter.notifyItemInserted(chatViewModel.chatList.value!!.size - 1)
                                    viewBinding.recyclerViewChat.scrollToPosition(chatViewModel.chatList.value!!.size - 1)
                                    viewBinding.llPleaseWait.visibility = View.GONE
                                }
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } finally {
                            isWaitingForResponse = false
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {}
                })

            viewBinding.editTextUserInput.setText("")
            updateLastMessageTime()

        }
    }


    private fun parseResponse(responseString: String): Pair<String, String> {
        var answer = ""
        var conversationId = ""

        try {
            val dataLines = responseString.split("data: ")
            Log.d("dataLines", "$dataLines")
            for (line in dataLines) {
                if (line.trim().startsWith("{")) {
                    val jsonObject = JSONObject(line.trim())
                    if (jsonObject.getString("event") == "workflow_finished") {
                        val dataObject = jsonObject.getJSONObject("data")
                        answer = dataObject.getJSONObject("outputs").getString("answer")
                        conversationId = jsonObject.getString("conversation_id")
                        break
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Pair(answer, conversationId)
    }

    fun clearChatList() {
        chatViewModel.chatList.value!!.clear()
        chatAdapter.notifyDataSetChanged()
    }


    private fun getStoredConversationId(): String? {
        val sharedPref = requireContext().getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("conversation_id", null)
    }

    private fun storeConversationId(conversationId: String?) {
        val sharedPref = requireContext().getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("conversation_id", conversationId)
            apply()
        }
    }
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity?.currentFocus
        view?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        viewBinding.root.clearFocus()
        imm.hideSoftInputFromWindow(viewBinding.root.windowToken, 0)
    }


}
