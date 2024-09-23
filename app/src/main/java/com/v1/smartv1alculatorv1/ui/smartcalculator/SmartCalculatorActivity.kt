package com.v1.smartv1alculatorv1.ui.smartcalculator

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.v1.smartv1alculatorv1.Database.ChatRepository
import com.v1.smartv1alculatorv1.Model.SmartModel
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivitySmartCalculatorBinding
import com.v1.smartv1alculatorv1.ui.home.HomeActivity
import com.v1.smartv1alculatorv1.untils.NumberFormatter
import com.v1.smartv1alculatorv1.untils.tap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormatSymbols


class SmartCalculatorActivity :
    BaseActivity<ActivitySmartCalculatorBinding, SmartCalculatorViewModel>() {

    private val decimalSeparatorSymbol =
        DecimalFormatSymbols.getInstance().decimalSeparator.toString()
    private val groupingSeparatorSymbol =
        DecimalFormatSymbols.getInstance().groupingSeparator.toString()

    private lateinit var historyAdapter: HistoryAdapter
    private var historyList: MutableList<SmartModel> = mutableListOf()
    private lateinit var chatRepository: ChatRepository

    private val REQUEST_CODE = 1 // Khai báo REQUEST_CODE

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

    private fun loaddata() {
        historyList = chatRepository.getAllChatsHisSmart()
        if (historyList.isNotEmpty()) {
            binding.constraintBanner1.visibility = View.VISIBLE
            Log.d("hir", "${historyList[0].calculation}")
        } else {
            Log.d("hir", "History list is empty.")
            binding.constraintBanner1.visibility = View.GONE
        }
        Log.d("ass",historyList.size.toString())

            binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
            historyAdapter = HistoryAdapter(historyList)
            binding.recyclerView.adapter = historyAdapter



    }


    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        super.initView()

        chatRepository = ChatRepository(this)

        // Ẩn clearButton khi EditText không có nội dung lúc khởi tạo
        val hasText = binding.input.text.isNotEmpty()
        binding.clearButton.visibility = if (hasText) View.VISIBLE else View.INVISIBLE

        binding.icBackSmartcal.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        binding.imgThongTin.setOnClickListener {
            val intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }


        binding.recyclerView.layoutManager = GridLayoutManager(this,3)// Số cột bạn muốn
        historyAdapter = HistoryAdapter(historyList)
        binding.recyclerView.adapter = historyAdapter


        loaddata()
        binding.nextCursorStart.setOnClickListener {
            // Lấy văn bản hiện tại từ EditText
            val currentText = binding.input.text.toString()

            // Thêm dấu '{' vào đầu và xuống dòng
            val newText = if (currentText.isNotEmpty()) {
                "{\n$currentText"
            } else {
                "{\n"
            }
            // Cập nhật văn bản mới vào EditText
            binding.input.setText(newText)
            // Đặt con trỏ ở cuối văn bản mới
            binding.input.post {
                binding.input.setSelection(newText.length) // Di chuyển con trỏ đến cuối
            }
        }
        binding.nextCursorRight.setOnClickListener{
            binding.input.requestFocus()
            moveCursor(1)
        }
        binding.nextCursorLeft.setOnClickListener{
            binding.input.requestFocus()
            moveCursor(-1)
        }

        binding.btnGiaTriTuyetDoi.setOnClickListener {
            insertAbsoluteValueSymbols(it)
        }

        binding.btnPhanSo.setOnClickListener {
            insertValuePhanSo(it)
        }
        binding.txtTry1.requestFocus()




        binding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Không cần xử lý trước khi thay đổi
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val hasText = s?.isNotEmpty() == true
                // Hiển thị hoặc ẩn drawable tùy thuộc vào nội dung của EditText
                binding.input.setCompoundDrawablesWithIntrinsicBounds(0, 0, if (hasText) R.drawable.custom_clean_x else 0, 0)
                // Hiển thị hoặc ẩn clearButton dựa trên nội dung
                binding.clearButton.visibility = if (hasText) View.VISIBLE else View.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                // Không cần xử lý sau khi thay đổi
            }
        })

        binding.clearButton.setOnClickListener {
            Toast.makeText(this, "Clear button clicked", Toast.LENGTH_SHORT).show()
            binding.input.text.clear()

        }


        binding.xBtn.tap {
            it?.let { view -> showPopupWindow(view) }
            true
        }

        binding.squareInv.tap {
            it?.let { view -> showPopupWindowMU(view) }
            true
        }

        binding.square.tap {
            it?.let { view -> showPopupWindowCanbac(view) }
            true
        }

        binding.input.setOnTouchListener { v, event ->
            v.performClick() // Đảm bảo rằng EditText vẫn nhận được sự kiện click
            v.requestFocus() // Yêu cầu EditText được focus
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0) // Ẩn bàn phím khi EditText được nhấn
            true
        }

        binding.imgHistoryDelete.setOnClickListener {

            deleteChatHisSmart()
//            historyList.clear()
//            historyAdapter.notifyDataSetChanged()

            binding.constraintBanner1.visibility = View.GONE
            binding.constraintBanner2.visibility = View.VISIBLE

        }


        binding.imgGui.setOnClickListener {
            val message = binding.input.text.toString()

            if (message.isNotEmpty()) {
                val messageList = SmartModel(message)
                chatRepository.insertChatHisSamrt(messageList)
                val intent = Intent(this@SmartCalculatorActivity, AnswerActivity::class.java)
                Log.d("choi",message)
                intent.putExtra("answer_rq", message)
                startActivityForResult(intent, REQUEST_CODE)

            } else {
                // Thông báo lỗi nếu người dùng chưa nhập gì
                Toast.makeText(this, "Vui lòng nhập phép tính", Toast.LENGTH_SHORT).show()
            }
        }

        val selectedBackground = ContextCompat.getDrawable(this, R.drawable.backgroud_border_doi_mau)
        val DatlaiBackground = ContextCompat.getDrawable(this, R.drawable.backgroud_border)
        val selected3Background = ContextCompat.getDrawable(this, R.drawable.custom_3_cham_do)
        val Datlai3Background = ContextCompat.getDrawable(this, R.drawable.custom_3_cham)

        binding.btnBa1.setOnClickListener {
            binding.tableLayout.visibility = View.GONE
            binding.tableLayout2.visibility = View.GONE
            binding.tableLayout3.visibility = View.VISIBLE

            binding.btnBa3.background = selected3Background
            binding.btnAbc1.background = DatlaiBackground
            binding.btnAbc2.background = DatlaiBackground
            binding.btnAbc3.background = DatlaiBackground

        }

        binding.btnBa2.setOnClickListener {
            binding.tableLayout.visibility = View.VISIBLE
            binding.tableLayout2.visibility = View.GONE
            binding.tableLayout3.visibility = View.GONE

            binding.btnBa1.background = selected3Background
            binding.btnAbc1.background = DatlaiBackground
            binding.btnAbc2.background = DatlaiBackground
            binding.btnAbc3.background = DatlaiBackground

        }

        binding.btnBa3.setOnClickListener {
            binding.tableLayout.visibility = View.VISIBLE
            binding.tableLayout2.visibility = View.GONE
            binding.tableLayout3.visibility = View.GONE

            binding.btnBa1.background = selected3Background
            binding.btnAbc1.background = DatlaiBackground
            binding.btnAbc2.background = DatlaiBackground
            binding.btnAbc3.background = DatlaiBackground
        }


        binding.btnAbc1.setOnClickListener {
            binding.tableLayout.visibility = View.GONE
            binding.tableLayout2.visibility = View.VISIBLE
            binding.tableLayout3.visibility = View.GONE


            binding.btnAbc2.background = selectedBackground
            binding.btnBa1.background = Datlai3Background
            binding.btnBa2.background = Datlai3Background
            binding.btnBa3.background = Datlai3Background

        }
        binding.btnAbc2.setOnClickListener {
            binding.tableLayout.visibility = View.VISIBLE
            binding.tableLayout2.visibility = View.GONE
            binding.tableLayout3.visibility = View.GONE

            binding.btnAbc1.background = selectedBackground
            binding.btnBa1.background = Datlai3Background
            binding.btnBa2.background = Datlai3Background
            binding.btnBa3.background = Datlai3Background

        }
        binding.btnAbc3.setOnClickListener {
            binding.tableLayout.visibility = View.VISIBLE
            binding.tableLayout2.visibility = View.GONE
            binding.tableLayout3.visibility = View.GONE

            binding.btnAbc1.background = selectedBackground
            binding.btnBa1.background = Datlai3Background
            binding.btnBa2.background = Datlai3Background
            binding.btnBa3.background = Datlai3Background

        }

    }


    private fun updateDisplay(view: View, value: String) {
        val valueNoSeparators = value.replace(groupingSeparatorSymbol, "")
        val isValueInt = valueNoSeparators.toIntOrNull() != null
        val isValueLetter = value.matches(Regex("[a-zA-Z]")) // Kiểm tra nếu giá trị là chữ cái

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

            var newValueFormatted = if(isValueInt) {
                NumberFormatter.format(newValue, decimalSeparatorSymbol, groupingSeparatorSymbol)
            }else{
                newValue // Nếu là chữ cái thì không cần định dạng
            }
            withContext(Dispatchers.Main) {
                // Avoid two decimalSeparator in the same number
                // when you click on the decimalSeparator button

                // Tránh thêm hai dấu thập phân
                if (value == decimalSeparatorSymbol && decimalSeparatorSymbol in binding.input.text.toString()) {
                    return@withContext
                }
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
                if (previousChar.matches("[+\\-÷×^/]".toRegex())) {
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
                else if (nextChar.matches("[+\\-÷×^%!/]".toRegex())
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

    fun bangButton(view: View) {
        addSymbol(view, "=")
    }
//    fun muButton(view: View) {
//        addSymbol(view, "^²")
//    }

    fun phansoButton(view: View) {
        addSymbol(view, "/")
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


    private fun showPopupWindow(anchorView: View) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_key_options_x_y, null)


        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val buttonBackground = ContextCompat.getDrawable(this, R.drawable.background_button_popupwindow)
        val selectedBackground = ContextCompat.getDrawable(this, R.drawable.background_popupwindow_doi_mau)

        val btnXPopup = popupView.findViewById<Button>(R.id.btn_x)
        val btnYPopup = popupView.findViewById<Button>(R.id.btn_y)

        btnXPopup.setOnClickListener {
            updateDisplay(binding.xBtn, "x")
            btnXPopup.background = selectedBackground
            btnYPopup.background = buttonBackground // Đặt lại màu nền btnY
            popupWindow.dismiss()
        }

        btnYPopup.setOnClickListener {
            updateDisplay(binding.xBtn, "y")
            btnYPopup.background = selectedBackground
            btnXPopup.background = buttonBackground // Đặt lại màu nền btnX
            popupWindow.dismiss()
        }
        // Tính toán vị trí để hiển thị popup trên đầu nút bấm
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)

        // Chuyển đổi 50dp sang pixel
        val offsetInDp = 80
        val offsetInPx = (offsetInDp * resources.displayMetrics.density).toInt()
        // Hiển thị popup phía trên đầu anchorView, lùi lên 50dp
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.height - offsetInPx)


    }



    private fun showPopupWindowMU(anchorView: View) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_key_options_so_mu, null)


        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val buttonBackground = ContextCompat.getDrawable(this, R.drawable.background_button_popupwindow)
        val selectedBackground = ContextCompat.getDrawable(this, R.drawable.background_popupwindow_doi_mau)

        val btnMu2Popup = popupView.findViewById<Button>(R.id.btn_power_2)
        val btnMu3Popup = popupView.findViewById<Button>(R.id.btn_power_3)

        btnMu2Popup.setOnClickListener {
            updateDisplay(binding.xBtn, "^²")
            btnMu2Popup.background = selectedBackground // Đổi màu nền btnX
            btnMu3Popup.background = buttonBackground // Đặt lại màu nền btnY
            popupWindow.dismiss()
        }

        btnMu3Popup.setOnClickListener {
            updateDisplay(binding.xBtn, "^³")
            btnMu3Popup.background = selectedBackground
            btnMu2Popup.background = buttonBackground
            popupWindow.dismiss()
        }


        // Tính toán vị trí để hiển thị popup trên đầu nút bấm
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)

        // Chuyển đổi 50dp sang pixel
        val offsetInDp = 80
        val offsetInPx = (offsetInDp * resources.displayMetrics.density).toInt()
        // Hiển thị popup phía trên đầu anchorView, lùi lên 50dp
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.height - offsetInPx)



    }



    private fun showPopupWindowCanbac(anchorView: View) {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_key_options_can_bac_2, null)


        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        val buttonBackground = ContextCompat.getDrawable(this, R.drawable.background_button_popupwindow)
        val selectedBackground = ContextCompat.getDrawable(this, R.drawable.background_popupwindow_doi_mau)

        val btnCan2Popup = popupView.findViewById<Button>(R.id.btn_canbac_2)
        val btnCan3Popup = popupView.findViewById<Button>(R.id.btn_canbac_3)

        btnCan2Popup.setOnClickListener {
            updateDisplay(binding.xBtn, "²√")
            btnCan2Popup.background = selectedBackground // Đổi màu nền btnX
            btnCan3Popup.background = buttonBackground // Đặt lại màu nền btnY
            popupWindow.dismiss()
        }

        btnCan3Popup.setOnClickListener {
            updateDisplay(binding.xBtn, "³√")
            btnCan3Popup.background = selectedBackground
            btnCan2Popup.background = buttonBackground
            popupWindow.dismiss()
        }


        // Tính toán vị trí để hiển thị popup trên đầu nút bấm
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)

        // Chuyển đổi 50dp sang pixel
        val offsetInDp = 80
        val offsetInPx = (offsetInDp * resources.displayMetrics.density).toInt()
        // Hiển thị popup phía trên đầu anchorView, lùi lên 50dp
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.height - offsetInPx)



    }



    fun insertAbsoluteValueSymbols(view: View) {
        val editText = findViewById<EditText>(R.id.input) // ID của EditText
        val cursorPosition = editText.selectionStart
        val text = editText.text.toString()

        // Chèn dấu giá trị tuyệt đối | | vào vị trí con trỏ
        val newText = text.substring(0, cursorPosition) + "|" + "" + "|" + text.substring(cursorPosition)
        editText.setText(newText)

        // Đặt con trỏ ở giữa hai dấu |
        editText.setSelection(cursorPosition + 1)
    }

    fun insertValuePhanSo(view: View) {
        val editText = findViewById<EditText>(R.id.input) // ID của EditText
        val cursorPosition = editText.selectionStart
        val text = editText.text.toString()

        // Chèn dấu giá trị tuyệt đối | | vào vị trí con trỏ
        val newText = text.substring(0, cursorPosition) + "( )" + "/" + "( )" + text.substring(cursorPosition)
        editText.setText(newText)

        // Đặt con trỏ ở giữa hai dấu |
        editText.setSelection(cursorPosition + 1)
    }


    // Hàm tạo TextView động và thêm vào txt_history
    private fun addMessageToHistory(message: String) {
//        historyList.add(message)
//        historyAdapter.notifyItemInserted(historyList.size - 1)
//        binding.recyclerView.scrollToPosition(historyList.size - 1)
//
//        // Hiển thị lại constraintBanner1 nếu có dữ liệu
//        if (historyList.isNotEmpty()) {
//            binding.constraintBanner1.visibility = View.VISIBLE
//            binding.constraintBanner2.visibility = View.VISIBLE // Cập nhật banner 2 nếu cần
//        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val returnedText = data?.getStringExtra("RETURN_TEXT")
            Log.d("choi","$returnedText")
            if (!returnedText.isNullOrEmpty()) {
                addMessageToHistory(returnedText)
            }
        }
    }



    private fun deleteChatHisSmart() {
        chatRepository.deleteAllChatsHisSmart() // Gọi hàm xóa từ repository
        loaddata() // Tải lại dữ liệu để cập nhật RecyclerView
    }

    override fun onResume() {
        super.onResume()
        loaddata()
    }

}