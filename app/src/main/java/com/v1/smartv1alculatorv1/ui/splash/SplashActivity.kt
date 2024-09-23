package com.v1.smartv1alculatorv1.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivitySplashBinding
import com.v1.smartv1alculatorv1.ui.language_start.LanguageActivity

class SplashActivity : BaseActivity<ActivitySplashBinding, BaseViewModel>(){
    companion object {
        var isClearn = true
    }
    override fun createBinding() = ActivitySplashBinding.inflate(layoutInflater)

    override fun setViewModel() = BaseViewModel()


    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.ic_converter_color)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LanguageActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }



}