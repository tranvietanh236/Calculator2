package com.v1.smartv1alculatorv1.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.calculator.customformula.utils.SystemUtil
import com.v1.smartv1alculatorv1.R


abstract class BaseActivity<VB : ViewBinding, V : ViewModel> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: V

    abstract fun createBinding(): VB
    abstract fun setViewModel(): V

    protected open fun initView() {}
    protected open fun bindView() {}
    open fun viewModel() {}
    open fun initData() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtil.setLocale(this)
        binding = createBinding()
        setContentView(binding.root)
        viewModel = setViewModel()

        initView()
        bindView()
        viewModel()

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Sử dụng WindowInsetsController cho Android 11 (API 30) trở lên
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            // Sử dụng View flags cho các phiên bản Android cũ hơn
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase == null) {
            super.attachBaseContext(null)
        }
        newBase?.let {
            val newContext = SystemUtil.setLocale(it)
            super.attachBaseContext(newContext)
        }
    }

    override fun onStart() {
        super.onStart()
        showStatusBar(this)
    }


    override fun hasWindowFocus(): Boolean {
        showStatusBar(this)
        return super.hasWindowFocus()
    }

    private fun hideStatusBar(activity: Activity?) {
        try {
            if (activity == null) return
            val window: Window = activity.window ?: return
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            // Clear the fullscreen flags
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
            val lp = window.attributes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
            window.attributes = lp
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun showStatusBar(activity: Activity?) {
        try {
            if (activity == null) return
            val window: Window = activity.window ?: return

            // Clear the fullscreen flags
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

            // Reset system UI visibility flags to default
            window.decorView.systemUiVisibility = (

                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            // mau status bar
            //window.statusBarColor = ContextCompat.getColor(activity, R.color.transparent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowInsetsController = window.insetsController
                window.statusBarColor = Color.TRANSPARENT // Làm cho thanh trạng thái trong suốt
            } else {
                window.statusBarColor = Color.TRANSPARENT // Chỉ định màu trong suốt cho các phiên bản Android thấp hơn
            }
            // Adjust window attributes to reset display cutout mode if necessary
            val lp = window.attributes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            }
            window.attributes = lp
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}