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

class SpeedToBottomSheetFragment : BottomSheetDialogFragment() {
    private var radioGroupUnits: RadioGroup? = null
    private var listener: OnClickToLengthBottomSheet? = null

    fun setOnUnitSelectedListener(listener: OnClickToLengthBottomSheet) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_speed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroupUnits = view.findViewById(R.id.radio_group_units)
        val savedUnit = UnitPreferences.getToSpeedUnit(requireContext())
        when (savedUnit) {
            "M/s" -> radioGroupUnits?.check(R.id.radio_meter_per_second)
            "Km/h" -> radioGroupUnits?.check(R.id.radio_kilometer_per_hour)
            "Mph" -> radioGroupUnits?.check(R.id.radio_mile_per_hour)
            "Kn" -> radioGroupUnits?.check(R.id.radio_knot)
            "Ft/s" -> radioGroupUnits?.check(R.id.radio_feet_per_second)
            else -> radioGroupUnits?.check(R.id.radio_meter_per_second)
        }
        // Thiết lập sự kiện khi chọn RadioButton
        radioGroupUnits?.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.radio_meter_per_second -> "M/s"
                R.id.radio_kilometer_per_hour -> "Km/h"
                R.id.radio_mile_per_hour -> "Mph"
                R.id.radio_knot -> "Kn"
                R.id.radio_feet_per_second -> "Ft/s"
                else -> ""
            }
            UnitPreferences.saveToSpeedUnit(requireContext(), selectedUnit)
            listener?.onUnitToSelected(selectedUnit) // Gọi callback để truyền dữ liệu
            dismiss() // Đóng Bottom Sheet sau khi chọn
        }
    }
}
