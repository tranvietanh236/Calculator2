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

class MassToBottomSheetFragment : BottomSheetDialogFragment() {
    private var radioGroupUnits: RadioGroup? = null
    private var listener: OnClickToLengthBottomSheet? = null

    fun setOnUnitSelectedListener(listener: OnClickToLengthBottomSheet) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_mass, container, false) // Sửa đổi tên layout thành bottom_sheet_mass
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroupUnits = view.findViewById(R.id.radio_group_units)
        val savedUnit = UnitPreferences.getMassToUnit(requireContext())
        when (savedUnit) {
            "Kg" -> radioGroupUnits?.check(R.id.radio_kilogram)
            "G" -> radioGroupUnits?.check(R.id.radio_gram)
            "Mg" -> radioGroupUnits?.check(R.id.radio_milligram)
            "Lb" -> radioGroupUnits?.check(R.id.radio_pound)
            "Oz" -> radioGroupUnits?.check(R.id.radio_ounce)
            else -> radioGroupUnits?.check(R.id.radio_kilogram)
        }
        // Thiết lập sự kiện khi chọn RadioButton
        radioGroupUnits?.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.radio_kilogram -> "Kg"
                R.id.radio_gram -> "G"
                R.id.radio_milligram -> "Mg"
                R.id.radio_pound -> "Lb"
                R.id.radio_ounce -> "Oz"
                else -> ""
            }
            UnitPreferences.saveToMassUnit(requireContext(), selectedUnit)
            listener?.onUnitToSelected(selectedUnit) // Gọi callback để truyền dữ liệu
            dismiss() // Đóng Bottom Sheet sau khi chọn
        }
    }
}
