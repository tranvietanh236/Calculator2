package com.v1.smartv1alculatorv1.ui.chat_ai.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.v1.smartv1alculatorv1.Model.ChatAnswer


class ChatViewModel : ViewModel() {
    val chatList: MutableLiveData<MutableList<ChatAnswer>> by lazy {
        MutableLiveData<MutableList<ChatAnswer>>(ArrayList())
    }
    var currentConversationId: String? = null
}

