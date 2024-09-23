package com.v1.smartv1alculatorv1.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityPermissionBinding
import com.v1.smartv1alculatorv1.ui.home.HomeActivity

class PermissionActivity : BaseActivity<ActivityPermissionBinding, BaseViewModel>(){
    override fun createBinding() = ActivityPermissionBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()


    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val PREFS_NAME = "app_prefs"
    private val KEY_PERMISSIONS_GRANTED = "permissions_granted"


    override fun initView() {
        super.initView()
        showStatusBar(this)
        if (checkCameraPermission()) {
            showCameraGrantedState() // Hiển thị trạng thái đã cấp quyền
        } else {
            showCameraNotGrantedState() // Hiển thị trạng thái chưa cấp quyền
        }

        // Sự kiện nhấn vào imgSwUnSelected1 để yêu cầu quyền Camera
        binding.imgSwUnSelected1.setOnClickListener {
            if (!checkCameraPermission()) {
                requestCameraPermission()
            }
        }

        // Kiểm tra tất cả quyền khi nhấn imgClick
        binding.imgClick.visibility = ImageView.GONE
        binding.imgClick.setOnClickListener {
            checkAllPermissionsAndProceed()
        }


    }

    fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }



    // Yêu cầu quyền camera
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Giải thích lý do tại sao cần quyền nếu cần thiết
            AlertDialog.Builder(this)
                .setTitle("Yêu cầu quyền camera")
                .setMessage("Ứng dụng cần truy cập camera để chụp ảnh.")
                .setPositiveButton("Đồng ý") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_REQUEST_CODE
                    )

                }
                .setNegativeButton("Hủy", null)
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }


    // Gọi onPermissionsGranted() sau khi quyền được cấp
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                onPermissionsGranted() // Cấp quyền thành công, chuyển đến HomeActivity
            } else {
                // Quyền bị từ chối, có thể thông báo cho người dùng
                Log.d("PermissionActivity", "Quyền Camera bị từ chối.")
            }
        }
    }

    // Hiển thị trạng thái đã cấp quyền
    private fun showCameraGrantedState() {
        binding.imgSwUnSelected1.visibility = ImageView.GONE // Ẩn nút tắt
        binding.imgSwSelected1.visibility = ImageView.VISIBLE // Hiển thị nút bật
    }

    // Hiển thị trạng thái chưa cấp quyền
    private fun showCameraNotGrantedState() {
        binding.imgSwUnSelected1.visibility = ImageView.VISIBLE // Hiển thị nút tắt
        binding.imgSwSelected1.visibility = ImageView.GONE // Ẩn nút bật
    }


    // Sau khi quyền được cấp, hiển thị nút imgClick và lưu trạng thái quyền đã được cấp
    private fun onPermissionsGranted() {
        setPermissionsGranted(true) // Lưu trạng thái đã cấp quyền
        binding.imgClick.visibility = ImageView.VISIBLE // Hiển thị nút imgClick
        showCameraGrantedState()
    }


    // Lưu trạng thái quyền
   fun setPermissionsGranted(granted: Boolean) {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_PERMISSIONS_GRANTED, granted)
        editor.apply()
    }


    // Kiểm tra tất cả quyền và chuyển sang màn hình Home nếu đủ quyền
    private fun checkAllPermissionsAndProceed() {
        val isCameraGranted = checkCameraPermission()

        if (isCameraGranted) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d("PermissionActivity", "Vui lòng cấp tất cả quyền.")
        }
    }

    // Kiểm tra trạng thái quyền
    private fun arePermissionsGranted(): Boolean {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_PERMISSIONS_GRANTED, false)
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