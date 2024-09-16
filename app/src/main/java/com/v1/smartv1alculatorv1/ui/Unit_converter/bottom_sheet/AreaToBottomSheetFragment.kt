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

class AreaToBottomSheetFragment : BottomSheetDialogFragment() {
    private var radioGroupUnits: RadioGroup? = null
    private var listener: OnClickToLengthBottomSheet? = null

    fun setOnUnitSelectedListener(listener: OnClickToLengthBottomSheet) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_area, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroupUnits = view.findViewById(R.id.radio_group_units)


        // Đọc giá trị từ SharedPreferences và đánh dấu RadioButton đã chọn
        val selectedUnit = UnitPreferences.getToAreaUnit(requireContext())

        // Nếu không có giá trị đã chọn, chọn mục đầu tiên (M²)
        val checkedId = when (selectedUnit) {
            "M²" -> R.id.radio_metre_squared
            "Km²" -> R.id.radio_kilometre_squared
            "Cm²" -> R.id.radio_centimetre_squared
            "Mm²" -> R.id.radio_millimetre_squared
            "Mile²" -> R.id.radio_mile_squared
            "Yard²" -> R.id.radio_yard_squared
            "Foot²" -> R.id.radio_foot_squared
            "Inch²" -> R.id.radio_inch_squared
            else -> R.id.radio_metre_squared // Mặc định chọn M²
        }

        radioGroupUnits?.check(checkedId)

        // Thiết lập sự kiện khi chọn RadioButton
        radioGroupUnits?.setOnCheckedChangeListener { _, checkedId ->
            val selectedUnit = when (checkedId) {
                R.id.radio_metre_squared -> "M²"
                R.id.radio_kilometre_squared -> "Km²"
                R.id.radio_centimetre_squared -> "Cm²"
                R.id.radio_millimetre_squared -> "Mm²"
                R.id.radio_mile_squared -> "Mile²"
                R.id.radio_yard_squared -> "Yard²"
                R.id.radio_foot_squared -> "Foot²"
                R.id.radio_inch_squared -> "Inch²"
                else -> ""
            }
            UnitPreferences.saveToAreaUnit(requireContext(), selectedUnit)
            listener?.onUnitToSelected(selectedUnit) // Gọi callback để truyền dữ liệu
            dismiss() // Đóng Bottom Sheet sau khi chọn
        }
    }
}
