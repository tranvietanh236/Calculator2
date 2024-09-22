package com.v1.smartv1alculatorv1.ui.scan_to_slove

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.calculator.customformula.utils.GlobalFunction
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.Model.ScanModel
import com.v1.smartv1alculatorv1.Model.UserMessage
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.api.ModuleChat
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityAnswer2Binding
import com.v1.smartv1alculatorv1.ui.smartcalculator.SmartCalculatorViewModel
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException

class Answer2Activity : BaseActivity<ActivityAnswer2Binding, BaseViewModel>() {
    private lateinit var chatViewModel: SmartCalculatorViewModel
    private lateinit var chatRepository: ChatRepository
    private val conversationResetDelay: Long = 30 * 60 * 1000L
    private val handler = Handler(Looper.getMainLooper())
    private var byteArray: ByteArray? = null
    private val resetConversationIdRunnable = Runnable {
        //chatViewModel.currentConversationId = null
    }
    private var isWaitingForResponse: Boolean = false
    override fun createBinding(): ActivityAnswer2Binding {
        return ActivityAnswer2Binding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        binding.icBackAnswers.setOnClickListener {
            GlobalFunction.startActivity(
                this@Answer2Activity,
                ScanActivityNew::class.java
            )
            finish()
        }
        chatViewModel = SmartCalculatorViewModel()
        chatRepository = ChatRepository(this)
        chatViewModel.currentConversationId = getStoredConversationId()

        val text = intent.getStringExtra("answer_rq2")
        Log.d("TextRecognition", "answer2: $text")

        byteArray = intent.getByteArrayExtra("image_data")
        if (byteArray != null) {
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
            binding.imgCrop.setImageBitmap(bitmap)
        }
        if (!text.isNullOrEmpty()) {
            sendMessage(text)
        } else {
            val bottomSheet2 = AnswerFoundBottomSheet()
            bottomSheet2.setOnclickAskQuestionFound(object :
                AnswerFoundBottomSheet.OnclickAskQuestionFound {
                override fun onClick() {
                    GlobalFunction.startActivity(this@Answer2Activity, ScanActivityNew::class.java)
                }

            })
            bottomSheet2.show(
                supportFragmentManager,
                bottomSheet2.tag
            )
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
                conversationId = "",
                isBot = false
            )

            chatViewModel.chatList.value!!.add(userMessage)
            //chatRepository.insertChatHis(userMessage)


//            runOnUiThread {
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
                                conversationId = "",
                                isBot = true
                            )
                            Log.d("TAG123", "onNext: ${botMessage.answer}")
                            chatViewModel.chatList.value!!.add(botMessage)
                            //chatRepository.insertChatHis(botMessage)


                            runOnUiThread {
                                val solutionSteps = botMessage.answer
                                Log.d("TAG123", "loi giai: $solutionSteps")
                                val recapKeyword = "Recap:**"
                                val result = solutionSteps.substringAfter(recapKeyword).trim()
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

                                        val regex = Regex(
                                            """\d+\.\s(.+?)(?=\d+\.\s|$)""",
                                            RegexOption.DOT_MATCHES_ALL
                                        )
                                        val stepsList = regex.findAll(stepsSection)
                                            .map { it.groupValues[1].trim() }.toMutableList()

                                        stepsList.add("Resuilt:$result")
                                        if (stepsList.isNotEmpty()) {
                                            val scanList =
                                                ScanModel(
                                                    "Resuilt:$result",
                                                    solutionSteps,
                                                    byteArray!!,
                                                    createdAt = (System.currentTimeMillis()
                                                        .toString()),
                                                    conversationId = ""
                                                )

                                            chatRepository.insertScanList(scanList)

                                            // Hiển thị BottomSheet với danh sách bước
                                            val bottomSheet = StepsBottomSheet(stepsList)
                                            bottomSheet.show(
                                                supportFragmentManager,
                                                bottomSheet.tag
                                            )

                                        } else {
                                            val bottomSheet2 = AnswerFoundBottomSheet()
                                            bottomSheet2.setOnclickAskQuestionFound(object :
                                                AnswerFoundBottomSheet.OnclickAskQuestionFound {
                                                override fun onClick() {
                                                    GlobalFunction.startActivity(
                                                        this@Answer2Activity,
                                                        ScanActivityNew::class.java
                                                    )
                                                }

                                            })
                                            bottomSheet2.show(
                                                supportFragmentManager,
                                                bottomSheet2.tag
                                            )
                                        }
                                    } else {
                                        val bottomSheet2 = AnswerFoundBottomSheet()
                                        bottomSheet2.setOnclickAskQuestionFound(object :
                                            AnswerFoundBottomSheet.OnclickAskQuestionFound {
                                            override fun onClick() {
                                                GlobalFunction.startActivity(
                                                    this@Answer2Activity,
                                                    ScanActivityNew::class.java
                                                )
                                            }

                                        })
                                        bottomSheet2.show(
                                            supportFragmentManager,
                                            bottomSheet2.tag
                                        )
                                    }
                                } else {
                                    val bottomSheet2 = AnswerFoundBottomSheet()
                                    bottomSheet2.setOnclickAskQuestionFound(object :
                                        AnswerFoundBottomSheet.OnclickAskQuestionFound {
                                        override fun onClick() {
                                            GlobalFunction.startActivity(
                                                this@Answer2Activity,
                                                ScanActivityNew::class.java
                                            )
                                        }

                                    })
                                    bottomSheet2.show(
                                        supportFragmentManager,
                                        bottomSheet2.tag
                                    )
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
            //startConversationTimeoutHandler()
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
