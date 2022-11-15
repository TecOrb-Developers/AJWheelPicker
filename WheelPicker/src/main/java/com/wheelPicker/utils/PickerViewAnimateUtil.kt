package com.wheelPicker.utils

import android.view.Gravity
import com.wheelPicker.R
import com.wheelPicker.utils.PickerViewAnimateUtil

/**
 * Created by ajay on 11/11/22.
 */
object PickerViewAnimateUtil {
    private const val INVALID = -1

    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the gravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    fun getAnimationResource(gravity: Int, isInAnimation: Boolean): Int {
        when (gravity) {
            Gravity.BOTTOM -> return if (isInAnimation) R.anim.slide_in_bottom else R.anim.slide_out_bottom
        }
        return INVALID
    }
}