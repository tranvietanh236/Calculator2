package com.v1.smartv1alculatorv1.ui.setting

import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding, BaseViewModel>() {
    override fun createBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }
}