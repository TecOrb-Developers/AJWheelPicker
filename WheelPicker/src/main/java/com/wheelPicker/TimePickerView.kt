package com.wheelPicker

import android.content.Context
import com.wheelPicker.view.BasePickerView
import android.graphics.Typeface
import android.util.Log
import com.wheelPicker.view.MyWheelTime
import android.widget.TextView
import com.wheelPicker.TimePickerView.OnTimeSelectListener
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.wheelPicker.R
import com.wheelPicker.view.WheelTime
import java.text.ParseException
import java.util.*

/**
 * Created by ajay on 11/11/22.
 */
class TimePickerView(context: Context?, type: Type?) : BasePickerView(context!!),
    View.OnClickListener {
    override fun setCustomFont(typeface: Typeface) {
        wheelTime.setCustomTypeface(typeface)
    }

    enum class Type {
        ALL, YEAR_MONTH_DAY, HOURS_MINS, MONTH_DAY_HOUR_MIN, YEAR_MONTH, HOUR_MIN_SEC, HOUR_MIN_APPM
    } // 四种选择模式，年月日时分，年月日，时分，月日时分

    var wheelTime: MyWheelTime
   // private val btnSubmit: View? = null
    //private val btnCancel: View? = null
    private val tvTitle: TextView
    private var timeSelectListener: OnTimeSelectListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.pickerview_time, contentContainer)
        // -----确定和取消按钮
        setBtnSubmit(findViewById(R.id.btnSubmit) as Button)
        getBtnSubmit()?.let {
            it.tag = TAG_SUBMIT
        }
        setBtnCancel(findViewById(R.id.btnCancel) as Button)
        getBtnCancel()?.let {
            it.tag = TAG_CANCEL
        }
        getBtnSubmit()?.let {
            it.setOnClickListener(this)
        }
        getBtnCancel()?.let {
            it.setOnClickListener(this)
        }
        //顶部标题
        tvTitle = findViewById(R.id.tvTitle) as TextView
        // ----时间转轮
        val timepickerview = findViewById(R.id.timepicker)
        wheelTime = MyWheelTime(timepickerview, type!!)

        //默认选中当前时间
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val hours = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]
        val ampm = calendar[Calendar.AM_PM]
        wheelTime.setPicker(year, month, day, hours, minute, second, ampm)
    }

    /**
     * 设置可以选择的时间范围
     *
     * @param startYear
     * @param endYear
     */
    fun setRange(startYear: Int, endYear: Int) {
        wheelTime.startYear = startYear
        wheelTime.endYear = endYear
    }

    fun setTime(date: Date?, type: Type) {
        val calendar = Calendar.getInstance()
        if (date == null) calendar.timeInMillis = System.currentTimeMillis() else calendar.time =
            date
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val hours: Int
        hours = if (type == Type.HOUR_MIN_APPM) {
            calendar[Calendar.HOUR]
        } else {
            calendar[Calendar.HOUR_OF_DAY]
        }
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]
        Log.v("am_pm", "" + calendar[Calendar.AM_PM])
        val ampm = calendar[Calendar.AM_PM]
        wheelTime.setPicker(year, month, day, hours, minute, second, ampm)
    }
    //    /**
    //     * 指定选中的时间，显示选择器
    //     *
    //     * @param date
    //     */
    //    public void show(Date date) {
    //        Calendar calendar = Calendar.getInstance();
    //        if (date == null)
    //            calendar.setTimeInMillis(System.currentTimeMillis());
    //        else
    //            calendar.setTime(date);
    //        int year = calendar.get(Calendar.YEAR);
    //        int month = calendar.get(Calendar.MONTH);
    //        int day = calendar.get(Calendar.DAY_OF_MONTH);
    //        int hours = calendar.get(Calendar.HOUR_OF_DAY);
    //        int minute = calendar.get(Calendar.MINUTE);
    //        wheelTime.setPicker(year, month, day, hours, minute);
    //        show();
    //    }
    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    fun setCyclic(cyclic: Boolean) {
        wheelTime.setCyclic(cyclic)
    }

    override fun onClick(v: View) {
        val tag = v.tag as String
        if (tag == TAG_CANCEL) {
            dismiss()
            return
        } else {
            if (timeSelectListener != null) {
                try {
                    val date = WheelTime.dateFormat.parse(wheelTime.time)
                    timeSelectListener!!.onTimeSelect(date)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
            }
            dismiss()
            return
        }
    }

    interface OnTimeSelectListener {
        fun onTimeSelect(date: Date?)
    }

    fun setOnTimeSelectListener(timeSelectListener: OnTimeSelectListener?) {
        this.timeSelectListener = timeSelectListener
    }

    fun setTitle(title: String?) {
        tvTitle.text = title
    }

    companion object {
        private const val TAG_SUBMIT = "submit"
        private const val TAG_CANCEL = "cancel"
    }
}