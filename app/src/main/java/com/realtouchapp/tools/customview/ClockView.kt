package com.realtouchapp.tools.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View


/*
    作者: RobertChou
    時間: 2023/1/24 下午 06:19
    信箱: didi31139@gmail.com
    說明:
*/
class ClockView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2
        val centerY = height / 2
        val radius = Math.min(width, height) / 2
        val clockLineWidth = radius * 0.9f
        val deepBlueLine = radius * 0.78f
        val deepBlueCircle = radius * 0.67f
        val grayCicle = radius * 0.7f

        var progressAngle = (progress / 100f) * 360.0f


//        Log.d("radius", "onDraw: " + radius)

        val paint = Paint()
        paint.color = Color.parseColor("#E0E0E0")
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        for (i in 0..11) {
            val angle = i * 30.0
            val x1 = centerX + radius * Math.sin(angle * (Math.PI / 180))
            val y1 = centerY - radius * Math.cos(angle * Math.PI / 180)
            val x2 = centerX + clockLineWidth * Math.sin(angle * Math.PI / 180)
            val y2 = centerY - clockLineWidth * Math.cos(angle * Math.PI / 180)
//            Log.d("radiusi", "onDraw: "+i)
//            Log.d("radiusX1", "onDraw: "+x1)
//            Log.d("radiusX2", "onDraw: "+x2)
//            Log.d("radiusY1", "onDraw: "+y1)
//            Log.d("radiusY2", "onDraw: "+y2)
            canvas.drawLine(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), paint)
        }

        //內圈灰色
        val paint4 = Paint()
        paint4.style = Paint.Style.STROKE
        paint4.isAntiAlias = true
        paint4.color = Color.parseColor("#ECEFF3")
        paint4.strokeWidth = (clockLineWidth - grayCicle - (clockLineWidth - deepBlueLine) / 3f)
        canvas.drawCircle(centerX, centerY, grayCicle, paint4)

        //外圍藍線
        val paint2 = Paint()
        paint2.style = Paint.Style.STROKE
        paint2.isAntiAlias = true
        paint2.color = Color.parseColor("#3F5D95")
        paint2.strokeWidth = (clockLineWidth - deepBlueLine) / 3f
        canvas.drawArc(
            radius - (deepBlueLine),
            radius - (deepBlueLine),
            radius + deepBlueLine,
            radius + deepBlueLine,
            270f,
            progressAngle,
            false,
            paint2
        )

        //外圍藍圈
        val px1 = centerX + deepBlueLine * Math.sin(progressAngle * (Math.PI / 180))
        val py1 = centerY - deepBlueLine * Math.cos(progressAngle * Math.PI / 180)
        val paint3 = Paint()
        paint3.color = Color.parseColor("#3F5D95")
        paint3.isAntiAlias = true
        canvas.drawCircle(
            px1.toFloat(),
            py1.toFloat(),
            (clockLineWidth - deepBlueCircle - ((clockLineWidth - deepBlueLine) / 3f)) / 2,
            paint3
        )
    }
    var progress = 0;
    fun setProgressPercent(progress:Int){
        this.progress = progress
        invalidate()

    }
}