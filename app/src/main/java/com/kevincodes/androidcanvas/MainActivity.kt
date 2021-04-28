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


class MainActivity : AppCompatActivity() {
    companion object {
        private const val OFFSET = 120
        private const val MULTIPLIER = 100
    }

    private lateinit var mCanvas: Canvas
    private var mRectanglePaint = Paint()
    private var mUnderlinedPaintedText = Paint(Paint.UNDERLINE_TEXT_FLAG)
    private lateinit var mBitmapOnImageView: Bitmap
    private var mRect: Rect = Rect()
    private var mBounds: Rect = Rect()
    private var mOffset = OFFSET
    private var mFirstDrawnBackgroundColor = 0
    private var mRectFillColor = 0
    private var mColorAccent = 0
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
        mColorAccent = ResourcesCompat.getColor(resources, R.color.teal_200, null)
        mWhiteColor = ResourcesCompat.getColor(resources, R.color.white, null)
        // assign colors
        mRectanglePaint.color = mFirstDrawnBackgroundColor
        mUnderlinedPaintedText.color = mWhiteColor
        mUnderlinedPaintedText.textSize = 70f
    }

    fun drawSomething(view: View) {
        val vWidth = view.width
        val vHeight = view.height
        val halfWidth = vWidth / 2
        val halfHeight = vHeight / 2
        if (mOffset == OFFSET) {
            mBitmapOnImageView = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888)
            binding.myImageview.setImageBitmap(mBitmapOnImageView)
            mCanvas = Canvas(mBitmapOnImageView)
            mCanvas.drawColor(mFirstDrawnBackgroundColor)
            mCanvas.drawText(getString(R.string.keep_tapping), 100f, 100f, mUnderlinedPaintedText)
            mOffset += OFFSET
        } else {
            if (mOffset < halfWidth && mOffset < halfHeight) {
                // Change the color by subtracting an integer.
                mRectanglePaint.color = mRectFillColor - MULTIPLIER * mOffset
                mRect.set(
                    mOffset, mOffset, vWidth - mOffset, vHeight - mOffset
                )
                mCanvas.drawRect(mRect, mRectanglePaint)
                // Increase the indent.
                mOffset += OFFSET
            } else {
                mRectanglePaint.color = mColorAccent
                mCanvas.drawCircle(
                    halfWidth.toFloat(),
                    halfHeight.toFloat(),
                    (halfWidth / 3).toFloat(),
                    mRectanglePaint
                )
                val text = getString(R.string.done)
                // Get bounding box for text to calculate where to draw it.
                mUnderlinedPaintedText.getTextBounds(text, 0, text.length, mBounds)
                // Calculate x and y for text so it's centered.
                val x = halfWidth - mBounds.centerX()
                val y = halfHeight - mBounds.centerY()
                mCanvas.drawText(text, x.toFloat(), y.toFloat(), mUnderlinedPaintedText)
            }
        }
        view.invalidate()
    }
}