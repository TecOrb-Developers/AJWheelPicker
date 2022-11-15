package com.wheelPicker.view

import android.graphics.Typeface
import android.util.Log
import android.view.View
import com.wheelPicker.R
import com.wheelPicker.TimePickerView
import com.wheelPicker.adapter.ArrayWheelAdapter
import com.wheelPicker.adapter.NumericWheelAdapter
import com.wheelPicker.lib.WheelView
import com.wheelPicker.listener.OnItemSelectedListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WheelTime {
    var view: View
    private var wv_year: WheelView? = null
    private var wv_month: WheelView? = null
    private var wv_day: WheelView? = null
    private var wv_hours: WheelView? = null
    private var wv_mins: WheelView? = null
    private var wv_seconds: WheelView? = null
    private var wv_ampm: WheelView? = null
    private var type: TimePickerView.Type
    var startYear = DEFULT_START_YEAR
    var endYear = DEFULT_END_YEAR

    constructor(view: View) : super() {
        this.view = view
        type = TimePickerView.Type.ALL

    }

    constructor(view: View, type: TimePickerView.Type) : super() {
        this.view = view
        this.type = type

    }

    fun setPicker(first: Int, second: Int, third: Int, type: TimePickerView.Type) {
        if (type === TimePickerView.Type.YEAR_MONTH_DAY) this.setPicker(
            first,
            second,
            third,
            0,
            0,
            0,
            0
        ) else if (type === TimePickerView.Type.HOUR_MIN_SEC) this.setPicker(
            0,
            0,
            0,
            first,
            second,
            third,
            0
        )
    }
    /*public void setPicker(int hour, int min, int sec) {
        this.setPicker(0, 0, 0, hour, min, sec);
    }*/
    /**
     * @Description: TODO 弹出日期时间选择器
     */
    fun setPicker(year: Int, month: Int, day: Int, h: Int, m: Int, s: Int, ampm: Int) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        val months_big = arrayOf("1", "3", "5", "7", "8", "10", "12")
        val months_little = arrayOf("4", "6", "9", "11")
        val list_big = Arrays.asList(*months_big)
        val list_little = Arrays.asList(*months_little)
        val context = view.context
        // 年
        wv_year = view.findViewById<View>(R.id.year) as WheelView
        wv_year!!.adapter = NumericWheelAdapter(startYear, endYear) // 设置"年"的显示数据
        wv_year!!.setLabel(context.getString(R.string.pickerview_year)) // 添加文字
        wv_year!!.currentItem = year - startYear // 初始化时显示的数据

        // 月
        wv_month = view.findViewById<View>(R.id.month) as WheelView
        wv_month!!.adapter = NumericWheelAdapter(1, 12)
        wv_month!!.setLabel(context.getString(R.string.pickerview_month))
        wv_month!!.currentItem = month

        // 日
        wv_day = view.findViewById<View>(R.id.day) as WheelView
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains((month + 1).toString())) {
            wv_day!!.adapter = NumericWheelAdapter(1, 31)
        } else if (list_little.contains((month + 1).toString())) {
            wv_day!!.adapter = NumericWheelAdapter(1, 30)
        } else {
            // 闰年
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) wv_day!!.adapter =
                NumericWheelAdapter(1, 29) else wv_day!!.adapter = NumericWheelAdapter(1, 28)
        }
        wv_day!!.setLabel(context.getString(R.string.pickerview_day))
        wv_day!!.currentItem = day - 1
        if (type === TimePickerView.Type.HOUR_MIN_APPM) {
            wv_hours = view.findViewById<View>(R.id.hour) as WheelView
            wv_hours!!.adapter = NumericWheelAdapter(1, 12)
            wv_hours!!.setLabel(context.getString(R.string.pickerview_hours)) // 添加文字
            wv_hours!!.currentItem = h
        } else {
            wv_hours = view.findViewById<View>(R.id.hour) as WheelView
            wv_hours!!.adapter = NumericWheelAdapter(0, 23)
            wv_hours!!.setLabel(context.getString(R.string.pickerview_hours)) // 添加文字
            wv_hours!!.currentItem = h
        }
        wv_mins = view.findViewById<View>(R.id.min) as WheelView
        wv_mins!!.adapter = NumericWheelAdapter(0, 59)
        wv_mins!!.setLabel(context.getString(R.string.pickerview_minutes)) // 添加文字
        wv_mins!!.currentItem = m
        wv_seconds = view.findViewById<View>(R.id.sec) as WheelView
        wv_seconds!!.adapter = NumericWheelAdapter(0, 59)
        wv_seconds!!.setLabel(context.getString(R.string.pickerview_seconds)) // 添加文字
        val amPM = arrayOf("AM", "PM")
        val ampmlist = ArrayList(Arrays.asList(*amPM))
        wv_ampm = view.findViewById<View>(R.id.amPm) as WheelView
        wv_ampm!!.adapter = ArrayWheelAdapter(ampmlist, ampmlist.size)
        wv_ampm!!.setLabel(context.getString(R.string.pickerview_ampm)) // 添加文字
        wv_ampm!!.currentItem = ampm


        // 添加"年"监听
        val wheelListener_year = object:OnItemSelectedListener{
            override fun onItemSelected(index: Int) {
                val year_num = index + startYear
                // 判断大小月及是否闰年,用来确定"日"的数据
                var maxItem = 30
                if (list_big
                        .contains((wv_month!!.currentItem + 1).toString())
                ) {
                    wv_day!!.adapter = NumericWheelAdapter(1, 31)
                    maxItem = 31
                } else if (list_little.contains(
                        (wv_month!!.currentItem + 1).toString())) {
                    wv_day!!.adapter = NumericWheelAdapter(1, 30)
                    maxItem = 30
                } else {
                    if (year_num % 4 == 0 && year_num % 100 != 0
                        || year_num % 400 == 0
                    ) {
                        wv_day!!.adapter = NumericWheelAdapter(1, 29)
                        maxItem = 29
                    } else {
                        wv_day!!.adapter = NumericWheelAdapter(1, 28)
                        maxItem = 28
                    }
                }
                if (wv_day!!.currentItem > maxItem - 1) {
                    wv_day!!.currentItem = maxItem - 1
                }
            }
        }

        // 添加"月"监听
        var wheelListener_month: OnItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(index: Int) {
                val month_num = index + 1
                var maxItem = 30
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(month_num.toString())) {
                    wv_day!!.adapter = NumericWheelAdapter(1, 31)
                    maxItem = 31
                } else if (list_little.contains(month_num.toString())) {
                    wv_day!!.adapter = NumericWheelAdapter(1, 30)
                    maxItem = 30
                } else {
                    if ((wv_year!!.currentItem + startYear) % 4 == 0 && (wv_year!!
                            .currentItem + startYear) % 100 != 0
                        || (wv_year!!.currentItem + startYear) % 400 == 0
                    ) {
                        wv_day!!.adapter = NumericWheelAdapter(1, 29)
                        maxItem = 29
                    } else {
                        wv_day!!.adapter = NumericWheelAdapter(1, 28)
                        maxItem = 28
                    }
                }
                if (wv_day!!.currentItem > maxItem - 1) {
                    wv_day!!.currentItem = maxItem - 1
                }
            }
        }

        wv_year!!.setOnItemSelectedListener(wheelListener_year)
        wv_month!!.setOnItemSelectedListener(wheelListener_month)

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        var textSize = 6
        when (type) {
            TimePickerView.Type.ALL -> textSize = textSize * 3
            TimePickerView.Type.YEAR_MONTH_DAY -> {
                textSize = textSize * 4
                wv_hours!!.visibility = View.GONE
                wv_mins!!.visibility = View.GONE
                wv_ampm!!.visibility = View.GONE
            }
            TimePickerView.Type.HOURS_MINS -> {
                textSize = textSize * 4
                wv_year!!.visibility = View.GONE
                wv_month!!.visibility = View.GONE
                wv_day!!.visibility = View.GONE
                wv_ampm!!.visibility = View.GONE
            }
            TimePickerView.Type.MONTH_DAY_HOUR_MIN -> {
                textSize = textSize * 3
                wv_year!!.visibility = View.GONE
                wv_ampm!!.visibility = View.GONE
            }
            TimePickerView.Type.YEAR_MONTH -> {
                textSize = textSize * 4
                wv_day!!.visibility = View.GONE
                wv_hours!!.visibility = View.GONE
                wv_mins!!.visibility = View.GONE
                wv_ampm!!.visibility = View.GONE
            }
            TimePickerView.Type.HOUR_MIN_SEC -> {
                textSize = textSize * 3
                wv_year!!.visibility = View.GONE
                wv_month!!.visibility = View.GONE
                wv_day!!.visibility = View.GONE
                wv_ampm!!.visibility = View.GONE
            }
            TimePickerView.Type.HOUR_MIN_APPM -> {
                textSize = textSize * 3
                wv_year!!.visibility = View.GONE
                wv_month!!.visibility = View.GONE
                wv_day!!.visibility = View.GONE
                wv_seconds!!.visibility = View.GONE
            }
        }
        wv_day!!.setTextSize(textSize.toFloat())
        wv_month!!.setTextSize(textSize.toFloat())
        wv_year!!.setTextSize(textSize.toFloat())
        wv_hours!!.setTextSize(textSize.toFloat())
        wv_mins!!.setTextSize(textSize.toFloat())
        wv_seconds!!.setTextSize(textSize.toFloat())
        wv_ampm!!.setTextSize(textSize.toFloat())
    }


    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    fun setCyclic(cyclic: Boolean) {
        wv_year!!.setCyclic(cyclic)
        wv_month!!.setCyclic(cyclic)
        wv_day!!.setCyclic(cyclic)
        wv_hours!!.setCyclic(cyclic)
        wv_mins!!.setCyclic(cyclic)
        wv_seconds!!.setCyclic(cyclic)
        wv_ampm!!.setCyclic(cyclic)
    }

    val time: String
        get() {
            val sb = StringBuffer()
            if (type === TimePickerView.Type.HOUR_MIN_SEC) {
                sb.append(wv_hours!!.currentItem).append(":")
                    .append(wv_mins!!.currentItem).append(wv_seconds!!.currentItem)
            } else if (type === TimePickerView.Type.HOUR_MIN_APPM) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                sb.append(calendar[Calendar.YEAR]).append("-")
                    .append(calendar[Calendar.MONTH] + 1).append("-")
                    .append(calendar[Calendar.DAY_OF_MONTH]).append(" ")
                    .append(wv_hours!!.currentItem).append(":")
                    .append(wv_mins!!.currentItem).append(" ").append(wv_ampm!!.currentItem)
            } else {
                sb.append(wv_year!!.currentItem + startYear).append("-")
                    .append(wv_month!!.currentItem + 1).append("-")
                    .append(wv_day!!.currentItem + 1).append(" ")
                    .append(wv_hours!!.currentItem).append(":")
                    .append(wv_mins!!.currentItem).append(wv_seconds!!.currentItem)
                    .append(wv_ampm!!.currentItem)
            }
            Log.v("get timee", sb.toString())
            return sb.toString()
        }



    fun setCustomTypeface(typeface: Typeface) {
        wv_ampm!!.setFont(typeface)
        wv_day!!.setFont(typeface)
        wv_hours!!.setFont(typeface)
        wv_mins!!.setFont(typeface)
        wv_month!!.setFont(typeface)
        wv_seconds!!.setFont(typeface)
        wv_year!!.setFont(typeface)
    }

    companion object {
        var dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("en"))
        const val DEFULT_START_YEAR = 1990
        const val DEFULT_END_YEAR = 2100
        var s_year = 0
        var s_month = 0
        var s_day = 0
        var s_h = 0
        var s_m = 0
        var s_s = 0
        var s_ampm = 0
    }
}