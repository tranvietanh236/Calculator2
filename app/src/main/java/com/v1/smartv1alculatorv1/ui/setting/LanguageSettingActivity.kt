package com.v1.smartv1alculatorv1.ui.setting

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.calculator.customformula.utils.SystemUtil
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivityLanguageSettingBinding
import com.v1.smartv1alculatorv1.ui.language_start.LanguageAdapter
import com.v1.smartv1alculatorv1.ui.language_start.LanguageViewModel

class LanguageSettingActivity : BaseActivity<ActivityLanguageSettingBinding, LanguageViewModel>(){
    override fun createBinding() = ActivityLanguageSettingBinding.inflate(layoutInflater)

    override fun setViewModel() = LanguageViewModel()

    private var adapter: LanguageAdapter? = null
    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
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