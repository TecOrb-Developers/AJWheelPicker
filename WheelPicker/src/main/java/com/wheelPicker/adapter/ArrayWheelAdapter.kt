package com.wheelPicker.adapter
import kotlin.jvm.JvmOverloads
import java.util.ArrayList
/**
 * Created by ajay on 11/11/22.
 * The simple Array wheel adapter
 * @param <T> the element type
</T> */
class ArrayWheelAdapter<T>
/**
 * Contructor
 * @param items the items
 */ @JvmOverloads constructor(private val items: ArrayList<T>,  private val length: Int = DEFAULT_LENGTH) : WheelAdapter<Any?> {
    /**
     * Constructor
     * @param items the items
     * @param length the max items length
     */
    override fun getItem(index: Int): Any? {
        return if (index >= 0 && index < items.size) {
            items[index]
        } else ""
    }
    override val itemsCount: Int
        get() = items.size

    override fun indexOf(o: Any?): Int {
        return items.indexOf(o)
    }

    companion object {
        /** The default items length  */
        const val DEFAULT_LENGTH = 4
    }
}