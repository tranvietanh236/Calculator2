package com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.inteface.OnClickFromLengthBottomSheet
import com.v1.smartv1alculatorv1.inteface.OnClickToLengthBottomSheet
import com.v1.smartv1alculatorv1.untils.UnitPreferences

class VolumeToBottomSheetFragment : BottomSheetDialogFragment() {
    private var radioGroupUnits: RadioGroup? = null
    private var listener: OnClickToLengthBottomSheet? = null

    fun setOnUnitSelectedListener(listener: OnClickToLengthBottomSheet) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_volume, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroupUnits = view.findViewById(R.id.radio_group_units)
        val savedUnit = UnitPreferences.getToVolumeUnit(requireContext())
        when (savedUnit) {
            "M³" -> radioGroupUnits?.check(R.id.radio_cubic_meter)
            "L" -> radioGroupUnits?.check(R.id.radio_liter)
            "ML" -> radioGroupUnits?.check(R.id.radio_milliliter)
            "Cm³" -> radioGroupUnits?.check(R.id.radio_cubic_centimeter)
            "Dm³" -> radioGroupUnits?.check(R.id.radio_cubic_decimeter)
            "HL" -> radioGroupUnits?.check(R.id.radio_hectoliter)
            else -> radioGroupUnits?.check(R.id.radio_meter_per_second)
        }
        // Thiết lập sự kiện khi chọn RadioButton
        radioGroupUnits?.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.radio_cubic_meter -> "M³"
                R.id.radio_liter -> "L"
                R.id.radio_milliliter -> "ML"
                R.id.radio_cubic_centimeter -> "Cm³"
                R.id.radio_cubic_decimeter -> "Dm³"
                R.id.radio_hectoliter -> "HL"
                else -> ""
            }
            UnitPreferences.saveToVolumeUnit(requireContext(), selectedUnit)
            listener?.onUnitToSelected(selectedUnit) // Gọi callback để truyền dữ liệu
            dismiss() // Đóng Bottom Sheet sau khi chọn
        }
    }
}
