package com.wheelPicker.lib
import com.wheelPicker.listener.OnItemSelectedListener
import java.util.*

internal class InertiaTimerTask(val loopView: WheelView, val velocityY: Float) : TimerTask() {
    var a: Float

    init {
        a = Int.MAX_VALUE.toFloat()
    }


    override fun run() {
        if (a == Int.MAX_VALUE.toFloat()) {
            a = if (Math.abs(velocityY) > 2000f) {
                if (velocityY > 0.0f) {
                    2000f
                } else {
                    -2000f
                }
            } else {
                velocityY
            }
        }
        if (Math.abs(a) >= 0.0f && Math.abs(a) <= 20f) {
            loopView.cancelFuture()
            loopView.handler?.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL)
            return
        }
        val i = (a * 10f / 1000f).toInt()
        loopView.totalScrollY = loopView.totalScrollY - i
        if (!loopView.isLoop) {
            val itemHeight = loopView.itemHeight
            var top = -loopView.initPosition * itemHeight
            var bottom = (loopView.itemsCount - 1 - loopView.initPosition) * itemHeight
            if (loopView.totalScrollY - itemHeight * 0.3 < top) {
                top = (loopView.totalScrollY + i).toFloat()
            } else if (loopView.totalScrollY + itemHeight * 0.3 > bottom) {
                bottom = (loopView.totalScrollY + i).toFloat()
            }
            if (loopView.totalScrollY <= top) {
                a = 40f
                loopView.totalScrollY = top.toInt()
            } else if (loopView.totalScrollY >= bottom) {
                loopView.totalScrollY = bottom.toInt()
                a = -40f
            }
        }
        a = if (a < 0.0f) {
            a + 20f
        } else {
            a - 20f
        }
        loopView.handler?.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW)
    }
}