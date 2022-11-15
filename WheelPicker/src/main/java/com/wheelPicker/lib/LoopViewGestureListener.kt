package com.wheelPicker.lib

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent

internal class LoopViewGestureListener(val loopView: WheelView) : SimpleOnGestureListener() {
    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        loopView.scrollBy(velocityY)
        return true
    }
}