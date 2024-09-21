package com.v1.smartv1alculatorv1.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class CustomCropView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint().apply {
        color = Color.parseColor("#80000000") // Màu mờ cho phần ngoài vùng crop
    }

    private val cropRect = RectF() // Vùng crop sẽ được đặt khi onSizeChanged được gọi
    private val cornerRadius = 30f // Bán kính bo góc
    private val cropPaint: Paint = Paint().apply {
        color = Color.TRANSPARENT // Vùng crop trong suốt
    }

    private var isResizing = false
    private var isDragging = false
    private var activeCorner = -1 // -1 nghĩa là không có góc nào đang được chọn

    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private val cornerTouchAreaSize = 50f // Kích thước vùng chạm quanh các góc

    // Paint cho các đường cong ở góc
    private val cornerPaint: Paint = Paint().apply {
        color = Color.parseColor("#FFA500") // Màu cam cho đường cong
        strokeWidth = 10f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas?.let {
            // Vẽ phần mờ ngoài vùng crop
            val path = Path().apply {
                addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
                addRoundRect(cropRect, cornerRadius, cornerRadius, Path.Direction.CCW) // Bo góc
            }

            it.drawPath(path, paint) // Vẽ phần mờ
            it.drawRoundRect(cropRect, cornerRadius, cornerRadius, cropPaint) // Vẽ vùng crop

            // Vẽ các đường cong bo góc
            drawCornerCurves(canvas)
        }
    }

    private fun drawCornerCurves(canvas: Canvas) {
        val smallRadius = 30f // Bán kính nhỏ hơn cho các đường cong
        val offset = 1f // Độ di chuyển vào trong để vẽ đường cong nằm trong crop

        // Vẽ đường cong ở góc trên trái
        canvas.drawArc(
            RectF(cropRect.left + offset, cropRect.top + offset,
                cropRect.left + 2 * smallRadius - offset, cropRect.top + 2 * smallRadius - offset),
            180f, 100f, false, cornerPaint)

        // Vẽ đường cong ở góc trên phải
        canvas.drawArc(
            RectF(cropRect.right - 2 * smallRadius + offset, cropRect.top + offset,
                cropRect.right - offset, cropRect.top + 2 * smallRadius - offset),
            270f, 100f, false, cornerPaint)

        // Vẽ đường cong ở góc dưới trái
        canvas.drawArc(
            RectF(cropRect.left + offset, cropRect.bottom - 2 * smallRadius + offset,
                cropRect.left + 2 * smallRadius - offset, cropRect.bottom - offset),
            90f, 100f, false, cornerPaint)

        // Vẽ đường cong ở góc dưới phải
        canvas.drawArc(
            RectF(cropRect.right - 2 * smallRadius + offset, cropRect.bottom - 2 * smallRadius + offset,
                cropRect.right - offset, cropRect.bottom - offset),
            0f, 100f, false, cornerPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastTouchX = event.x
                    lastTouchY = event.y

                    activeCorner = getTouchedCorner(event.x, event.y)
                    isResizing = activeCorner != -1 // Nếu góc nào được chọn thì bật chế độ resize
                    if (!isResizing && isTouchingCropArea(event.x, event.y)) {
                        isDragging = true
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - lastTouchX
                    val dy = event.y - lastTouchY

                    if (isResizing) {
                        adjustCropRect(dx, dy)
                    } else if (isDragging) {
                        val newRect = RectF(cropRect)
                        newRect.offset(dx, dy)

                        // Kiểm tra nếu vùng crop không đi ra ngoài màn hình
                        if (newRect.left >= 0 && newRect.top >= 0 &&
                            newRect.right <= width && newRect.bottom <= height) {
                            cropRect.offset(dx, dy)
                        }
                    }

                    lastTouchX = event.x
                    lastTouchY = event.y
                    postInvalidateOnAnimation()
                }

                MotionEvent.ACTION_UP -> {
                    isResizing = false
                    isDragging = false
                    activeCorner = -1
                }
            }
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Tính toán kích thước của vùng crop
        val cropWidth = 650f // Chiều rộng của vùng crop
        val cropHeight = 280f // Chiều cao của vùng crop

        // Đặt vùng crop ở giữa màn hình
        val left = (w - cropWidth) / 2
        val top = (h - cropHeight) / 2
        val right = left + cropWidth
        val bottom = top + cropHeight

        // Cập nhật lại cropRect
        cropRect.set(left, top, right, bottom)
    }

    private fun getTouchedCorner(x: Float, y: Float): Int {
        val left = cropRect.left
        val top = cropRect.top
        val right = cropRect.right
        val bottom = cropRect.bottom

        return when {
            isPointInCornerArea(x, y, left, top) -> 0 // Top left
            isPointInCornerArea(x, y, right, top) -> 1 // Top right
            isPointInCornerArea(x, y, left, bottom) -> 2 // Bottom left
            isPointInCornerArea(x, y, right, bottom) -> 3 // Bottom right
            else -> -1
        }
    }

    private fun isPointInCornerArea(x: Float, y: Float, cornerX: Float, cornerY: Float): Boolean {
        return x >= cornerX - cornerTouchAreaSize && x <= cornerX + cornerTouchAreaSize &&
                y >= cornerY - cornerTouchAreaSize && y <= cornerY + cornerTouchAreaSize
    }

    private fun adjustCropRect(dx: Float, dy: Float) {
        when (activeCorner) {
            0 -> { // Top left
                cropRect.left += dx
                cropRect.top += dy
            }
            1 -> { // Top right
                cropRect.right += dx
                cropRect.top += dy
            }
            2 -> { // Bottom left
                cropRect.left += dx
                cropRect.bottom += dy
            }
            3 -> { // Bottom right
                cropRect.right += dx
                cropRect.bottom += dy
            }
        }
    }

    private fun isTouchingCropArea(x: Float, y: Float): Boolean {
        return cropRect.contains(x, y)
    }

    fun cropImage(originalBitmap: Bitmap): Bitmap? {
        // Tính toán vị trí và kích thước của vùng crop
        val cropWidth = cropRect.width().toInt()
        val cropHeight = cropRect.height().toInt()

        // Kiểm tra nếu vùng crop nằm trong kích thước của ảnh gốc
        if (cropRect.left < 0 || cropRect.top < 0 ||
            cropRect.right > originalBitmap.width ||
            cropRect.bottom > originalBitmap.height) {
            return null // Vùng crop không hợp lệ
        }

        // Cắt ảnh theo vùng crop
        return Bitmap.createBitmap(originalBitmap,
            cropRect.left.toInt(),
            cropRect.top.toInt(),
            cropWidth,
            cropHeight)
    }

}
