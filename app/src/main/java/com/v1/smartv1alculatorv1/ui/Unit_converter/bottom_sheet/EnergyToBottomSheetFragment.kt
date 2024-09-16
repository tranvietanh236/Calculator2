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

class EnergyToBottomSheetFragment : BottomSheetDialogFragment() {
    private var radioGroupUnits: RadioGroup? = null
    private var listener: OnClickToLengthBottomSheet? = null

    fun setOnUnitSelectedListener(listener: OnClickToLengthBottomSheet) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_energy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroupUnits = view.findViewById(R.id.radio_group_units)
        val savedUnit = UnitPreferences.getToEnergyUnit(requireContext())
        when (savedUnit) {
            "Je" -> radioGroupUnits?.check(R.id.radio_joule)
            "KJ" -> radioGroupUnits?.check(R.id.radio_kilojoule)
            "Cal" -> radioGroupUnits?.check(R.id.radio_calorie)
            "Kcal" -> radioGroupUnits?.check(R.id.radio_kilocalorie)
            "BTU" -> radioGroupUnits?.check(R.id.radio_btu)
            else -> radioGroupUnits?.check(R.id.radio_joule)
        }
        // Thiết lập sự kiện khi chọn RadioButton
        radioGroupUnits?.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.radio_joule -> "Je"
                R.id.radio_kilojoule -> "KJ"
                R.id.radio_calorie -> "Cal"
                R.id.radio_kilocalorie -> "Kcal"
                R.id.radio_btu -> "BTU"
                else -> ""
            }
            UnitPreferences.saveToEnergyUnit(requireContext(), selectedUnit) // Lưu giá trị đã chọn
            listener?.onUnitToSelected(selectedUnit) // Gọi callback để truyền dữ liệu
            dismiss() // Đóng Bottom Sheet sau khi chọn
        }
    }
}
