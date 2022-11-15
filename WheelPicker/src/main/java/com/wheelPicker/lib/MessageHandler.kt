package com.wheelPicker.lib

import android.os.Handler
import android.os.Message
import com.wheelPicker.lib.WheelView.ACTION

internal class MessageHandler(val loopview: WheelView) : Handler() {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            WHAT_INVALIDATE_LOOP_VIEW -> loopview.invalidate()
            WHAT_SMOOTH_SCROLL -> loopview.smoothScroll(ACTION.FLING)
            WHAT_ITEM_SELECTED -> loopview.onItemSelected()
        }
    }

    companion object {
        const val WHAT_INVALIDATE_LOOP_VIEW = 1000
        const val WHAT_SMOOTH_SCROLL = 2000
        const val WHAT_ITEM_SELECTED = 3000
    }
}