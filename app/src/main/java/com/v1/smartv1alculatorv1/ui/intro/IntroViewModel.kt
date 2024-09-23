package com.v1.smartv1alculatorv1.ui.intro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.v1.smartv1alculatorv1.Model.IntroModel
import com.v1.smartv1alculatorv1.R

class IntroViewModel : ViewModel() {
    private val intro = MutableLiveData<List<IntroModel>>()
    val listIntro: LiveData<List<IntroModel>> = intro

    fun getData() {
        val list = listOf(
            IntroModel(R.drawable.img_intro_1),
            IntroModel(R.drawable.img_intro),
            IntroModel(R.drawable.img_intro_3),
            IntroModel(R.drawable.img_intro_4)
        )

        intro.value = list

    }
}