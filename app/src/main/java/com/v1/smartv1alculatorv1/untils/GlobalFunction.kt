package com.calculator.customformula.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat

object GlobalFunction {

    var checkTranslationLanguage = false

    fun startActivity(context: Context?, clz: Class<*>?) {
        val intent = Intent(context, clz)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(intent)
    }

    fun startActivity(context: Context?, clz: Class<*>?, bundle: Bundle?) {
        val intent = Intent(context, clz)
        intent.putExtras(bundle!!)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        context!!.startActivity(intent)
    }

    fun showToastMessage(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun hideSoftKeyboard(activity: Activity) {
        try {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        } catch (ex: NullPointerException) {
            ex.printStackTrace()
        }
    }

    fun reloadActivity(context: Context) {
        (context as Activity).finish()
        ContextCompat.startActivity(context, context.intent, null)
    }
}