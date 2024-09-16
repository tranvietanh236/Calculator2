package com.v1.smartv1alculatorv1.untils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SharePrefUtils {
    private var mSharePref: SharedPreferences? = null

    fun init(context: Context?) {
        if (mSharePref == null) {
            mSharePref = PreferenceManager.getDefaultSharedPreferences(context)
        }
    }

    fun isRated(context: Context): Boolean {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return pre.getBoolean("rated", false)
    }


    fun forceRated(context: Context) {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putBoolean("rated", true)
        editor.commit()
    }

    fun isAppClosed (context: Context): Boolean {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return pre.getBoolean("isAppClosed", false)
    }


    fun foreAppClosed (context: Context, boolean: Boolean) {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putBoolean("isAppClosed", boolean)
        editor.commit()
    }


    fun isSelectModeOpen (context: Context): Boolean {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return pre.getBoolean("SelectModeOpen", false)
    }


    fun foreSelectModeOpen (context: Context, boolean: Boolean) {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = pre.edit()
        editor.putBoolean("SelectModeOpen", boolean)
        editor.commit()
    }

    fun getCounteEject(context: Context): Int {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return pre.getInt("CounteEject", 0)
    }

    fun setCounterEject(context: Context) {
        val pre = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val currentCounter = pre.getInt("CounteEject", 0)
        pre.edit().putInt("CounteEject", currentCounter + 1).apply()
    }



}