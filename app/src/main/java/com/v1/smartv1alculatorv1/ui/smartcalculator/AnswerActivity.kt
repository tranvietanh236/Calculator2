package com.v1.smartv1alculatorv1.ui.smartcalculator

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.Model.UserMessage
import com.v1.smartv1alculatorv1.api.ModuleChat
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityAnswerBinding
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException

class AnswerActivity : BaseActivity<ActivityAnswerBinding, SmartCalculatorViewModel>() {

    private lateinit var chatViewModel: SmartCalculatorViewModel
    private lateinit var chatRepository: ChatRepository
    private val handler = Handler(Looper.getMainLooper())
    private val resetConversationIdRunnable = Runnable {
        chatViewModel.currentConversationId = null
    }
    private var isWaitingForResponse: Boolean = false
    override fun createBinding(): ActivityAnswerBinding {
        return ActivityAnswerBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): SmartCalculatorViewModel {
        return SmartCalculatorViewModel()
    }

    override fun initView() {
        super.initView()
        binding.icBackAnswers.setOnClickListener {
            finish()
        }
        chatViewModel = SmartCalculatorViewModel()
        chatRepository = ChatRepository(this)
        chatViewModel.currentConversationId = getStoredConversationId()

        val text = intent.getStringExtra("answer_rq")
        Log.d("TAG123", "initView: $text")
        binding.answerTxt.text = text
        sendMessage(text)
    }

    private fun sendMessage(message: String? = null) {
        if (isWaitingForResponse) return

        val userMessageText = message.toString().trim()
        val deviceId = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

        if (userMessageText.isNotEmpty()) {
            isWaitingForResponse = true

            val userMessage = ChatAnswer(
                createdAt = (System.currentTimeMillis() / 1000L).toString(),
                answer = userMessageText,
                conversationId = "",
                isBot = false
            )

            chatViewModel.chatList.value!!.add(userMessage)
            chatRepository.insertChatHis(userMessage)


//            requireActivity().runOnUiThread {
//                chatAdapter.notifyItemInserted(chatViewModel.chatList.value!!.size - 1)
//                viewBinding.recyclerViewChat.scrollToPosition(chatViewModel.chatList.value!!.size - 1)
//            }

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
                            Log.d("TAG123", "onNext: ${botMessage.answer}")
                            chatViewModel.chatList.value!!.add(botMessage)
                            chatRepository.insertChatHis(botMessage)


                            runOnUiThread {
                                binding.rqAnswer.text = botMessage.answer
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

            updateLastMessageTime()

        }
    }

    private fun parseResponse(responseString: String): Pair<String, String> {
        var answer = ""
        var conversationId = ""

        try {
            val dataLines = responseString.split("data: ")
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

    private fun updateLastMessageTime() {
        val sharedPref = this.getSharedPreferences("ChatPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putLong("last_message_time", System.currentTimeMillis())
            apply()
        }
    }

}