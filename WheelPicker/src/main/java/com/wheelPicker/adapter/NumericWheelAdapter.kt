package com.wheelPicker.adapter

import kotlin.jvm.JvmOverloads
import com.wheelPicker.adapter.WheelAdapter
import com.wheelPicker.adapter.NumericWheelAdapter

/**
 * Created by ajay on 11/11/22.
 * Numeric Wheel adapter.
 */
class NumericWheelAdapter
/**
 * Default constructor
 */ @JvmOverloads constructor(// Values
    private val minValue: Int = DEFAULT_MIN_VALUE, private val maxValue: Int = DEFAULT_MAX_VALUE
) : WheelAdapter<Any?> {
    /**
     * Constructor
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    override fun getItem(index: Int): Any? {
        return if (index >= 0 && index < itemsCount) {
            minValue + index
        } else 0
    }

    override val itemsCount: Int
        get() = maxValue - minValue + 1

    override fun indexOf(o: Any?): Int {
        return o as Int - minValue
    }

    companion object {
        /** The default min value  */
        const val DEFAULT_MAX_VALUE = 9

        /** The default max value  */
        private const val DEFAULT_MIN_VALUE = 0
    }
}