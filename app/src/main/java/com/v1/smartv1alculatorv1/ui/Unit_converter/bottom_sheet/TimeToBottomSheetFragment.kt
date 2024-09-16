package com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.inteface.OnClickToLengthBottomSheet
import com.v1.smartv1alculatorv1.untils.UnitPreferences

class TimeToBottomSheetFragment: BottomSheetDialogFragment() {
    private var radioGroupUnits: RadioGroup? = null
    private var listener: OnClickToLengthBottomSheet? = null

    fun setOnUnitSelectedListener(listener: OnClickToLengthBottomSheet) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroupUnits = view.findViewById(R.id.radio_group_units)
        val savedUnit = UnitPreferences.getToTimeUnit(requireContext())
        when (savedUnit) {
            "S" -> radioGroupUnits?.check(R.id.radio_second)
            "M" -> radioGroupUnits?.check(R.id.radio_minute)
            "H" -> radioGroupUnits?.check(R.id.radio_hour)
            "Day" -> radioGroupUnits?.check(R.id.radio_day)
            "Week" -> radioGroupUnits?.check(R.id.radio_week)
            else -> radioGroupUnits?.check(R.id.radio_second)
        }
        // Thiết lập sự kiện khi chọn RadioButton
        radioGroupUnits?.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.radio_second -> "S"
                R.id.radio_minute -> "M"
                R.id.radio_hour -> "H"
                R.id.radio_day -> "Day"
                R.id.radio_week -> "Week"
                else -> ""
            }
            UnitPreferences.saveToTimeUnit(requireContext(), selectedUnit)
            listener?.onUnitToSelected(selectedUnit) // Gọi callback để truyền dữ liệu
            dismiss() // Đóng Bottom Sheet sau khi chọn
        }
    }
}
