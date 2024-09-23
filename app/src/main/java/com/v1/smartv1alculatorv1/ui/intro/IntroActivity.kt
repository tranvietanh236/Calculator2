package com.v1.smartv1alculatorv1.ui.intro

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivityIntroBinding
import com.v1.smartv1alculatorv1.ui.home.HomeActivity
import com.v1.smartv1alculatorv1.ui.permission.PermissionActivity

class IntroActivity : BaseActivity<ActivityIntroBinding, IntroViewModel>() {
    override fun createBinding() = ActivityIntroBinding.inflate(layoutInflater)

    override fun setViewModel() = IntroViewModel()
    private var introAdapter: IntroAdapter? = null

    private val PREFS_NAME = "app_prefs"
    private val KEY_PERMISSIONS_GRANTED = "permissions_granted"

    override fun viewModel() {
        super.viewModel()


        viewModel.getData()
        viewModel.listIntro.observe(this) {
            introAdapter?.list = it
            introAdapter = IntroAdapter(this, it)
            binding.viewPagerIntro.offscreenPageLimit = 5
            binding.viewPagerIntro.adapter = introAdapter
            introAdapter?.notifyDataSetChanged()
        }
    }


    override fun initView() {
        super.initView()
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )


        binding.viewPagerIntro.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> {
                        binding.ads.visibility = View.GONE
                        binding.viewIntro.visibility = View.VISIBLE
                        binding.tvTitle.text = getString(R.string.title_intro_1)
                        binding.tvContent.text = getString(R.string.content_intro_1)
                        dotDefault()
                        binding.dot1.setImageResource(R.drawable.dot_select)
                        // Đổi icon về vectoc_right nếu không phải trang cuối
                        binding.tvNext.setImageResource(R.drawable.vectoc_right)
                    }
                    1 -> {
                        binding.ads.visibility = View.GONE
                        binding.viewIntro.visibility = View.VISIBLE
                        binding.tvTitle.text = getString(R.string.title_intro_2)
                        binding.tvContent.text = getString(R.string.content_intro_2)
                        dotDefault()
                        binding.dot2.setImageResource(R.drawable.dot_select)
                        // Đổi icon về vectoc_right nếu không phải trang cuối
                        binding.tvNext.setImageResource(R.drawable.vectoc_right)
                    }
                    2 -> {
                        binding.ads.visibility = View.GONE
                        binding.viewIntro.visibility = View.VISIBLE
                        binding.tvTitle.text = getString(R.string.title_intro_3)
                        binding.tvContent.text = getString(R.string.content_intro_3)
                        dotDefault()
                        binding.dot3.setImageResource(R.drawable.dot_select)
                        // Đổi icon về vectoc_right nếu không phải trang cuối
                        binding.tvNext.setImageResource(R.drawable.vectoc_right)
                    }
                    3 -> {
                        binding.ads.visibility = View.GONE
                        binding.viewIntro.visibility = View.VISIBLE
                        binding.tvTitle.text = getString(R.string.title_intro_4)
                        binding.tvContent.text = getString(R.string.content_intro_4)
                        dotDefault()
                        binding.dot4.setImageResource(R.drawable.dot_select)
                        // Đổi icon về vectoc_right nếu không phải trang cuối
                        binding.tvNext.setImageResource(R.drawable.ic_tich)
                    }
                }
            }
        })


        // Khi nhấn vào nút "Next"
        binding.tvNext.setOnClickListener {
            // Nếu là màn cuối cùng của intro
            if (binding.viewPagerIntro.currentItem >= 3) {
                // Kiểm tra quyền camera đã được cấp hay chưa
                if (arePermissionsGranted()) {
                    // Nếu quyền đã được cấp, chuyển đến HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Nếu chưa cấp quyền, chuyển đến PermissionActivity
                    val intent = Intent(this, PermissionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                // Nếu chưa phải màn cuối cùng, chuyển sang trang intro tiếp theo
                binding.viewPagerIntro.currentItem = binding.viewPagerIntro.currentItem + 1
            }
        }

    }
    private fun dotDefault() {
        binding.dot1.setImageResource(R.drawable.dot_not_select)
        binding.dot2.setImageResource(R.drawable.dot_not_select)
        binding.dot3.setImageResource(R.drawable.dot_not_select)
        binding.dot4.setImageResource(R.drawable.dot_not_select)

    }


    // Hàm kiểm tra xem quyền đã được cấp hay chưa
    private fun arePermissionsGranted(): Boolean {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_PERMISSIONS_GRANTED, false)
    }



}