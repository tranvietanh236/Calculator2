package com.v1.smartv1alculatorv1.ui.chat_ai.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.calculator.customformula.utils.GlobalFunction
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.Model.UserMessage
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.api.ModuleChat
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityChatNewBinding
import com.v1.smartv1alculatorv1.ui.chat_ai.Adapter.ChatAdapter
import com.v1.smartv1alculatorv1.ui.chat_ai.ViewModel.ChatViewModel
import com.v1.smartv1alculatorv1.ui.history.HistoryActivity
import com.v1.smartv1alculatorv1.ui.home.HomeActivity
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import java.util.HashMap

class ChatActivityNew : BaseActivity<ActivityChatNewBinding, BaseViewModel>() {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatRepository: ChatRepository

    private val conversationResetDelay: Long = 30 * 60 * 1000L
    private val handler = Handler(Looper.getMainLooper())
    private val resetConversationIdRunnable = Runnable {
        chatViewModel.currentConversationId = null
        clearChatList()
    }
    private var isWaitingForResponse: Boolean = false
    override fun createBinding(): ActivityChatNewBinding {
        return ActivityChatNewBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        super.initView()
        binding.ivHistory.setOnClickListener {
            GlobalFunction.startActivity(this, HistoryActivity::class.java)
        }
        // Đổi màu StatusBar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.ic_converter_color)
        }
        binding.txtQs1.setOnClickListener {
            sendMessage("What is the least common multiple of 6 and 11?")
        }

        binding.txtQs2.setOnClickListener {
            sendMessage("Make a sentence using the given words:is/you/ she / waiting for.")
        }
        binding.txtQs3.setOnClickListener {
            sendMessage("Why is the Earth called a watery planet?")
        }


        chatRepository = ChatRepository(this)
        chatViewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        // Khởi tạo chatAdapter sau khi gán giá trị cho chatViewModel.chatList
        chatAdapter = ChatAdapter(chatViewModel.chatList.value!!, binding.clQs)
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChat.adapter = chatAdapter

        if (HomeActivity.isClearn) {
            clearChatList1()
            HomeActivity.isClearn = false
        }

        clearMessagesIfTimeout()
        if (chatViewModel.chatList.value!!.isEmpty()) {
            binding.textViewEmpty.visibility = View.VISIBLE
            binding.clQs.visibility = View.VISIBLE

        } else {
            binding.textViewEmpty.visibility = View.GONE
            binding.clQs.visibility = View.GONE
        }
        binding.actionButton.setOnClickListener {
            sendMessage()
        }
//        cardViewM2

        binding.editTextUserInput.addTextChangedListener { s ->
            val inputText = s.toString().trim()
            if (inputText.isNotEmpty()) {
                binding.actionButton.setImageResource(R.drawable.ic_send_ai_on)
                binding.editTextUserInput.setBackgroundResource(R.drawable.bg_edit_text_on)
            } else {
                binding.actionButton.setImageResource(R.drawable.ic_send_ai_off)
                binding.editTextUserInput.setBackgroundResource(R.drawable.bg_edit_text_off)
            }
        }
        val userBmi = ChatActivity.bmiValue
        if (userBmi.toString().isNotEmpty() && userBmi > 0) {
            sendMessage("My BMI is $userBmi, what does that mean?")

        }
        binding.recyclerViewChat.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            true
        }
    }
    private fun clearMessagesIfTimeout() {
        val sharedPref = this.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        val lastMessageTime = sharedPref.getLong("last_message_time", 0L)
        val currentTime = System.currentTimeMillis()

    }

    private fun updateLastMessageTime() {
        val sharedPref = this.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("last_message_time", System.currentTimeMillis())
            apply()
        }
    }

    private fun sendMessage(message: String? = null) {
        if (isWaitingForResponse) return

        val userMessageText = message ?: binding.editTextUserInput.text.toString().trim()
        val deviceId = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

        if (userMessageText.isNotEmpty()) {
            isWaitingForResponse = true
            binding.llPleaseWait.visibility = View.VISIBLE

            val userMessage = ChatAnswer(
                createdAt = (System.currentTimeMillis() / 1000L).toString(),
                answer = userMessageText,
                conversationId = chatViewModel.currentConversationId.orEmpty(),
                isBot = false
            )

            chatViewModel.chatList.value!!.add(userMessage)
            chatRepository.insertChat(userMessage)

            if (chatViewModel.chatList.value!!.isEmpty()) {
                binding.textViewEmpty.visibility = View.VISIBLE
                binding.clQs.visibility = View.VISIBLE
            } else {
                binding.textViewEmpty.visibility = View.GONE
                binding.clQs.visibility = View.GONE
            }

          runOnUiThread {
                chatAdapter.notifyItemInserted(chatViewModel.chatList.value!!.size - 1)
                binding.recyclerViewChat.scrollToPosition(chatViewModel.chatList.value!!.size - 1)
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

                                runOnUiThread {
                                    chatAdapter.notifyItemInserted(chatViewModel.chatList.value!!.size - 1)
                                    binding.recyclerViewChat.scrollToPosition(chatViewModel.chatList.value!!.size - 1)
                                    binding.llPleaseWait.visibility = View.GONE

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

            binding.editTextUserInput.setText("")
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


    private fun startConversationTimeoutHandler() {
        handler.removeCallbacks(resetConversationIdRunnable)
        handler.postDelayed(resetConversationIdRunnable, conversationResetDelay)
    }

    fun clearChatList() {
        chatViewModel.chatList.value!!.clear()
        chatAdapter.notifyDataSetChanged()
    }

    fun clearChatList1() {
        chatViewModel.chatList.value!!.clear()
        chatViewModel.currentConversationId = null
    }

    private fun getStoredConversationId(): String? {
        val sharedPref = this.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("conversation_id", null)
    }

    private fun storeConversationId(conversationId: String?) {
        val sharedPref = this.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("conversation_id", conversationId)
            apply()
        }
    }
    private fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus
        view?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        binding.root.clearFocus()
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }


}