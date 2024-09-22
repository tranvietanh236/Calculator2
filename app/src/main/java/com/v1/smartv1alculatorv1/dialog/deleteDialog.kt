package com.v1.smartv1alculatorv1.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.TextView
import com.v1.smartv1alculatorv1.R

class deleteDialog constructor(mContext: Context) : Dialog(mContext, R.style.CustomDialogTheme)  {

    private var onPressListener: OnClickDialogPaListener? = null
    private val tv_cancel_fl: TextView
    private val tv_ok_fl: TextView
    private val context: Context


    init {
        this.context = mContext
        setContentView(R.layout.item_delete_dialog)
        val attributes = window!!.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = attributes
        window!!.setSoftInputMode(16)

        tv_cancel_fl = findViewById(R.id.tv_cancel_his)
        tv_ok_fl = findViewById(R.id.tv_delete_his)


        onClick()
    }
    fun init(context: Context?, onPress: OnClickDialogPaListener?) {
        this.onPressListener = onPress
    }



    private fun onClick(){
        tv_cancel_fl.setOnClickListener {
            onPressListener!!.cancel()
        }
        tv_ok_fl.setOnClickListener {
            onPressListener!!.ok()
        }
    }
    interface OnClickDialogPaListener{
        fun cancel()
        fun ok()
    }
}