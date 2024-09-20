package com.v1.smartv1alculatorv1.ui.history.activity

import android.util.Log
import androidx.core.content.ContextCompat
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityDetailHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailHistoryActivity : BaseActivity<ActivityDetailHistoryBinding, BaseViewModel>() {
    override fun createBinding(): ActivityDetailHistoryBinding {
        return ActivityDetailHistoryBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.ic_converter_color)
        val answer = intent.getStringExtra("answer")
        Log.d("TAG123", "Detail: $answer")
        val answerBot = intent.getStringExtra("answerBot")
        Log.d("TAG123", "Detail: $answerBot")
        val time = intent.getStringExtra("time")
        Log.d("TAG123", "Detail: $time")

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.textMessageUser.text = answer
        binding.txtBot.text = answerBot
        binding.textTimestampBot.text = formatTimestamp(time.toString())
        binding.textTimestampUser.text = formatTimestamp(time.toString())
    }

    private fun formatTimestamp(timestamp: String): String {
        val time = timestamp.toLong()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(time))
    }
}
