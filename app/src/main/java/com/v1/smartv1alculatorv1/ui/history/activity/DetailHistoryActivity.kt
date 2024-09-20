package com.v1.smartv1alculatorv1.ui.history.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : BaseActivity<ActivityDetailHistoryBinding, BaseViewModel>() {
    override fun createBinding(): ActivityDetailHistoryBinding {
        return ActivityDetailHistoryBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        val answer = intent.getStringExtra("answer")
        Log.d("TAG123", "Detail: $answer")
        val time = intent.getStringExtra("answer")
        Log.d("TAG123", "Detail: $time")
        Log.d("TAG123", "Detail: $time")
    }
}
