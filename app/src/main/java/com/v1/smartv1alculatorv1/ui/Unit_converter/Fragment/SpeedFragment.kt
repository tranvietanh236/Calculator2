package com.v1.smartv1alculatorv1.ui.Unit_converter.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.calculator.customformula.base.BaseFragment
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.databinding.FragmentMassBinding
import com.v1.smartv1alculatorv1.databinding.FragmentSpeedBinding
import com.v1.smartv1alculatorv1.inteface.OnClickFromLengthBottomSheet
import com.v1.smartv1alculatorv1.inteface.OnClickToLengthBottomSheet
import com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet.MassFromBottomSheetFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet.MassToBottomSheetFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet.SpeedFromBottomSheetFragment
import com.v1.smartv1alculatorv1.ui.Unit_converter.bottom_sheet.SpeedToBottomSheetFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpeedFragment : BaseFragment<FragmentSpeedBinding>(), OnClickFromLengthBottomSheet,
    OnClickToLengthBottomSheet {

    override fun inflateViewBinding(): FragmentSpeedBinding {
        return FragmentSpeedBinding.inflate(layoutInflater)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        super.initView()
        viewBinding.editTextValue.setOnTouchListener { v, event ->
            v.performClick() // Đảm bảo rằng EditText vẫn nhận được sự kiện click
            v.requestFocus() // Yêu cầu EditText được focus
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
            openBottomSheetFrom()
        }
        viewBinding.imageTo.setOnClickListener {
            openBottomSheetTo()
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

        // Chuyển đổi giá trị đầu vào sang Meter per Second (m/s)
        val valueInMetersPerSecond = when (fromUnit) {
            "Km/h" -> value / 3.6                   // 1 km/h = 0.27778 m/s
            "Mph" -> value * 0.44704                      // 1 mph = 0.44704 m/s
            "Kn" -> value * 0.514444                              // 1 kn = 0.514444 m/s
            "Ft/s" -> value * 0.3048                    // 1 ft/s = 0.3048 m/s
            else -> value                                                  // Giả sử mặc định là Meter per Second
        }

        // Chuyển đổi giá trị từ Meter per Second (m/s) sang đơn vị mục tiêu
        val result = when (toUnit) {
            "Km/h" -> valueInMetersPerSecond * 3.6   // 1 m/s = 3.6 km/h
            "Mph" -> valueInMetersPerSecond / 0.44704      // 1 m/s = 2.23694 mph
            "Kn" -> valueInMetersPerSecond / 0.514444                // 1 m/s = 1.94384 kn
            "Ft/s" -> valueInMetersPerSecond / 0.3048    // 1 m/s = 3.28084 ft/s
            else -> valueInMetersPerSecond                                  // Giả sử mặc định là Meter per Second
        }

        viewBinding.textViewResult.text = result.toString()
        //"%.4f".format(result)  // Định dạng kết quả nếu cần
    }


    private fun swapUnits() {
        val fromPosition = viewBinding.spinnerFrom.text
        val toPosition = viewBinding.spinnerTo.text
        viewBinding.spinnerFrom.text = toPosition
        viewBinding.spinnerTo.text = fromPosition
        convert()
    }



    fun openBottomSheetFrom(){
        val unitsBottomSheet = SpeedFromBottomSheetFragment()
        unitsBottomSheet.setOnUnitSelectedListener(this)
        unitsBottomSheet.show(parentFragmentManager, unitsBottomSheet.tag)

    }
    fun openBottomSheetTo(){
        val unitsBottomSheet = SpeedToBottomSheetFragment()
        unitsBottomSheet.setOnUnitSelectedListener(this)
        unitsBottomSheet.show(parentFragmentManager, unitsBottomSheet.tag)

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


}