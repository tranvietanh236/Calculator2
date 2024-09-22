package com.v1.smartv1alculatorv1.ui.scan_to_slove

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.inteface.OnClickToLengthBottomSheet

class AnswerFoundBottomSheet : BottomSheetDialogFragment() {
    private var listener: OnclickAskQuestionFound? = null
    interface OnclickAskQuestionFound{
        fun onClick()
    }

    fun setOnclickAskQuestionFound(listener: OnclickAskQuestionFound) {
        this.listener = listener
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.answer_found_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val askQuestion = view.findViewById<TextView>(R.id.btn_ask_question)
        askQuestion.setOnClickListener {
            listener?.onClick()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED //
    }
}