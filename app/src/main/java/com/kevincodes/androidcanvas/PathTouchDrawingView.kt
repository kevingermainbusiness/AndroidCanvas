package com.kevincodes.androidcanvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent


class PathTouchDrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    // Get height or width of screen
//    var screenHeight = DeviceDimensionsHelper.getDisplayHeight(context)
//    var screenWidth = DeviceDimensionsHelper.getDisplayWidth(context)

    // Convert dp to pixels
//    var px = DeviceDimensionsHelper.convertDpToPixel(25f, context)

    // Convert pixels to dp
//    var dp = DeviceDimensionsHelper.convertPixelsToDp(25f, context)

    private var mDefaultPaint: Paint = Paint()
    private var mPath = Path()

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        setupPaint()
    }

    private fun setupPaint() {
        mDefaultPaint.color = Color.GREEN
        mDefaultPaint.isAntiAlias = true
        mDefaultPaint.strokeWidth = 5f
        mDefaultPaint.strokeJoin = Paint.Join.ROUND
        mDefaultPaint.strokeCap = Paint.Cap.ROUND
        mDefaultPaint.style = Paint.Style.STROKE
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event!!.x
        val touchY = event.y
        // Checks for the event that occurs
        when (event.action) {
            MotionEvent.ACTION_DOWN -> // Starts a new line in the path
                mPath.moveTo(touchX, touchY)
            MotionEvent.ACTION_MOVE -> // Draws line between last point and this point
                mPath.lineTo(touchX, touchY)
            else -> return false
        }
        // Force a view to draw again
        postInvalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mPath, mDefaultPaint)
    }
}