package com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet

import UnitsAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.inteface.OnClickFromLengthBottomSheet
import com.v1.smartv1alculatorv1.ui.Unit_converter.model.ConverterModel
import com.v1.smartv1alculatorv1.untils.UnitPreferences

class TempFromBottomSheetFragment : BottomSheetDialogFragment() {
    var selectedPosition: Int? = null
    private var ic_close : ImageView? = null
    private var listener: OnClickFromLengthBottomSheet? = null

    fun setOnUnitSelectedListener(listener: OnClickFromLengthBottomSheet) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_temp, container, false) // Đổi layout nếu cần
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        ic_close = view.findViewById(R.id.ic_close_temp)
        ic_close?.setOnClickListener {
            dismiss()
        }
        selectedPosition = UnitPreferences.loadSavedPositionFromTemp(requireContext())
        val recyclerViewUnits: RecyclerView = view.findViewById(R.id.recyclerView)
        val unitList = listOf(
            ConverterModel("Degree Celsius", "°C"),
            ConverterModel("Fahrenheit", "°F"),
            ConverterModel("Kelvin", "K"),

        )

        recyclerViewUnits.layoutManager = LinearLayoutManager(context)
        val adapter = UnitsAdapter(requireContext(), unitList) { selectedUnit, index->
            UnitPreferences.saveFromTempUnit(requireContext(), selectedUnit)
            UnitPreferences.saveSelectedPositionFromTemp(requireContext(), index)
            listener?.onUnitSelected(selectedUnit)
            Log.d("TAG123", "onViewCreated: $selectedUnit")
            //dismiss()
        }
        adapter.updatePosition(selectedPosition!!)
        recyclerViewUnits.adapter = adapter
    }
    override fun dismiss() {
        super.dismiss()
        listener?.onDismissListener()
    }
}