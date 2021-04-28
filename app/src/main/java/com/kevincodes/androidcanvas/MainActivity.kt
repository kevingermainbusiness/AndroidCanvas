package com.kevincodes.androidcanvas

import android.R.attr
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.kevincodes.androidcanvas.databinding.ActivityMainBinding
import timber.log.Timber
import android.R.attr.width
import android.os.Build


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
    private var mChocolateColor = 0

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
        mChocolateColor = ResourcesCompat.getColor(resources, R.color.chocolate, null)
        // assign colors
        mDefaultPainter.color = mFirstDrawnBackgroundColor
        mUnderlinedTextPainter.color = mWhiteColor
        mUnderlinedTextPainter.textSize = 70f
    }

    fun drawADonut(mView: View) {
        val vWidth = mView.width
        val vHeight = mView.height
        mBitmapOnImageView = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888)
        binding.myImageview.setImageBitmap(mBitmapOnImageView)
        mCanvas = Canvas(mBitmapOnImageView)
        val basePaint = Paint().apply {
            color = 0xffc6853b.toInt() // light brown color
            isAntiAlias = true
        }

        // Create a rect that references the size of the imageView
        val mViewBounds = Rect(mView.left, mView.top, mView.right, mView.bottom)
        val halfWidth = mViewBounds.width() / 2
        val halfHeight = mViewBounds.height() / 2

        // Save current state of the canvas
        // Will ensure that clipping, rotating doesn't stop future
        // drawings over the canvas
        mCanvas.save()

        // Draw the hole inside the donut first
        val holeCircularPath = Path()
        holeCircularPath.addCircle(
            halfWidth.toFloat(),
            halfHeight.toFloat(),
            mViewBounds.width() / 8.0f,
            Path.Direction.CCW
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mCanvas.clipOutPath(holeCircularPath)
        } else {
            mCanvas.clipPath(holeCircularPath, Region.Op.DIFFERENCE)
        }

        // The top layer of the donut with the light brown color
        mCanvas.drawCircle(
            halfWidth.toFloat(),
            halfHeight.toFloat(),
            vWidth.toFloat() / 4f,
            basePaint
        )

        // Initiate an icing paint & path effect
        val icingPaint = Paint().apply {
            color = mChocolateColor
            pathEffect = ComposePathEffect(
                CornerPathEffect(40f),
                DiscretePathEffect(60f, 25f)
            )
        }
        // Draw the icing circle with the path effects
        mCanvas.drawCircle(
            halfWidth.toFloat(),
            halfHeight.toFloat(),
            vWidth.toFloat() / 4.2f,
            icingPaint
        )

        // Restore the state of the canvas before clipping
        mCanvas.restore()

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
    fun drawRectAndCenteredCircle(view: View) {
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