package com.v1.smartv1alculatorv1.ui.history


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.calculator.customformula.base.BaseFragment
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.HistoryModel
import com.v1.smartv1alculatorv1.Model.ScanModel
import com.v1.smartv1alculatorv1.databinding.FragmentHisScanBinding
import com.v1.smartv1alculatorv1.ui.history.adapter.HistoryAdapterNew
import com.v1.smartv1alculatorv1.ui.history.adapter.ScanAdapter


class HisScanFragment : BaseFragment<FragmentHisScanBinding>() {
    lateinit var chatRepository: ChatRepository
    private var historyScanList: MutableList<ScanModel> = mutableListOf()
    private lateinit var chatAdapter: ScanAdapter
    override fun inflateViewBinding(): FragmentHisScanBinding {
        return FragmentHisScanBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatRepository = ChatRepository(requireContext())
        chatAdapter = ScanAdapter(historyScanList, requireContext())
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.recyclerView.adapter = chatAdapter
        loadChatHistoryNew()
    }

    fun loadChatHistoryNew() {
        historyScanList.clear()
        historyScanList.addAll(chatRepository.getAllScanList())
        if (historyScanList.isEmpty()) {
            viewBinding.textViewEmptyConversation.visibility = View.VISIBLE
        } else {
            viewBinding.textViewEmptyConversation.visibility = View.GONE
        }
        chatAdapter.notifyDataSetChanged()
        Log.d("TAG123", "loadChatHistoryNew: ${historyScanList.size}")
    }

    override fun onResume() {
        super.onResume()
        loadChatHistoryNew()
    }

}