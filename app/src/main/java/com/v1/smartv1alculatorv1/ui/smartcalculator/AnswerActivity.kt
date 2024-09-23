package com.v1.smartv1alculatorv1.ui.smartcalculator

import android.content.Context
import android.content.Intent
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
import com.v1.smartv1alculatorv1.databinding.ActivityAnswerBinding
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException


class AnswerActivity : BaseActivity<ActivityAnswerBinding, SmartCalculatorViewModel>() {

    private lateinit var chatViewModel: SmartCalculatorViewModel
    private lateinit var chatRepository: ChatRepository
    private val conversationResetDelay: Long = 30 * 60 * 1000L
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
        binding.imgThongTin.setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }
        binding.imgEdit.setOnClickListener {
            finish()
        }

        // Ẩn LinearLayout chứa rq_answer ban đầu
//        binding.linearLayout.visibility = View.GONE

        chatViewModel = SmartCalculatorViewModel()
        chatRepository = ChatRepository(this)
        chatViewModel.currentConversationId = getStoredConversationId()

        val text = intent.getStringExtra("answer_rq")
        Log.d("TAG123", "initView: $text")
        binding.answerTxt.text = text
        sendMessage(text)


        // Xử lý nút để gửi lại dữ liệu về SmartCalculatorActivity
        binding.btnSolveAnother.setOnClickListener {
            /*if (text != null){
                val chatRepository = ChatRepository(this)
                chatRepository.saveChatData(text)
            }*/

            val intent = Intent()
            intent.putExtra("RETURN_TEXT", text) // Chuyển lại dữ liệu text
            setResult(RESULT_OK, intent)
            Log.d("choi","$text")
            finish() // Quay lại SmartCalculatorActivity

        }

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
                conversationId = chatViewModel.currentConversationId.orEmpty(),
                isBot = false
            )

            chatViewModel.chatList.value!!.add(userMessage)
            //chatRepository.insertChatHisSamrt(userMessage)


            val userMessageToSend = UserMessage(
                inputs = HashMap(),
                query = userMessageText,
                responseMode = "streaming",
                conversationId = chatViewModel.currentConversationId.orEmpty(),
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
                            //chatRepository.insertChatHisSamrt(botMessage)


                            runOnUiThread {
                                val solutionSteps = botMessage.answer
                                Log.d("TAG123Answer", "loi giai Answer: $solutionSteps")


                                if (!solutionSteps.isNullOrEmpty()) {
                                    // Tìm chỉ số của cả ba chuỗi
                                    val stepByStepIndex =
                                        solutionSteps.indexOf("Step-by-Step Solution:")
                                    val solutionStepsIndex =
                                        solutionSteps.indexOf("Solution Steps:")
                                    val solutionProcessIndex =
                                        solutionSteps.indexOf("Solution Process:")
                                    val recapIndex =
                                        solutionSteps.indexOf("**Recap:**") // Tìm chỉ số của "**Recap:**"

                                    // Lấy chỉ số bắt đầu của phần cần lấy
                                    val startIndex = when {
                                        stepByStepIndex != -1 && solutionStepsIndex != -1 && solutionProcessIndex != -1 ->
                                            minOf(
                                                stepByStepIndex,
                                                solutionStepsIndex,
                                                solutionProcessIndex
                                            )

                                        stepByStepIndex != -1 && solutionProcessIndex != -1 ->
                                            minOf(stepByStepIndex, solutionProcessIndex)

                                        solutionStepsIndex != -1 && solutionProcessIndex != -1 ->
                                            minOf(solutionStepsIndex, solutionProcessIndex)

                                        stepByStepIndex != -1 ->
                                            stepByStepIndex

                                        solutionStepsIndex != -1 ->
                                            solutionStepsIndex

                                        solutionProcessIndex != -1 ->
                                            solutionProcessIndex

                                        else ->
                                            -1
                                    }

                                    // Lấy phần nội dung từ startIndex đến recapIndex
                                    if (startIndex != -1 && recapIndex != -1) {
                                        val stepsSection =
                                            solutionSteps.substring(startIndex, recapIndex)
                                                .trim() // Lấy nội dung từ startIndex đến recapIndex
                                        Log.d("TAG123", "stepsSection: $stepsSection")

                                        // Sử dụng regex để lấy từng bước (bắt đầu bằng số và dấu '.')
                                        val regex = Regex(
                                            """\d+\.\s(.+?)(?=\d+\.\s|$)""",
                                            RegexOption.DOT_MATCHES_ALL
                                        )
                                        val stepsListAnswser = regex.findAll(stepsSection)
                                            .map { it.groupValues[1].trim() }.toMutableList()

                                        if (stepsListAnswser.isNotEmpty()) {
                                            // Hiển thị BottomSheet với danh sách bước
                                            val bottomSheet = StepsAnswerBottomSheet(stepsListAnswser)
                                            bottomSheet.show(
                                                supportFragmentManager,
                                                bottomSheet.tag
                                            )
                                        } else {
                                            Log.d("TAG123", "Không tìm thấy các bước giải")
                                        }
                                    } else {
                                        Log.d(
                                            "TAG123",
                                            "Không tìm thấy phần Step-by-Step Solution, Solution Steps hoặc Solution Process"
                                        )
                                        //binding.rqAnswer.text = "Không tìm thấy phần giải"
                                    }
                                } else {
                                    Log.d("TAG123", "Dữ liệu rỗng hoặc null từ server")
                                    //binding.rqAnswer.text = "Không có dữ liệu trả về"
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

            updateLastMessageTime()
            startConversationTimeoutHandler()
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
                        Log.d("answer", answer)
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

    private fun startConversationTimeoutHandler() {
        handler.removeCallbacks(resetConversationIdRunnable)
        handler.postDelayed(resetConversationIdRunnable, conversationResetDelay)
    }

    fun parseSolutionSteps(solution: String): List<String> {
        val stepsList = mutableListOf<String>()
        val regex = Regex("""\d+\.\s.*?(?=\d+\.\s|$)""") // Regex để tách từng bước
        regex.findAll(solution).forEach {
            stepsList.add(it.value.trim())
        }
        return stepsList
    }


}