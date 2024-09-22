package com.v1.smartv1alculatorv1.ui.scan_to_slove

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.v1.smartv1alculatorv1.R
import com.v1.smartv1alculatorv1.base.BaseActivity
import com.v1.smartv1alculatorv1.base.BaseViewModel
import com.v1.smartv1alculatorv1.databinding.ActivityScanToSoloveBinding
import com.v1.smartv1alculatorv1.ui.smartcalculator.AnswerActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class ScanToSoloveActivity : BaseActivity<ActivityScanToSoloveBinding, BaseViewModel>() {
    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private lateinit var imageView: ImageView
    private lateinit var focusView: FrameLayout
    private var message = ""

    override fun createBinding(): ActivityScanToSoloveBinding {
        return ActivityScanToSoloveBinding.inflate(layoutInflater)
    }

    override fun setViewModel(): BaseViewModel {
        return BaseViewModel()
    }


    override fun initView() {
        super.initView()

        focusView = findViewById(R.id.focus_area)
        var click = findViewById<Button>(R.id.click)
        imageView = findViewById(R.id.imageView)
        click.setOnClickListener {
            previewView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            imageView.setImageDrawable(null)
        }

        previewView = findViewById(R.id.previewView)
        val captureButton: Button = findViewById(R.id.captureButton)

        // Kiểm tra quyền camera
        if (allPermissionsGranted()) {
            startCamera() // Bắt đầu camera nếu đã có quyền
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Set action cho button chụp ảnh
        captureButton.setOnClickListener {
            takePhoto()
            previewView.visibility = View.GONE
            imageView.visibility = View.VISIBLE
        }
        setupFocusView()


        binding.SendMessageButton.setOnClickListener {
            if (message.isNotEmpty()) {
                val intent = Intent(this@ScanToSoloveActivity, Answer2Activity::class.java)
                intent.putExtra("answer_rq_scan", message)
                startActivity(intent)
            }

        }
    }

//    private fun checkCameraPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//            == PackageManager.PERMISSION_GRANTED) {
//            // Quyền đã được cấp, tiếp tục sử dụng camera
//            Toast.makeText(this, "Camera is now accessible", Toast.LENGTH_SHORT).show()
//        } else {
//            // Xin quyền nếu chưa được cấp
//            ActivityCompat.requestPermissions(this,
//                arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
//        }
//    }
//
//    // Xử lý kết quả khi người dùng cho phép hoặc từ chối quyền
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<out String>,
//                                            grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == CAMERA_PERMISSION_CODE) {
//            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                // Quyền được cấp, mở camera
//                Toast.makeText(this, "Camera is now accessible", Toast.LENGTH_SHORT).show()
//            } else {
//                // Quyền bị từ chối
//                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Xác định hiển thị Preview
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY) // Tối ưu hóa cho tốc độ chụp
                .build()

            // Chọn camera sau
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Gắn camera vào lifecycle
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Không thể khởi động camera.", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        // Tạo file lưu ảnh
        val photoFile = File(
            externalMediaDirs.firstOrNull(),
            SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Chụp ảnh
        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Lỗi: Không thể chụp ảnh", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    // Hiển thị ảnh trong ImageView sau khi chụp
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    // Xoay ảnh nếu bị xoay ngang
                    val rotatedBitmap = rotateBitmapIfNeeded(photoFile.absolutePath, bitmap)


                    val imageWidth = rotatedBitmap.width
                    val imageHeight = rotatedBitmap.height

                    val previewWidth = previewView.width
                    val previewHeight = previewView.height

                    val focusLocation = IntArray(2)
                    focusView.getLocationOnScreen(focusLocation)

                    val focusX = focusLocation[0]
                    val focusY = focusLocation[1]
                    val focusWidth = focusView.width
                    val focusHeight = focusView.height

                    val widthRatio = imageWidth.toFloat() / previewWidth.toFloat()
                    val heightRatio = imageHeight.toFloat() / previewHeight.toFloat()

                    val adjustedFocusX = (focusX * widthRatio).toInt()
                    val adjustedFocusY = (focusY * heightRatio).toInt()
                    val adjustedFocusWidth = (focusWidth * widthRatio).toInt()
                    val adjustedFocusHeight = (focusHeight * heightRatio).toInt()

                    val croppedBitmap = Bitmap.createBitmap(
                        rotatedBitmap,
                        adjustedFocusX,
                        adjustedFocusY,
                        adjustedFocusWidth,
                        adjustedFocusHeight
                    )
                    imageView.setImageBitmap(rotatedBitmap)
                    val image = InputImage.fromBitmap(croppedBitmap, 0)

                    // Khởi tạo TextRecognizer
                    val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())

                    // Xử lý hình ảnh
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
                    // Ẩn PreviewView và hiển thị ImageView
                    previewView.visibility = View.GONE
                    imageView.visibility = View.VISIBLE
                }
            })

        val datapath = this.filesDir.toString() + "/tessdata/"
        Log.d(TAG, "datapath: ${datapath}")
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
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
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            else -> return bitmap // Không xoay nếu ảnh đã đúng hướng hoặc không cần thiết
        }

        // Xoay ảnh
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupFocusView() {
        var edgeToResize: Edge? = null
        val scaleGestureDetector = ScaleGestureDetector(focusView.context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val parent = focusView.parent as View

                // Tính toán kích thước mới theo cạnh đang zoom
                val scaleFactor = detector.scaleFactor
                val newWidth = focusView.width
                val newHeight = focusView.height

                when (edgeToResize) {
                    Edge.TOP -> {
                        val deltaHeight = ((focusView.height * (1 - scaleFactor)).toInt())
                        val newTop = (focusView.top + deltaHeight).coerceIn(0, focusView.bottom)
                        focusView.layout(focusView.left, newTop, focusView.right, focusView.bottom)
                    }
                    Edge.BOTTOM -> {
                        val deltaHeight = ((focusView.height * (scaleFactor - 1)).toInt())
                        val newBottom = (focusView.bottom + deltaHeight).coerceIn(focusView.top, parent.height)
                        focusView.layout(focusView.left, focusView.top, focusView.right, newBottom)
                    }
                    Edge.LEFT -> {
                        val deltaWidth = ((focusView.width * (1 - scaleFactor)).toInt())
                        val newLeft = (focusView.left + deltaWidth).coerceIn(0, focusView.right)
                        focusView.layout(newLeft, focusView.top, focusView.right, focusView.bottom)
                    }
                    Edge.RIGHT -> {
                        val deltaWidth = ((focusView.width * (scaleFactor - 1)).toInt())
                        val newRight = (focusView.right + deltaWidth).coerceIn(focusView.left, parent.width)
                        focusView.layout(focusView.left, focusView.top, newRight, focusView.bottom)
                    }
                    null -> {
                        // Không thay đổi khi không chọn cạnh
                    }
                }
                return true
            }
        })

        focusView.setOnTouchListener(object : View.OnTouchListener {
            private var dX = 0f
            private var dY = 0f

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                scaleGestureDetector.onTouchEvent(event) // Kích hoạt ScaleGestureDetector khi có sự kiện chạm
                val parent = v.parent as View

                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = v.x - event.rawX
                        dY = v.y - event.rawY
                        edgeToResize = detectEdgeTouched(event.x, event.y, v)
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if (!scaleGestureDetector.isInProgress) {
                            // Di chuyển focusView trong màn hình
                            val newX = (event.rawX + dX).coerceIn(0f, (parent.width - v.width).toFloat())
                            val newY = (event.rawY + dY).coerceIn(0f, (parent.height - v.height).toFloat())

                            v.animate()
                                .x(newX)
                                .y(newY)
                                .setDuration(0)
                                .start()
                        }
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        edgeToResize = null // Kết thúc việc zoom cạnh
                        return true
                    }
                }
                return false
            }
        })
    }

    // Enum xác định cạnh nào đang được chọn để zoom
    private enum class Edge {
        TOP, BOTTOM, LEFT, RIGHT
    }

    // Hàm để xác định cạnh nào đang được chạm để zoom
    private fun detectEdgeTouched(x: Float, y: Float, view: View): Edge? {
        val edgeSize = 50 // Kích thước của vùng cạnh nhạy với thao tác
        return when {
            y < edgeSize -> Edge.TOP
            y > view.height - edgeSize -> Edge.BOTTOM
            x < edgeSize -> Edge.LEFT
            x > view.width - edgeSize -> Edge.RIGHT
            else -> null
        }
    }
}