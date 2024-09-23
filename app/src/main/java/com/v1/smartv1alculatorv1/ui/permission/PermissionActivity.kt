package com.v1.smartv1alculatorv1.ui.permission

import android.Manifest
import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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


//        // Kiểm tra nếu quyền đã được cấp, chuyển trực tiếp tới HomeActivity
//        if (arePermissionsGranted()) {
//            val intent = Intent(this, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
//            return
//        }

        // Kiểm tra nếu quyền đã được cấp
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


    // Kiểm tra quyền camera
    private fun checkCameraPermission(): Boolean {
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
    private fun setPermissionsGranted(granted: Boolean) {
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

}