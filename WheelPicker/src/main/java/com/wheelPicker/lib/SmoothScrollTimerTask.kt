package com.wheelPicker.lib

import java.util.*

internal class SmoothScrollTimerTask(val loopView: WheelView, var offset: Int) : TimerTask() {
    var realTotalOffset: Int
    var realOffset: Int

    init {
        realTotalOffset = Int.MAX_VALUE
        realOffset = 0
    }

    override fun run() {
        if (realTotalOffset == Int.MAX_VALUE) {
            realTotalOffset = offset
        }
        //把要滚动的范围细分成十小份，按是小份单位来重绘
        realOffset = (realTotalOffset.toFloat() * 0.1f).toInt()
        if (realOffset == 0) {
            realOffset = if (realTotalOffset < 0) {
                -1
            } else {
                1
            }
        }
        if (Math.abs(realTotalOffset) <= 1) {
            loopView.cancelFuture()
            loopView.handler!!.sendEmptyMessage(MessageHandler.Companion.WHAT_ITEM_SELECTED)
        } else {
            loopView.totalScrollY = loopView.totalScrollY + realOffset

            //这里如果不是循环模式，则点击空白位置需要回滚，不然就会出现选到－1 item的 情况
            if (!loopView.isLoop) {
                val itemHeight = loopView.itemHeight
                val top = (-loopView.initPosition).toFloat() * itemHeight
                val bottom =
                    (loopView.itemsCount - 1 - loopView.initPosition).toFloat() * itemHeight
                if (loopView.totalScrollY <= top || loopView.totalScrollY >= bottom) {
                    loopView.totalScrollY = loopView.totalScrollY - realOffset
                    loopView.cancelFuture()
                    loopView.handler!!.sendEmptyMessage(MessageHandler.Companion.WHAT_ITEM_SELECTED)
                    return
                }
            }
            loopView.handler!!.sendEmptyMessage(MessageHandler.Companion.WHAT_INVALIDATE_LOOP_VIEW)
            realTotalOffset = realTotalOffset - realOffset
        }
    }
}