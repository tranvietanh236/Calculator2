package com.v1.smartv1alculatorv1.ui.scan_to_slove

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityScanNewBinding

class ScanActivityNew : BaseActivity<ActivityScanNewBinding, BaseViewModel>() {
    override fun createBinding(): ActivityScanNewBinding {
        return ActivityScanNewBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Lấy cameraProvider
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Tạo Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            // Chọn camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Gỡ bỏ các use cases đã có
            cameraProvider.unbindAll()

            // Ràng buộc camera với lifecycle
            cameraProvider.bindToLifecycle(this, cameraSelector, preview)
        }, ContextCompat.getMainExecutor(this))
    }
}