package com.v1.smartv1alculatorv1.ui.history

import android.os.Build
import androidx.core.content.ContextCompat
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityHistoryNewBinding
import com.v1.smartv1alculatorv1.databinding.FragmentHisTutorBinding
import com.v1.smartv1alculatorv1.ui.chat_ai.Fragment.HistoryFragment

class HistoryActivityNew : BaseActivity<ActivityHistoryNewBinding, BaseViewModel>() {
    override fun createBinding(): ActivityHistoryNewBinding {
        return ActivityHistoryNewBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_his_fragment, HisScanFragment())
            .commit()

        binding.hisScan.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_his_fragment, HisScanFragment())
                .commit()
        }
        binding.hisTutor.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_his_fragment, HistoryFragment())
                .commit()
        }
    }
}