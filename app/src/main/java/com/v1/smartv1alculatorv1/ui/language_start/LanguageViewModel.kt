package com.v1.smartv1alculatorv1.ui.language_start

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.calculator.customformula.utils.SystemUtil
import com.v1.smartv1alculatorv1.Model.LanguageModel
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ItemLanguagescreenBinding
import java.util.Locale

class LanguageViewModel: BaseViewModel() {

    val languages = MutableLiveData<List<LanguageModel>>()
    val selectedLanguage = MutableLiveData<LanguageModel>()

    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private val _langDevice = MutableLiveData<String>()
    val langDevice: LiveData<String>
        get() = _langDevice

    private val _codeLang = MutableLiveData<String>()
    val codeLang: LiveData<String>
        get() = _codeLang


    fun languageStart(context: Context) {
        val deviceLanguageCode = SystemUtil.getDeviceLanguage(context)
        Log.d("bbb", deviceLanguageCode.toString())
        val listLanguage = mutableListOf(
            LanguageModel("Hindi", "hi", false, R.drawable.icon_hindi),
            LanguageModel("Spanish", "es", false, R.drawable.icon_spanish),
            LanguageModel("French", "fr", false, R.drawable.icon_french),
            LanguageModel("Portuguese", "pt", false, R.drawable.icon_portuguese),
            LanguageModel("Indonesian", "in", false, R.drawable.icon_indonesian),
            LanguageModel("German", "de", false, R.drawable.icon_german),
            LanguageModel("Japan", "ja", false, R.drawable.icon_japan),
            LanguageModel("English", "en", false, R.drawable.icon_english),
        )

        Log.d("bbb", deviceLanguageCode.toString())
        val deviceLanguageIndex = listLanguage.indexOfFirst { it.code == deviceLanguageCode }
        Log.d("aaa", deviceLanguageCode.toString())
        if (deviceLanguageIndex != -1) {
            val deviceLanguage = listLanguage.removeAt(deviceLanguageIndex)
            listLanguage.add(0, deviceLanguage)
        } else {
            val englishLanguageIndex = listLanguage.indexOfFirst { it.code == "en" }
            if (englishLanguageIndex != -1) {
                val englishLanguage = listLanguage.removeAt(englishLanguageIndex)
                listLanguage.add(0, englishLanguage)
            }
        }

        languages.postValue(listLanguage)

    }


    fun languageSetting(context: Context) {

        var langDevice = "en"
        var codeLang = "en"
        val listLanguage = mutableListOf<LanguageModel>()


        listLanguage.add(LanguageModel("Hindi", "hi", false, R.drawable.icon_hindi))
        listLanguage.add(LanguageModel("Spanish", "es", false, R.drawable.icon_spanish))
        listLanguage.add(LanguageModel("French", "fr", false, R.drawable.icon_french))
        listLanguage.add(LanguageModel("Portuguese", "pt", false, R.drawable.icon_portuguese))
        listLanguage.add(LanguageModel("Indonesian", "in", false, R.drawable.icon_indonesian))
        listLanguage.add(LanguageModel("German", "de", false, R.drawable.icon_german))
        listLanguage.add(LanguageModel("Japan", "ja", false, R.drawable.icon_japan))
        listLanguage.add(LanguageModel("English", "en", false, R.drawable.icon_english))

        val preferredLanguage = SystemUtil.getPreLanguage(context)
        val selectedLanguageIndex = listLanguage.indexOfFirst { it.code == preferredLanguage }
        if (selectedLanguageIndex != -1) {
            val selectedLanguage = listLanguage.removeAt(selectedLanguageIndex).copy(active = true)
            listLanguage.add(0, selectedLanguage)
        }
        langDevice = preferredLanguage.toString()
        codeLang = preferredLanguage.toString()
        listLanguage.forEachIndexed { index, language ->
            if (index != 0) {
                listLanguage[index] = language.copy(active = false)
            }
        }
        _langDevice.postValue(langDevice)
        _codeLang.postValue(codeLang)
        languages.postValue(listLanguage)
    }

    fun setSelectedLanguage(context: Context, language: LanguageModel) {
        selectedLanguage.value = language
        _codeLang.postValue(language.code)
        saveSelectedLanguage(context, language.code)
    }

    private fun saveSelectedLanguage(context: Context, languageCode: String) {
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("selected_language", languageCode).apply()
    }

}