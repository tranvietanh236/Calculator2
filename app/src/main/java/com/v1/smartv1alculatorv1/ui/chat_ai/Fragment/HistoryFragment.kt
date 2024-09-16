package com.v1.smartv1alculatorv1.ui.chat_ai.Fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.calculator.customformula.base.BaseFragment
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.databinding.FragmentHistoryBinding
import com.v1.smartv1alculatorv1.ui.chat_ai.Adapter.ChatAdapter


class HistoryFragment : BaseFragment<FragmentHistoryBinding>() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatRepository: ChatRepository
    private var chatList: MutableList<ChatAnswer> = mutableListOf()

    override fun inflateViewBinding() = FragmentHistoryBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatRepository = ChatRepository(requireContext())

        chatAdapter = ChatAdapter(chatList, viewBinding.textViewEmptyConversation)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = chatAdapter

        loadChatHistory()
    }

    override fun onResume() {
        super.onResume()
        loadChatHistory()
    }

    private fun loadChatHistory() {
        chatList.clear()
        chatList.addAll(chatRepository.getAllChats())
        if (chatList.isEmpty()) {
            viewBinding.textViewEmptyConversation.visibility = View.VISIBLE
        } else {
            viewBinding.textViewEmptyConversation.visibility = View.GONE
        }
        chatAdapter.notifyDataSetChanged()
    }
}
