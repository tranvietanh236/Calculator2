package com.v1.smartv1alculatorv1.ui.chat_ai.Activity


import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivityChatBinding
import com.v1.smartv1alculatorv1.ui.chat_ai.Adapter.MainChatAdapter
import com.v1.smartv1alculatorv1.ui.chat_ai.ViewModel.ChatActiviyViewModel

class ChatActivity : BaseActivity<ActivityChatBinding, ChatActiviyViewModel>() {
    companion object {
        var bmiValue = 0f

    }

    override fun createBinding(): ActivityChatBinding {
        return ActivityChatBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): ChatActiviyViewModel {
        return ChatActiviyViewModel()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        super.initView()
        bmiValue = intent.getFloatExtra("BMI", 0f)


        val mainAdapter = MainChatAdapter(this)
        binding.viewPager.adapter = mainAdapter
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = 3

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {

                    0 -> {
                        binding.tvTitle.text = getString(R.string.ai_assistant)
                        binding.ivHistory.visibility = View.VISIBLE
                    }

                    1 -> {
                        binding.tvTitle.text = getString(R.string.history)
                        binding.ivHistory.visibility = View.GONE
                        binding.root.clearFocus()
                        hideKeyboard()
                    }

                }
            }
        })


        binding.ivHistory.setOnClickListener {
            binding.viewPager.currentItem = 1
        }
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboardBgr()
            }
            true
        }
        binding.ivBack.setOnClickListener {
            if (binding.viewPager.currentItem == 0) {
                finish()
            } else {
                binding.viewPager.currentItem = 0
            }
        }

        binding.ivHome.setOnClickListener {
            //startActivity(Intent(this, HealthyCheck::class.java))
            finish()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.viewPager.currentItem == 0) {
                    finish()
                } else {
                    binding.viewPager.currentItem = 0
                }
            }
        })

    }


    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        window.decorView.rootView?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }


    private fun hideKeyboardBgr() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus
        view?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        binding.root.clearFocus()
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

}