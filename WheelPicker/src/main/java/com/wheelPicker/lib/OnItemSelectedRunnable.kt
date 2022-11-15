package com.wheelPicker.lib

internal class OnItemSelectedRunnable(val loopView: WheelView) : Runnable {
    override fun run() {
        loopView.onItemSelectedListener!!.onItemSelected(loopView.currentItem)
    }
}