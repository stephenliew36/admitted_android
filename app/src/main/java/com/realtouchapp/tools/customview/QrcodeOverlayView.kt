package com.realtouchapp.tools.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.realtouchapp.admitted.R

class QrcodeOverlayView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var cropRect = RectF()
    private var cropPath: Path? = null
    private var drawable: Drawable? = null

    init {
        drawable = ContextCompat.getDrawable(context, R.drawable.ic_qrcode_frame)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (!changed) return

        val width = right - left
        val height = bottom - top
        val widthDp = width / resources.displayMetrics.density
        val heightDp = height / resources.displayMetrics.density

        if (minOf(widthDp, heightDp) < 600) {
            val size = minOf(width, height) * 0.8f

            cropRect.left = (width - size) / 2
            cropRect.right = cropRect.left + size
            cropRect.top = height * 0.4f - size / 2
            cropRect.bottom = cropRect.top + size
        } else {
            val size = minOf(width, height) * 0.5f

            cropRect.left = (width - size) / 2
            cropRect.right = cropRect.left + size
            cropRect.top = (height - size) / 2
            cropRect.bottom = cropRect.top + size
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        createPath()

        canvas.drawARGB(0, 0, 0, 0)
        cropPath?.let {
            canvas.save()
            canvas.clipPath(it, Region.Op.DIFFERENCE)
            canvas.drawARGB(128, 0, 0, 0)
            canvas.restore()
        }

        drawable?.let { drawable ->
            val padding = (5 * resources.displayMetrics.density).toInt()
            drawable.setBounds(
                cropRect.left.toInt() - padding,
                cropRect.top.toInt() - padding,
                cropRect.right.toInt() + padding,
                cropRect.bottom.toInt() + padding
            )

            drawable.draw(canvas)
        }
    }

    private fun createPath() {
        cropPath = Path().apply {
            val cornerRadius = 20f * resources.displayMetrics.density
            addRoundRect(cropRect, cornerRadius, cornerRadius, Path.Direction.CW)
        }
    }
}