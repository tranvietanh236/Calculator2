package com.v1.smartv1alculatorv1.ui.history.activity

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityHistoryBinding
import com.v1.smartv1alculatorv1.ui.history.HistoryAdapter

class HistoryActivity : BaseActivity<ActivityHistoryBinding, BaseViewModel>() {
    private lateinit var chatAdapter: HistoryAdapter
    private lateinit var chatRepository: ChatRepository
    private var chatList: MutableList<ChatAnswer> = mutableListOf()

    override fun createBinding(): ActivityHistoryBinding {
        return ActivityHistoryBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        chatRepository = ChatRepository(this)
        chatAdapter = HistoryAdapter(chatList, binding.textViewEmptyConversation)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = chatAdapter
        loadChatHistory()

        binding.icBackHis.setOnClickListener {
            finish()
        }
    }

    private fun loadChatHistory() {
        chatList.clear()
        chatList.addAll(chatRepository.getAllChatsHis())
        if (chatList.isEmpty()) {
            binding.textViewEmptyConversation.visibility = View.VISIBLE
        } else {
            binding.textViewEmptyConversation.visibility = View.GONE
        }
        chatAdapter.notifyDataSetChanged()
    }
}