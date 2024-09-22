package com.v1.smartv1alculatorv1.ui.permission

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityPermissionBinding
import com.v1.smartv1alculatorv1.ui.home.HomeActivity
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import com.v1.smartv1alculatorv1.ui.smartcalculator.SmartCalculatorActivity


class PermissionActivity : BaseActivity<ActivityPermissionBinding, BaseViewModel>(){
    override fun createBinding() = ActivityPermissionBinding.inflate(layoutInflater)
    override fun setViewModel() = BaseViewModel()


    // Khai báo ActivityResultLauncher ở cấp lớp
    private lateinit var storagePermissionLauncher: ActivityResultLauncher<String>
    // Khai báo ActivityResultLauncher cho quyền Camera
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>

    override fun initView() {
        super.initView()


        // Đăng ký ActivityResultLauncher trong initView hoặc onCreate
        storagePermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding.imgSwUnSelected1.visibility = ImageView.GONE
                binding.imgSwSelected1.visibility = ImageView.VISIBLE
                checkPermissionsStatus()
            } else {
                //Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        // Đăng ký ActivityResultLauncher cho quyền Camera
        cameraPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding.imgSwUnSelected2.visibility = ImageView.GONE
                binding.imgSwSelected2.visibility = ImageView.VISIBLE
                checkPermissionsStatus()
            }
        }


        // Bắt sự kiện nhấn vào quyền Storage Access
        binding.imgSwUnSelected1.setOnClickListener {
            requestStoragePermission()
        }

        // Bắt sự kiện nhấn vào quyền Screen Overlay
        binding.imgSwUnSelected2.setOnClickListener {
//            requestOverlayPermission()
            requestCameraPermission()
        }

        // Bắt sự kiện nhấn vào quyền Accessibility Service
        binding.imgSwUnSelected3.setOnClickListener {
            requestAccessibilityService()

        }

        binding.imgClick.visibility = ImageView.GONE
        binding.imgClick.setOnClickListener {
            checkAndRequestPermissions()
            checkAllPermissionsAndProceed()
        }


    }

    // Kiểm tra quyền Storage Access đã được cấp chưa
    private fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Các thiết bị Android thấp tự động cấp quyền
            true
        } else {
            // Kiểm tra quyền với các phiên bản Android cao hơn
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Kiểm tra quyền Camera đã được cấp chưa
    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Yêu cầu quyền Camera
    private fun requestCameraPermission() {
        binding.imgSwUnSelected2.visibility = ImageView.GONE
        binding.imgSwSelected2.visibility = ImageView.VISIBLE
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }


    // Kiểm tra quyền Overlay đã được cấp chưa
    private fun isOverlayPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // Các thiết bị Android thấp tự động cấp quyền
            true
        } else {
            // Kiểm tra quyền với các phiên bản Android cao hơn
            Settings.canDrawOverlays(this)
        }
    }


    private fun checkAndRequestPermissions() {
        val isStorageGranted = isStoragePermissionGranted()
        val isCameraGranted = isCameraPermissionGranted()
        val isOverlayGranted = isOverlayPermissionGranted()

        if (isStorageGranted && isCameraGranted && isOverlayGranted) {
            binding.imgClick.visibility = ImageView.VISIBLE
        } else {
            binding.imgClick.visibility = ImageView.VISIBLE
            // Hiển thị thông báo hoặc yêu cầu cấp quyền thủ công
            //Toast.makeText(this, "Please grant all necessary permissions", Toast.LENGTH_SHORT).show()
        }
    }

    // Yêu cầu quyền Storage
    private fun requestStoragePermission() {
        binding.imgSwUnSelected1.visibility = ImageView.GONE
        binding.imgSwSelected1.visibility = ImageView.VISIBLE
        storagePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }



    // Yêu cầu quyền Accessibility Service
    private fun requestAccessibilityService() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)

        checkPermissionsStatus()
        binding.imgSwUnSelected3.visibility = ImageView.GONE
        binding.imgSwSelected3.visibility = ImageView.VISIBLE
    }


    // Kiểm tra tất cả các quyền đã cấp chưa và hiển thị imgClick nếu đã cấp đủ quyền
    private fun checkPermissionsStatus() {
        val isCameraGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED


        if (isCameraGranted) {
            binding.imgClick.visibility = ImageView.VISIBLE // Hiển thị imgClick nếu tất cả quyền đã cấp
        }
    }




    // Khi người dùng nhấn vào imgClick, kiểm tra tất cả quyền và chuyển sang màn hình Home nếu đủ quyền
    private fun checkAllPermissionsAndProceed() {
        val isCameraGranted = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

        if (isCameraGranted) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            //Toast.makeText(this, "Please grant all permissions", Toast.LENGTH_SHORT).show()
        }
    }



}