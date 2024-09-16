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

class LengthToBottomSheetFragment : BottomSheetDialogFragment() {
    private var radioGroupUnits: RadioGroup? = null
    private var listener: OnClickToLengthBottomSheet? = null


    fun setOnUnitSelectedListener(listener: OnClickToLengthBottomSheet) {
        this.listener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_units, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radioGroupUnits = view.findViewById(R.id.radio_group_units)

        // Khôi phục giá trị đã lưu
        val savedUnit = UnitPreferences.getToLengthUnit(requireContext())
        when (savedUnit) {
            "M" -> radioGroupUnits?.check(R.id.radio_metre)
            "Km" -> radioGroupUnits?.check(R.id.radio_kilometre)
            "Cm" -> radioGroupUnits?.check(R.id.radio_centimetre)
            "Mm" -> radioGroupUnits?.check(R.id.radio_millimetre)
            "Mile" -> radioGroupUnits?.check(R.id.radio_mile)
            "Yard" -> radioGroupUnits?.check(R.id.radio_yard)
            "Foot" -> radioGroupUnits?.check(R.id.radio_foot)
            "Inch" -> radioGroupUnits?.check(R.id.radio_inch)
            else -> radioGroupUnits?.check(R.id.radio_metre)
        }


        // Thiết lập sự kiện khi chọn RadioButton
        radioGroupUnits?.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.radio_metre -> "M"
                R.id.radio_kilometre -> "Km"
                R.id.radio_centimetre -> "Cm"
                R.id.radio_millimetre -> "Mm"
                R.id.radio_mile -> "Mile"
                R.id.radio_yard -> "Yard"
                R.id.radio_foot -> "Foot"
                R.id.radio_inch -> "Inch"
                else -> ""
            }
            UnitPreferences.saveToLengthUnit(requireContext(), selectedUnit)
            listener?.onUnitToSelected(selectedUnit) // Gọi callback để truyền dữ liệu
            dismiss() // Đóng Bottom Sheet sau khi chọn
        }
    }
}