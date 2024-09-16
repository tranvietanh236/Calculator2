package com.v1.smartv1alculatorv1.ui.smartcalculator

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.ChatAnswer
import com.v1.smartv1alculatorv1.Model.UserMessage
import com.v1.smartv1alculatorv1.api.ModuleChat
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivitySmartCalculatorBinding
import com.v1.smartv1alculatorv1.untils.NumberFormatter
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import java.text.DecimalFormatSymbols

class SmartCalculatorActivity :
    BaseActivity<ActivitySmartCalculatorBinding, SmartCalculatorViewModel>() {

    private val decimalSeparatorSymbol =
        DecimalFormatSymbols.getInstance().decimalSeparator.toString()
    private val groupingSeparatorSymbol =
        DecimalFormatSymbols.getInstance().groupingSeparator.toString()

    private var isInvButtonClicked = false
    private var isEqualLastAction = false
    private var isDegreeModeActivated = true // Set degree by default
    private var errorStatusOld = false
    override fun createBinding(): ActivitySmartCalculatorBinding {
        return ActivitySmartCalculatorBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): SmartCalculatorViewModel {
        return SmartCalculatorViewModel()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        super.initView()

        binding.icBackSmartcal.setOnClickListener {
            finish()
        }
        binding.nextCursorStart.setOnClickListener{
            showCursorAtStart()
        }
        binding.nextCursorRight.setOnClickListener{
            binding.input.requestFocus()
            moveCursor(1)
        }
        binding.nextCursorLeft.setOnClickListener{
            binding.input.requestFocus()
            moveCursor(-1)
        }
        binding.input.setOnTouchListener { v, event ->
            v.performClick() // Đảm bảo rằng EditText vẫn nhận được sự kiện click
            v.requestFocus() // Yêu cầu EditText được focus
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0) // Ẩn bàn phím khi EditText được nhấn
            true
        }

        binding.equalsButton.setOnClickListener {
            //sendMessage()
            val message = binding.input.text.toString()
            val intent = Intent(this@SmartCalculatorActivity, AnswerActivity::class.java)
            intent.putExtra("answer_rq", message)
            startActivity(intent)
        }
        binding.btnAbc1.setOnClickListener {
            binding.tableLayout.visibility = View.GONE
            binding.tableLayout2.visibility = View.VISIBLE

        }
        binding.btnAbc2.setOnClickListener {
            binding.tableLayout.visibility = View.VISIBLE
            binding.tableLayout2.visibility = View.GONE
        }
    }


    private fun updateDisplay(view: View, value: String) {
        val valueNoSeparators = value.replace(groupingSeparatorSymbol, "")
        val isValueInt = valueNoSeparators.toIntOrNull() != null

        // Reset input with current number if following "equal"
        if (isEqualLastAction) {
            if (isValueInt || value == decimalSeparatorSymbol) {
                binding.input.setText("")
            } else {
                binding.input.setSelection(binding.input.text.length)
                //binding.inputHorizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
            }
            isEqualLastAction = false
        }

        if (!binding.input.isCursorVisible) {
            binding.input.isCursorVisible = true
        }

        lifecycleScope.launch(Dispatchers.Default) {
            withContext(Dispatchers.Main) {
                // Vibrate when key pressed
                //keyVibration(view)
            }

            val formerValue = binding.input.text.toString()
            val cursorPosition = binding.input.selectionStart
            val leftValue = formerValue.subSequence(0, cursorPosition).toString()
            val leftValueFormatted =
                NumberFormatter.format(leftValue, decimalSeparatorSymbol, groupingSeparatorSymbol)
            val rightValue = formerValue.subSequence(cursorPosition, formerValue.length).toString()

            val newValue = leftValue + value + rightValue

            var newValueFormatted =
                NumberFormatter.format(newValue, decimalSeparatorSymbol, groupingSeparatorSymbol)

            withContext(Dispatchers.Main) {
                // Avoid two decimalSeparator in the same number
                // when you click on the decimalSeparator button
                if (value == decimalSeparatorSymbol && decimalSeparatorSymbol in binding.input.text.toString()) {
                    if (binding.input.text.toString().isNotEmpty()) {
                        var lastNumberBefore = ""
                        if (cursorPosition > 0 && binding.input.text.toString()
                                .substring(0, cursorPosition)
                                .last() in "0123456789\\$decimalSeparatorSymbol"
                        ) {
                            lastNumberBefore = NumberFormatter.extractNumbers(
                                binding.input.text.toString().substring(0, cursorPosition),
                                decimalSeparatorSymbol
                            ).last()
                        }
                        var firstNumberAfter = ""
                        if (cursorPosition < binding.input.text.length - 1) {
                            firstNumberAfter = NumberFormatter.extractNumbers(
                                binding.input.text.toString()
                                    .substring(cursorPosition, binding.input.text.length),
                                decimalSeparatorSymbol
                            ).first()
                        }
                        if (decimalSeparatorSymbol in lastNumberBefore || decimalSeparatorSymbol in firstNumberAfter) {
                            return@withContext
                        }
                    }
                }

                // Update Display
                binding.input.setText(newValueFormatted)

                // Set cursor position
                if (isValueInt) {
                    val cursorOffset = newValueFormatted.length - newValue.length
                    binding.input.setSelection(cursorPosition + value.length + cursorOffset)
                } else {
                    binding.input.setSelection(leftValueFormatted.length + value.length)
                }
            }
        }
    }

    private fun addSymbol(view: View, currentSymbol: String) {
        // Get input text length
        val textLength = binding.input.text.length

        // If the input is not empty
        if (textLength > 0) {
            // Get cursor's current position
            val cursorPosition = binding.input.selectionStart

            // Get next / previous characters relative to the cursor
            val nextChar =
                if (textLength - cursorPosition > 0) binding.input.text[cursorPosition].toString() else "0" // use "0" as default like it's not a symbol
            val previousChar =
                if (cursorPosition > 0) binding.input.text[cursorPosition - 1].toString() else "0"

            if (currentSymbol != previousChar // Ignore multiple presses of the same button
                && currentSymbol != nextChar
                && previousChar != "√" // No symbol can be added on an empty square root
                && previousChar != decimalSeparatorSymbol // Ensure that the previous character is not a comma
                && (previousChar != "(" // Ensure that we are not at the beginning of a parenthesis
                        || currentSymbol == "-")
            ) { // Minus symbol is an override
                // If previous character is a symbol, replace it
                if (previousChar.matches("[+\\-÷×^]".toRegex())) {
                    //keyVibration(view)

                    val leftString =
                        binding.input.text.subSequence(0, cursorPosition - 1).toString()
                    val rightString =
                        binding.input.text.subSequence(cursorPosition, textLength).toString()

                    // Add a parenthesis if there is another symbol before minus
                    if (currentSymbol == "-") {
                        if (previousChar in "+-") {
                            binding.input.setText(leftString + currentSymbol + rightString)
                            binding.input.setSelection(cursorPosition)
                        } else {
                            binding.input.setText(leftString + previousChar + currentSymbol + rightString)
                            binding.input.setSelection(cursorPosition + 1)
                        }
                    } else if (cursorPosition > 1 && binding.input.text[cursorPosition - 2] != '(') {
                        binding.input.setText(leftString + currentSymbol + rightString)
                        binding.input.setSelection(cursorPosition)
                    } else if (currentSymbol == "+") {
                        binding.input.setText(leftString + rightString)
                        binding.input.setSelection(cursorPosition - 1)
                    }
                }
                // If next character is a symbol, replace it
                else if (nextChar.matches("[+\\-÷×^%!]".toRegex())
                    && currentSymbol != "%"
                ) { // Make sure that percent symbol doesn't replace succeeding symbols
                    //keyVibration(view)

                    val leftString = binding.input.text.subSequence(0, cursorPosition).toString()
                    val rightString =
                        binding.input.text.subSequence(cursorPosition + 1, textLength).toString()

                    if (cursorPosition > 0 && previousChar != "(") {
                        binding.input.setText(leftString + currentSymbol + rightString)
                        binding.input.setSelection(cursorPosition + 1)
                    } else if (currentSymbol == "+") binding.input.setText(leftString + rightString)
                }
                // Otherwise just update the display
                else if (cursorPosition > 0 || nextChar != "0" && currentSymbol == "-") {
                    updateDisplay(view, currentSymbol)
                } else ""
            } else ""
        } else { // Allow minus symbol, even if the input is empty
            if (currentSymbol == "-") updateDisplay(view, currentSymbol)
            else ""
        }
    }

    fun keyDigitPadMappingToDisplay(view: View) {
        updateDisplay(view, (view as Button).text as String)
    }

    fun addButton(view: View) {
        addSymbol(view, "+")
    }

    fun subtractButton(view: View) {
        addSymbol(view, "-")
    }

    fun divideButton(view: View) {
        addSymbol(view, "÷")
    }

    fun multiplyButton(view: View) {
        addSymbol(view, "×")
    }

    fun piButton(view: View) {
        updateDisplay(view, "π")
    }

    fun leftParenthesisButton(view: View) {
        updateDisplay(view, "(")
    }

    fun rightParenthesisButton(view: View) {
        updateDisplay(view, ")")
    }

    fun backspaceButton(view: View) {
        binding.input.requestFocus()
        var cursorPosition = binding.input.selectionStart
        val textLength = binding.input.text.length
        var newValue = ""
        var isFunction = false
        var isDecimal = false
        var functionLength = 0

        if (isEqualLastAction) {
            cursorPosition = textLength
        }

        if (cursorPosition != 0 && textLength != 0) {
            // Check if it is a function to delete
            val functionsList =
                listOf("cos⁻¹(", "sin⁻¹(", "tan⁻¹(", "cos(", "sin(", "tan(", "ln(", "log(", "exp(")
            for (function in functionsList) {
                val leftPart = binding.input.text.subSequence(0, cursorPosition).toString()
                if (leftPart.endsWith(function)) {
                    newValue = binding.input.text.subSequence(0, cursorPosition - function.length)
                        .toString() +
                            binding.input.text.subSequence(cursorPosition, textLength).toString()
                    isFunction = true
                    functionLength = function.length - 1
                    break
                }
            }
            // Else
            if (!isFunction) {
                // remove the grouping separator
                val leftPart = binding.input.text.subSequence(0, cursorPosition).toString()
                val leftPartWithoutSpaces = leftPart.replace(groupingSeparatorSymbol, "")
                functionLength = leftPart.length - leftPartWithoutSpaces.length

                newValue = leftPartWithoutSpaces.subSequence(0, leftPartWithoutSpaces.length - 1)
                    .toString() +
                        binding.input.text.subSequence(cursorPosition, textLength).toString()

                isDecimal = binding.input.text[cursorPosition - 1] == decimalSeparatorSymbol[0]
            }

            // Handle decimal deletion as a special case when finding cursor position
            var rightSideCommas = 0
            if (isDecimal) {
                val oldString = binding.input.text
                var immediateRightDigits = 0
                var index = cursorPosition
                // Find number of digits that were previously to the right of the decimal
                while (index < textLength && oldString[index].isDigit()) {
                    index++
                    immediateRightDigits++
                }
                // Determine how many thousands separators that gives us to our right
                if (immediateRightDigits > 3)
                    rightSideCommas = immediateRightDigits / 3
            }

            val newValueFormatted =
                NumberFormatter.format(newValue, decimalSeparatorSymbol, groupingSeparatorSymbol)
            var cursorOffset = newValueFormatted.length - newValue.length - rightSideCommas
            if (cursorOffset < 0) cursorOffset = 0

            binding.input.setText(newValueFormatted)
            binding.input.setSelection((cursorPosition - 1 + cursorOffset - functionLength).takeIf { it > 0 }
                ?: 0)
        }
    }

    private fun moveCursor(direction: Int) {
        val currentPosition = binding.input.selectionStart
        val textLength = binding.input.text.length

        val newPosition = (currentPosition + direction).coerceIn(0, textLength)
        binding.input.setSelection(newPosition)
    }

    private fun showCursorAtStart() {
        // Đặt con trỏ ở đầu văn bản
        binding.input.setSelection(0)
        // Hiển thị con trỏ
        binding.input.requestFocus()
    }
}