package com.v1.smartv1alculatorv1.ui.smartcalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.v1.smartv1alculatorv1.R

class StepsAnswerBottomSheet(
    private val stepsListAnswser: List<String>
) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnswerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewStepsAnswer)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = AnswerAdapter(stepsListAnswser)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(true) // Không cho phép tắt bằng cách chạm ra ngoài
        dialog?.setCancelable(true)
    }



}