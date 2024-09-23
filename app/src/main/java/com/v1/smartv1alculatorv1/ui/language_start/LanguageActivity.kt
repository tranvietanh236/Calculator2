package com.v1.smartv1alculatorv1.ui.language_start

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.calculator.customformula.utils.SystemUtil
import com.v1.smartv1alculatorv1.Model.LanguageModel
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivityLanguageBinding
import com.v1.smartv1alculatorv1.ui.intro.IntroActivity
import java.util.Locale

class LanguageActivity : BaseActivity<ActivityLanguageBinding, LanguageViewModel>() {
    override fun createBinding() = ActivityLanguageBinding.inflate(layoutInflater)

    override fun setViewModel() = LanguageViewModel()

    private var adapter: LanguageAdapter? = null
    override fun initView() {
        super.initView()
        restoreLocale()
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        viewModel = LanguageViewModel()

        adapter = LanguageAdapter(this, emptyList()) { language ->
            viewModel.setSelectedLanguage(this, language)
        }
        binding.rcvFrag.layoutManager = LinearLayoutManager(this)
        binding.rcvFrag.adapter = adapter
        viewModel.languages.observe(this) { languages ->
            adapter?.updateData(languages)
        }

        viewModel.selectedLanguage.observe(this) { selectedLanguage ->
            adapter?.setSelectedLanguage(selectedLanguage)
            updateSaveButtonVisibility(selectedLanguage)
        }

        viewModel.languageStart(this)


        binding.imgSave.setOnClickListener{
            val selectedLanguage = viewModel.selectedLanguage.value
            if (selectedLanguage != null) {
                viewModel.setLocale(this, selectedLanguage.code)
                SystemUtil.saveLocale(this,selectedLanguage.code)

                startActivity(Intent(this, IntroActivity::class.java))
                finish()

            } else {
                Toast.makeText(
                    this,
                    "aaaa",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }


    private fun restoreLocale() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("selected_language", "en")
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun updateSaveButtonVisibility(selectedLanguage: LanguageModel?) {
        binding.imgSave.visibility = if (selectedLanguage != null) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }


}