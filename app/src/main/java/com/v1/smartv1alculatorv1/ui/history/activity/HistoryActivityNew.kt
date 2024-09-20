package com.v1.smartv1alculatorv1.ui.history.activity

import androidx.core.content.ContextCompat
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityHistoryNewBinding
import com.v1.smartv1alculatorv1.ui.history.HisScanFragment
import com.v1.smartv1alculatorv1.ui.history.HisTutorFragment

class HistoryActivityNew : BaseActivity<ActivityHistoryNewBinding, BaseViewModel>() {
    override fun createBinding(): ActivityHistoryNewBinding {
        return ActivityHistoryNewBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_his_fragment, HisScanFragment())
            .commit()

        binding.hisScan.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_his_fragment, HisScanFragment())
                .commit()
            binding.hisScan.setBackgroundResource(R.drawable.bg_border_8_history_on)
            binding.hisScan.setTextColor(ContextCompat.getColor(this, R.color.txt_his))
            binding.hisTutor.setBackgroundResource(R.drawable.bg_border_8_history)
            binding.hisTutor.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        binding.hisTutor.setOnClickListener {
            binding.hisTutor.setBackgroundResource(R.drawable.bg_border_8_history_on)
            binding.hisTutor.setTextColor(ContextCompat.getColor(this, R.color.txt_his))
            binding.hisScan.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.hisScan.setBackgroundResource(R.drawable.bg_border_8_history)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container_his_fragment, HisTutorFragment())
                .commit()


        }

        binding.icBackHis.setOnClickListener {
            finish()
        }
    }
}