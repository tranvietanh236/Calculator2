package com.v1.smartv1alculatorv1.ui.setting

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.calculator.customformula.utils.SystemUtil
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivityLanguageSettingBinding
import com.v1.smartv1alculatorv1.ui.language.LanguageAdapter
import com.v1.smartv1alculatorv1.ui.language.LanguageViewModel

class LanguageSettingActivity : BaseActivity<ActivityLanguageSettingBinding, LanguageViewModel>(){
    override fun createBinding() = ActivityLanguageSettingBinding.inflate(layoutInflater)

    override fun setViewModel() = LanguageViewModel()

    private var adapter: LanguageAdapter? = null
    override fun initView() {
        super.initView()

        viewModel = LanguageViewModel()

        adapter = LanguageAdapter(this, emptyList()) { language ->
            viewModel.setSelectedLanguage(this, language)
        }

        binding.rcvLanguageStart.layoutManager = LinearLayoutManager(this)
        binding.rcvLanguageStart.adapter = adapter
        viewModel.languages.observe(this) { languages ->
            adapter?.updateData(languages)
        }


        viewModel.selectedLanguage.observe(this) { selectedLanguage ->
            adapter?.setSelectedLanguage(selectedLanguage)
        }
        // Initialize data
        viewModel.languageSetting(this)


        binding.imgSave.setOnClickListener {
            val selectedLanguage = viewModel.selectedLanguage.value
            if (selectedLanguage != null) {
                viewModel.setLocale(this, selectedLanguage.code)
                SystemUtil.saveLocale(this, selectedLanguage.code)
            }
            finish()
        }

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}