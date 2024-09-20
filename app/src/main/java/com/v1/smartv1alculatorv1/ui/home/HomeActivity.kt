package com.v1.smartv1alculatorv1.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.calculator.customformula.utils.GlobalFunction
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.databinding.ActivityHomeBinding
import com.v1.smartv1alculatorv1.ui.Unit_converter.HomeUnitConverterActivity
import com.v1.smartv1alculatorv1.ui.chat_ai.Activity.ChatActivity
import com.v1.smartv1alculatorv1.ui.history.activity.HistoryActivityNew

class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {
    private val CAMERA_PERMISSION_CODE = 100
    companion object{
        var isClearn = true
    }

    override fun createBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): HomeViewModel {
        return HomeViewModel()
    }

    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding.ConverterUnit.setOnClickListener {
            GlobalFunction.startActivity(this@HomeActivity, HomeUnitConverterActivity::class.java)
        }

        binding.ScanToSolve.setOnClickListener {
            GlobalFunction.startActivity(this@HomeActivity, HistoryActivityNew::class.java)
        }
        binding.AiTutor.setOnClickListener {
            GlobalFunction.startActivity(this@HomeActivity, ChatActivity::class.java)
        }
        checkCameraPermission()
    }


    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            // Quyền đã được cấp, tiếp tục sử dụng camera
            Toast.makeText(this, "Camera is now accessible", Toast.LENGTH_SHORT).show()
        } else {
            // Xin quyền nếu chưa được cấp
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE
            )
        }
    }

    // Xử lý kết quả khi người dùng cho phép hoặc từ chối quyền
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Quyền được cấp, mở camera
                Toast.makeText(this, "Camera is now accessible", Toast.LENGTH_SHORT).show()
            } else {
                // Quyền bị từ chối
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}