package com.v1.smartv1alculatorv1.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet

class StrokeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyle) {

    private val strokePaint = Paint()

    init {
        // Thiết lập paint cho viền
        strokePaint.isAntiAlias = true
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = 10f // Độ dày của viền
        strokePaint.color = Color.WHITE // Màu của viền
    }

    override fun onDraw(canvas: Canvas) {
        val textColor = textColors.defaultColor // Lưu màu chữ hiện tại

        // Vẽ viền bằng cách đặt chế độ paint là STROKE
        strokePaint.textSize = textSize // Kích thước chữ
        strokePaint.typeface = typeface // Kiểu chữ
        canvas.drawText(text.toString(), (width - strokePaint.measureText(text.toString())) / 2, baseline.toFloat(), strokePaint)

        // Vẽ chữ bên trong (nội dung chính) bằng chế độ FILL
        setTextColor(textColor)
        paint.style = Paint.Style.FILL
        super.onDraw(canvas)
    }
}
