package com.v1.smartv1alculatorv1.ui.setting

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.untils.tap


class ThankDialog @SuppressLint("NonConstantResourceId") constructor(context2: Context) :
    Dialog(context2, R.style.BaseDialog) {

    private var onPress: OnPress? = null
    private val tvTitle: TextView
    private val tvContent: TextView
    private val context: Context
    private val btnGot: AppCompatButton


    init {

        this.context = context2
        setContentView(R.layout.thank_you_dialog)

        val attributes = window!!.attributes
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.width = (context.resources.displayMetrics.widthPixels * 0.9).toInt()
        window!!.attributes = attributes
        window!!.setSoftInputMode(16)

        tvTitle = findViewById<TextView>(R.id.tv_title_ty)
        tvContent = findViewById<TextView>(R.id.tv_content_01)
        btnGot = findViewById<AppCompatButton>(R.id.btn_submit_got_it)


        btnGot.setOnClickListener {
            onPress?.gotIt()
            dismiss()
        }

    }


    interface OnPress {
        fun send(s: Int)

        fun rating(s: Int)

        fun cancel()

        fun later()

        fun gotIt()
    }

    fun init(context: Context?, onPress: OnPress?) {
        this.onPress = onPress
    }




}