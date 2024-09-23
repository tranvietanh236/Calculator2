package com.v1.smartv1alculatorv1.ui.home

import androidx.core.content.ContextCompat
import com.calculator.customformula.utils.GlobalFunction
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivityHomeBinding
import com.v1.smartv1alculatorv1.ui.Unit_converter.HomeUnitConverterActivity
import com.v1.smartv1alculatorv1.ui.chat_ai.Activity.ChatActivity
import com.v1.smartv1alculatorv1.ui.history.activity.HistoryActivityNew
import com.v1.smartv1alculatorv1.ui.scan_to_slove.ScanActivityNew
import com.v1.smartv1alculatorv1.ui.setting.SettingActivity
import com.v1.smartv1alculatorv1.ui.smartcalculator.SmartCalculatorActivity

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {
    override fun createBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): HomeViewModel {
        return HomeViewModel()
    }

    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding.ConverterUnit.setOnClickListener {
            GlobalFunction.startActivity(this@HomeActivity, HomeUnitConverterActivity::class.java)
        }

        binding.icHistory.setOnClickListener {
            GlobalFunction.startActivity(this@HomeActivity, HistoryActivityNew::class.java)
        }
        binding.ScanToSolve.setOnClickListener {
            GlobalFunction.startActivity(this@HomeActivity, ScanActivityNew::class.java)
        }
        binding.AiTutor.setOnClickListener {
            GlobalFunction.startActivity(this@HomeActivity, ChatActivity::class.java)
        }
        binding.icSetting.setOnClickListener {
            GlobalFunction.startActivity(this@HomeActivity, SettingActivity::class.java)
        }
        binding.SmartCalculator.setOnClickListener {
            GlobalFunction.startActivity(this@HomeActivity, SmartCalculatorActivity::class.java)
        }
    }



}