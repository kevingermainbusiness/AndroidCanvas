package com.kevincodes.androidcanvas

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.kevincodes.androidcanvas.databinding.ActivityMainBinding
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    companion object {
        private const val OFFSET = 120
        private const val COLOR_MULTIPLIER = 80
    }

    private lateinit var mCanvas: Canvas
    private var mDefaultPainter = Paint()
    private var mUnderlinedTextPainter = Paint(Paint.UNDERLINE_TEXT_FLAG)
    private lateinit var mBitmapOnImageView: Bitmap
    private var mInsideRect: Rect = Rect()
    private var mBounds: Rect = Rect()
    private var mOffset = OFFSET
    private var mFirstDrawnBackgroundColor = 0
    private var mRectFillColor = 0
    private var mWhiteColor = 0

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {
        mFirstDrawnBackgroundColor = ResourcesCompat.getColor(resources, R.color.purple_500, null)
        mRectFillColor = ResourcesCompat.getColor(resources, R.color.purple_700, null)
        mWhiteColor = ResourcesCompat.getColor(resources, R.color.white, null)
        // assign colors
        mDefaultPainter.color = mFirstDrawnBackgroundColor
        mUnderlinedTextPainter.color = mWhiteColor
        mUnderlinedTextPainter.textSize = 70f
    }

    /**
     * When a user just opened the app, the first condition will always be true,
     * thus be executed first
     * [mOffset] from what I can understand, helps adjusts the location of
     * an element on the canvas, it's used as a pointer to place stuff on the canvas
     * and define if a drawing should occur
     *
     * In the first condition, the imageView gets assigned its [Bitmap], the same
     * [Bitmap] that the [mCanvas] will to draw into.
     * The [mCanvas] draws a bg and a text on the [Bitmap], the [mOffset] is then
     * incremented by 1, then [View.invalidate] is called to make these changes visible
     * inside this [Bitmap]
     * */
    fun respondToImageViewClick(view: View) {
        val vWidth = view.width
        val vHeight = view.height
        val halfWidth = vWidth / 2
        val halfHeight = vHeight / 2
        if (mOffset == OFFSET) {
            mBitmapOnImageView = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888)
            binding.myImageview.setImageBitmap(mBitmapOnImageView)
            mCanvas = Canvas(mBitmapOnImageView)
            mCanvas.drawColor(mFirstDrawnBackgroundColor)
            mCanvas.drawText(
                getString(R.string.keep_tapping),
                (mOffset * 2).toFloat(), // Where the first rect will be drawn
                (halfHeight * 0.15).toFloat(), // 15% of half height
                mUnderlinedTextPainter
            )
            mOffset += OFFSET
            Timber.d("First offset size increase: $mOffset")
        } else {
            if (mOffset < halfWidth && mOffset < halfHeight) {
                // Change the color by subtracting an integer.
                mDefaultPainter.color = mRectFillColor - COLOR_MULTIPLIER * mOffset
                mInsideRect.set(
                    mOffset, mOffset, vWidth - mOffset, vHeight - mOffset
                )
                mCanvas.drawRect(mInsideRect, mDefaultPainter)
                // Increase the offset.
                mOffset += OFFSET
                Timber.d("Half width: $halfWidth, Half Height:$halfHeight, Offset size below half image size: $mOffset")
            } else {
                mDefaultPainter.color = mFirstDrawnBackgroundColor
                mCanvas.drawCircle(
                    halfWidth.toFloat(),
                    halfHeight.toFloat(),
                    (halfWidth / 3).toFloat(),
                    mDefaultPainter
                )
                val mTextInsideCircle = getString(R.string.done)
                // Get bounding box for text to calculate where to draw it.
                mUnderlinedTextPainter.getTextBounds(
                    mTextInsideCircle,
                    0,
                    mTextInsideCircle.length,
                    mBounds
                )
                // Calculate x and y for text so it's centered.
                val x = halfWidth - mBounds.centerX()
                val y = halfHeight - mBounds.centerY()
                mCanvas.drawText(
                    mTextInsideCircle,
                    x.toFloat(),
                    y.toFloat(),
                    mUnderlinedTextPainter
                )
                Timber.d("Offset size, not equal to original size nor inferior to half size: $mOffset")
            }
        }
        view.invalidate()
    }
}