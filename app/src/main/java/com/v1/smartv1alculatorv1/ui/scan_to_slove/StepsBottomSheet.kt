package com.v1.smartv1alculatorv1.ui.scan_to_slove

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.ui.scan_to_slove.adapter.StepsAdapter

class StepsBottomSheet(private val stepsList: List<String>) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StepsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewSteps)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = StepsAdapter(stepsList)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false) // Không cho phép tắt bằng cách chạm ra ngoài
        dialog?.setCancelable(false)
    }
}
