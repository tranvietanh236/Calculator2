package com.v1.smartv1alculatorv1.ui.smartcalculator

import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityGuideBinding

class GuideActivity : BaseActivity<ActivityGuideBinding, BaseViewModel>() {
    override fun createBinding() = ActivityGuideBinding.inflate(layoutInflater)

    override fun setViewModel() = BaseViewModel()

    override fun initView() {
        super.initView()

        binding.closeGuide.setOnClickListener {
            finish()
        }
    }


}