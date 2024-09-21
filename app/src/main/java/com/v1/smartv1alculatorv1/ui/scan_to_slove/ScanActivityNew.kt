package com.v1.smartv1alculatorv1.ui.scan_to_slove

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.common.InputImage
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.calculator.customformula.utils.GlobalFunction
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityScanNewBinding
import com.v1.smartv1alculatorv1.ui.smartcalculator.AnswerActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class ScanActivityNew : BaseActivity<ActivityScanNewBinding, BaseViewModel>() {

    private var imageCapture: ImageCapture? = null
    private var message = ""
    override fun createBinding(): ActivityScanNewBinding {
        return ActivityScanNewBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun initView() {
        super.initView()
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        startCamera()
        binding.icBackScan.setOnClickListener {
            finish()
        }
        binding.imgCap.setOnClickListener {
            takePicture()
            binding.imageView.visibility = View.VISIBLE
            binding.previewView.visibility = View.GONE
            binding.imgCap.visibility = View.GONE
            binding.imgCap2.visibility = View.VISIBLE
        }

        binding.imgCap2.setOnClickListener {
            binding.imageView.isDrawingCacheEnabled = true
            binding.imageView.buildDrawingCache()
            val originalBitmap = Bitmap.createBitmap(binding.imageView.drawingCache)
            binding.imageView.isDrawingCacheEnabled = false
            val croppedBitmap = binding.customCropView.cropImage(originalBitmap)
            if (croppedBitmap != null) {
                recognizeTextFromImage(croppedBitmap)
                lifecycleScope.launch {
                    delay(500) // Delay 1 giây
                    val intent = Intent(this@ScanActivityNew, Answer2Activity::class.java)
                    intent.putExtra("answer_rq2", message)
                   // intent.putExtra("image_data", croppedBitmap)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "Vùng crop không hợp lệ!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recognizeTextFromImage(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Xử lý văn bản trích xuất được
                val text = visionText.text
                message = text
                Log.d("TextRecognition", "Extracted Text: $message")
            }
            .addOnFailureListener { e ->
                Log.e("TextRecognition", "Error recognizing text", e)
            }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Xác định hiển thị Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // Tối ưu hóa cho tốc độ chụp
                .build()

            // Chọn camera sau
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {

            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePicture() {
        val photoFile = File(externalMediaDirs.first(), "${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Hiển thị ảnh lên ImageView
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    val rotatedBitmap = rotateBitmapIfNeeded(photoFile.absolutePath, bitmap)
                    binding.imageView.setImageBitmap(rotatedBitmap)
                    binding.customCropView.visibility = View.VISIBLE
                }

                override fun onError(exception: ImageCaptureException) {
                    // Xử lý lỗi
                }
            })
    }

    private fun rotateBitmapIfNeeded(filePath: String, bitmap: Bitmap): Bitmap {
        val ei: ExifInterface
        try {
            ei = ExifInterface(filePath)
        } catch (e: IOException) {
            e.printStackTrace()
            return bitmap
        }

        // Kiểm tra hướng của ảnh
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap // Không xoay nếu ảnh đã đúng hướng hoặc không cần thiết
        }

        // Xoay ảnh
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


}