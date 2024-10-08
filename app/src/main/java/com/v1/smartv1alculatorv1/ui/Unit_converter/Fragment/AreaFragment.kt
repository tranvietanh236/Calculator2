package com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment

import AreaFromBottomSheetFragment
import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.calculator.customformula.base.BaseFragment
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.databinding.FragmentAreaBinding
import com.v1.smartv1alculatorv1.inteface.OnClickFromLengthBottomSheet
import com.v1.smartv1alculatorv1.inteface.OnClickToLengthBottomSheet
import com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet.AreaToBottomSheetFragment
import com.v1.smartv1alculatorv1.untils.UnitPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AreaFragment : BaseFragment<FragmentAreaBinding>(), OnClickFromLengthBottomSheet,
    OnClickToLengthBottomSheet {

    private var check = false
    override fun inflateViewBinding(): FragmentAreaBinding {
        return FragmentAreaBinding.inflate(layoutInflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        super.initView()
        convert()
        val from = UnitPreferences.getFromAreaUnit(requireContext())
        val to = UnitPreferences.getToAreaUnit(requireContext())
        viewBinding.spinnerFrom.text = from
        viewBinding.spinnerTo.text = to
        viewBinding.editTextValue.setOnTouchListener { v, event ->
            v.performClick() // Đảm bảo rằng EditText vẫn nhận được sự kiện click
            v.requestFocus() // Yêu cầu EditText được focus
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0) // Ẩn bàn phím khi EditText được nhấn
            true
        }

        viewBinding.editTextValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convert()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        viewBinding.imageFrom.setOnClickListener {
            if (!check) {
                check = true
                openBottomSheetFrom()
            }

        }
        viewBinding.imageTo.setOnClickListener {
            if (!check) {
                check = true
                openBottomSheetTo()
            }
        }

        // Đặt listener cho nút đảo ngược đơn vị
        viewBinding.icSwap.setOnClickListener {
            swapUnits()
        }

        viewBinding.btnOne.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnTwo.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnThree.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnFouth.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnFive.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnSix.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnSeven.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnEight.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnNine.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnZezo.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnPoint.setOnClickListener { handleButtonClick(it) }
        viewBinding.btnDelete.setOnClickListener { handleButtonClick(it) }

    }

    private fun convert() {
        val value = viewBinding.editTextValue.text.toString().toDoubleOrNull() ?: return
        val fromUnit = viewBinding.spinnerFrom.text.toString()
        val toUnit = viewBinding.spinnerTo.text.toString()

        // Chuyển đổi giá trị đầu vào thành mét vuông (m²)
        val valueInSquareMetres = when (fromUnit) {
            "km²" -> value * 1_000_000                 // 1 km² = 1,000,000 m²
            "ha" -> value * 10_000                     // 1 ha = 10,000 m²
            "dam²" -> value * 100                      // 1 dam² = 100 m²
            "m²" -> value                              // 1 m² = 1 m²
            "dm²" -> value / 100                       // 1 dm² = 0.01 m²
            "cm²" -> value / 10_000                    // 1 cm² = 0.0001 m²
            "mm²" -> value / 1_000_000                 // 1 mm² = 0.000001 m²
            "acre" -> value * 4_046.85642              // 1 acre = 4046.85642 m²
            "mi²" -> value * 2_589_988.110336          // 1 mi² = 2,589,988.11 m²
            "yd²" -> value * 0.836127                  // 1 yd² = 0.836127 m²
            "ft²" -> value * 0.092903                  // 1 ft² = 0.092903 m²
            "in²" -> value * 0.00064516                // 1 in² = 0.00064516 m²
            else -> value
        }

        // Chuyển đổi từ mét vuông sang đơn vị đích
        val result = when (toUnit) {
            "km²" -> valueInSquareMetres / 1_000_000
            "ha" -> valueInSquareMetres / 10_000
            "dam²" -> valueInSquareMetres / 100
            "m²" -> valueInSquareMetres
            "dm²" -> valueInSquareMetres * 100
            "cm²" -> valueInSquareMetres * 10_000
            "mm²" -> valueInSquareMetres * 1_000_000
            "acre" -> valueInSquareMetres / 4_046.85642
            "mi²" -> valueInSquareMetres / 2_589_988.110336
            "yd²" -> valueInSquareMetres / 0.836127
            "ft²" -> valueInSquareMetres / 0.092903
            "in²" -> valueInSquareMetres / 0.00064516
            else -> valueInSquareMetres
        }

        val symbols = DecimalFormatSymbols().apply {
            decimalSeparator = '.'
        }
        val decimalFormat = DecimalFormat("0.##################################", symbols)
        viewBinding.textViewResult.text = decimalFormat.format(result)
    }

    private fun swapUnits() {
        val fromPosition = viewBinding.spinnerFrom.text
        val toPosition = viewBinding.spinnerTo.text
        viewBinding.spinnerFrom.text = toPosition
        viewBinding.spinnerTo.text = fromPosition
        convert()
    }


    fun openBottomSheetFrom() {
        val unitsBottomSheet = AreaFromBottomSheetFragment()
        unitsBottomSheet.setOnUnitSelectedListener(this)
        unitsBottomSheet.show(parentFragmentManager, unitsBottomSheet.tag)
        unitsBottomSheet.dialog?.setOnDismissListener {
            check = false
        }

    }

    fun openBottomSheetTo() {
        val unitsBottomSheet = AreaToBottomSheetFragment()
        unitsBottomSheet.setOnUnitSelectedListener(this)
        unitsBottomSheet.show(parentFragmentManager, unitsBottomSheet.tag)
        unitsBottomSheet.dialog?.setOnDismissListener {
            check = false
        }

    }

    private fun updateDisplay(view: View, value: String) {
        lifecycleScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                // Xử lý rung khi nhấn phím (nếu cần)
            }

            val formerValue = viewBinding.editTextValue.text.toString()
            val cursorPosition = formerValue.length  // Đặt con trỏ vào cuối (bên phải)
            val leftValue = formerValue.substring(0, cursorPosition)
            val rightValue = formerValue.substring(cursorPosition)

            // Nếu giá trị mới là dấu chấm và đã có dấu chấm, không thêm nữa
            val newValue = when {
                value == "." && (leftValue.contains(".") || rightValue.contains(".")) -> formerValue
                else -> leftValue + value + rightValue
            }

            // Cập nhật văn bản
            withContext(Dispatchers.Main) {
                viewBinding.editTextValue.setText(newValue)
                // Đặt con trỏ vào đầu văn bản (bên phải)
                viewBinding.editTextValue.setSelection(viewBinding.editTextValue.text.length)
            }
        }
    }


    private fun handleButtonClick(view: View) {
        when (view.id) {
            R.id.btn_one -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_two -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_one -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_three -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_fouth -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_five -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_six -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_seven -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_eight -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_nine -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_zezo -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_point -> {
                updateDisplay(view, (view as Button).text as String)
            }

            R.id.btn_delete -> {
                viewBinding.editTextValue.setText("")
                viewBinding.textViewResult.text = ""
            }

            else -> {

            }
        }
    }

    override fun onUnitSelected(unit: String) {
        viewBinding.spinnerFrom.text = unit
        convert()
    }


    override fun onUnitToSelected(unit: String) {
        viewBinding.spinnerTo.text = unit
        convert()
    }

    override fun onDismissToListener() {
        check = false
    }

    override fun onDismissListener() {
        check = false
    }

    override fun onResume() {
        super.onResume()
        check = false
    }
}