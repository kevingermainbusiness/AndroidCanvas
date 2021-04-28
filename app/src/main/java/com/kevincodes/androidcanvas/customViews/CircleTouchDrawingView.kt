package com.kevincodes.androidcanvas.customViews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent
import kotlin.math.roundToInt

class CircleTouchDrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var circlePaint: Paint = Paint()

    private var pointsInScreen: MutableList<Point>

    init {
        setupPaint()
        pointsInScreen = arrayListOf()
    }

    private fun setupPaint() {
        circlePaint.isAntiAlias = true
        circlePaint.color = Color.RED
        circlePaint.style = Paint.Style.FILL
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event!!.x
        val touchY = event.y
        pointsInScreen.add(Point(touchX.roundToInt(), touchY.roundToInt()))
        postInvalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        pointsInScreen.forEach { point ->
            canvas?.drawCircle(point.x.toFloat(), point.y.toFloat(), 50f, circlePaint)
        }
    }
}