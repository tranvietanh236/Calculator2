package com.v1.smartv1alculatorv1.ui.history

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.calculator.customformula.base.BaseFragment
import com.calculator.customformula.utils.GlobalFunction
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.HistoryModel
import com.v1.smartv1alculatorv1.databinding.FragmentHisTutorBinding
import com.v1.smartv1alculatorv1.inteface.OnItemHisClickListener
import com.v1.smartv1alculatorv1.ui.history.activity.DetailHistoryActivity
import com.v1.smartv1alculatorv1.ui.history.adapter.HistoryAdapterNew


class HisTutorFragment : BaseFragment<FragmentHisTutorBinding>(), OnItemHisClickListener {
    lateinit var chatAdapter: HistoryAdapterNew
    private lateinit var chatRepository: ChatRepository
    private var chatList: MutableList<HistoryModel> = mutableListOf()
    private var historyList : MutableList<HistoryModel> = mutableListOf()

    override fun inflateViewBinding(): FragmentHisTutorBinding {
        return FragmentHisTutorBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatRepository = ChatRepository(requireContext())
        chatAdapter = HistoryAdapterNew(chatList, requireContext(), this)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = chatAdapter
        //loadChatHistoryNew()
        loadChatHistory()
    }

    override fun onResume() {
        super.onResume()
        loadChatHistory()
    }

    fun loadChatHistory() {
        chatList.clear()
        chatList.addAll(chatRepository.getAllHistory())
        if (chatList.isEmpty()) {
            viewBinding.textViewEmptyConversation.visibility = View.VISIBLE
        } else {
            viewBinding.textViewEmptyConversation.visibility = View.GONE
        }
        chatAdapter.notifyDataSetChanged()
    }

//    fun loadChatHistoryNew(){
//        historyList.clear()
//        historyList.addAll(chatRepository.getAllHistory())
//
//    }

    override fun onItemClick(data: HistoryModel) {
        GlobalFunction.startActivity(requireContext(), DetailHistoryActivity::class.java)
    }
}
