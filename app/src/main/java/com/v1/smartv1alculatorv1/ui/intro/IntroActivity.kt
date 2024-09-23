package com.v1.smartv1alculatorv1.ui.intro

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivityIntroBinding
import com.v1.smartv1alculatorv1.permission.PermissionActivity
import com.v1.smartv1alculatorv1.ui.home.HomeActivity

class IntroActivity : BaseActivity<ActivityIntroBinding, IntroViewModel>() {
    override fun createBinding() = ActivityIntroBinding.inflate(layoutInflater)

    override fun setViewModel() = IntroViewModel()
    private var introAdapter: IntroAdapter? = null


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
        showStatusBar(this)
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


        binding.tvNext.setOnClickListener() {
            if (binding.viewPagerIntro.currentItem >= 3) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    startActivity(Intent(this, PermissionActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }

            } else {
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
    private fun arePermissionsGranted(): Boolean {

        val requiredPermissions = arrayOf(
            android.Manifest.permission.POST_NOTIFICATIONS,

            )

        for (permission in requiredPermissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }


    private fun showStatusBar(activity: Activity?) {
        try {
            if (activity == null) return
            val window: Window = activity.window ?: return

            // Clear the fullscreen flags
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

            // Reset system UI visibility flags to default
            window.decorView.systemUiVisibility = (

                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            // mau status bar
            //window.statusBarColor = ContextCompat.getColor(activity, R.color.transparent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowInsetsController = window.insetsController
                window.statusBarColor = Color.TRANSPARENT // Làm cho thanh trạng thái trong suốt
            } else {
                window.statusBarColor = Color.TRANSPARENT // Chỉ định màu trong suốt cho các phiên bản Android thấp hơn
            }
            // Adjust window attributes to reset display cutout mode if necessary
            val lp = window.attributes
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            }
            window.attributes = lp
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}