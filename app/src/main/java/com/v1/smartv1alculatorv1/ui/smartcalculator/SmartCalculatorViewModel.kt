package com.v1.smartv1alculatorv1.ui.smartcalculator

import androidx.lifecycle.MutableLiveData
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.base.BaseViewModel

class SmartCalculatorViewModel : BaseViewModel() {
    val chatList: MutableLiveData<MutableList<ChatAnswer>> by lazy {
        MutableLiveData<MutableList<ChatAnswer>>(ArrayList())
    }
    var currentConversationId: String? = null
}