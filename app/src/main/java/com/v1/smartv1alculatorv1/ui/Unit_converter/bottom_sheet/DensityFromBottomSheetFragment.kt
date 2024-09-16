package com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.inteface.OnClickFromLengthBottomSheet
import com.v1.smartv1alculatorv1.untils.UnitPreferences

class DensityFromBottomSheetFragment : BottomSheetDialogFragment() {
    private var radioGroupUnits: RadioGroup? = null
    private var listener: OnClickFromLengthBottomSheet? = null

    fun setOnUnitSelectedListener(listener: OnClickFromLengthBottomSheet) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_density, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroupUnits = view.findViewById(R.id.radio_group_units)
        // Khôi phục giá trị đã lưu
        val savedUnit = UnitPreferences.getFromDensityUnit(requireContext())
        when (savedUnit) {
            "kg/m³" -> radioGroupUnits?.check(R.id.radio_kg_per_m3)
            "g/cm³" -> radioGroupUnits?.check(R.id.radio_g_per_cm3)
            "lb/ft³" -> radioGroupUnits?.check(R.id.radio_lb_per_ft3)
            "lb/in³" -> radioGroupUnits?.check(R.id.radio_lb_per_in3)
            "oz/in³" -> radioGroupUnits?.check(R.id.radio_oz_per_in3)
            else -> radioGroupUnits?.check(R.id.radio_kg_per_m3)
        }
        // Thiết lập sự kiện khi chọn RadioButton
        radioGroupUnits?.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.radio_kg_per_m3 -> "kg/m³"
                R.id.radio_g_per_cm3 -> "g/cm³"
                R.id.radio_lb_per_ft3 -> "lb/ft³"
                R.id.radio_lb_per_in3 -> "lb/in³"
                R.id.radio_oz_per_in3 -> "oz/in³"
                else -> ""
            }
            UnitPreferences.saveFromTempUnit(requireContext(), selectedUnit) // Lưu giá trị đã chọn
            listener?.onUnitSelected(selectedUnit) // Gọi callback để truyền dữ liệu
            dismiss() // Đóng Bottom Sheet sau khi chọn
        }
    }
}
