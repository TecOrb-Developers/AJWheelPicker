package com.wheelPicker

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.wheelPicker.TimePickerView.OnTimeSelectListener
import com.wheelPicker.listener.OnOptionsSelectChangeListener
import java.util.*

class PickerOptions(buildType: Int) {
    @JvmField
    var optionsSelectListener: OptionsPickerView.OnOptionsSelectListener? = null
    var timeSelectListener: OnTimeSelectListener? = null

    @JvmField
    var cancelListener: View.OnClickListener? = null

    @JvmField
    var optionsSelectChangeListener: OnOptionsSelectChangeListener? = null

    //options picker
    @JvmField
    var label1: String? = null

    @JvmField
    var label2: String? = null

    @JvmField
    var label3 //单位字符
            : String? = null

    @JvmField
    var option1 = 0

    @JvmField
    var option2 = 0

    @JvmField
    var option3 //默认选中项
            = 0

    @JvmField
    var x_offset_one = 0

    @JvmField
    var x_offset_two = 0

    @JvmField
    var x_offset_three //x轴偏移量
            = 0

    @JvmField
    var cyclic1 = false //是否循环，默认否

    @JvmField
    var cyclic2 = false

    @JvmField
    var cyclic3 = false

    @JvmField
    var isRestoreItem = false //切换时，还原第一项

    //time picker
    var type = booleanArrayOf(true, true, true, false, false, false) //显示类型，默认显示： 年月日
    var date //当前选中时间
            : Calendar? = null
    var startDate //开始时间
            : Calendar? = null
    var endDate //终止时间
            : Calendar? = null
    var startYear //开始年份
            = 0
    var endYear //结尾年份
            = 0
    var cyclic = false //是否循环
    var isLunarCalendar = false //是否显示农历
    var label_year: String? = null
    var label_month: String? = null
    var label_day: String? = null
    var label_hours: String? = null
    var label_minutes: String? = null
    var label_seconds //单位
            : String? = null
    var x_offset_year = 0
    var x_offset_month = 0
    var x_offset_day = 0
    var x_offset_hours = 0
    var x_offset_minutes = 0
    var x_offset_seconds //单位
            = 0

    //******* general field ******//
    var layoutRes = 0

    @JvmField
    var decorView: ViewGroup? = null
    var textGravity = Gravity.CENTER

    @JvmField
    var context: Context? = null

    @JvmField
    var textContentConfirm //确定按钮文字
            : String? = null

    @JvmField
    var textContentCancel //取消按钮文字
            : String? = null

    @JvmField
    var textContentTitle //标题文字
            : String? = null

    @JvmField
    var textColorConfirm = PICKER_VIEW_BTN_COLOR_NORMAL //确定按钮颜色

    @JvmField
    var textColorCancel = PICKER_VIEW_BTN_COLOR_NORMAL //取消按钮颜色

    @JvmField
    var textColorTitle = PICKER_VIEW_COLOR_TITLE //标题颜色

    @JvmField
    var bgColorWheel = PICKER_VIEW_BG_COLOR_DEFAULT //滚轮背景颜色

    @JvmField
    var bgColorTitle = PICKER_VIEW_BG_COLOR_TITLE //标题背景颜色

    @JvmField
    var textSizeSubmitCancel = 17 //确定取消按钮大小

    @JvmField
    var textSizeTitle = 18 //标题文字大小

    @JvmField
    var textSizeContent = 18 //内容文字大小

    @JvmField
    var textColorOut = -0x575758 //分割线以外的文字颜色

    @JvmField
    var textColorCenter = -0xd5d5d6 //分割线之间的文字颜色
    var dividerColor = -0x2a2a2b //分割线的颜色

    @JvmField
    var outSideColor = -1 //显示时的外部背景色颜色,默认是灰色

    @JvmField
    var lineSpacingMultiplier = 1.6f // 条目间距倍数 默认1.6

    @JvmField
    var isDialog //是否是对话框模式
            = false

    @JvmField
    var cancelable = true //是否能取消

    @JvmField
    var isCenterLabel = false //是否只显示中间的label,默认每个item都显示

    @JvmField
    var font = Typeface.MONOSPACE //字体样式

    //public WheelView.DividerType dividerType = WheelView.DividerType.FILL;//分隔线类型
    @JvmField
    var itemsVisibleCount = 9 //最大可见条目数

    @JvmField
    var isAlphaGradient = false //透明度渐变

    init {
        layoutRes = if (buildType == TYPE_PICKER_OPTIONS) {
            R.layout.pickerview_options
        } else {
            R.layout.pickerview_time
        }
    }

    companion object {
        //constant
        private const val PICKER_VIEW_BTN_COLOR_NORMAL = -0xfa8201
        private const val PICKER_VIEW_BG_COLOR_TITLE = -0xa0a0b
        private const val PICKER_VIEW_COLOR_TITLE = -0x1000000
        private const val PICKER_VIEW_BG_COLOR_DEFAULT = -0x1
        const val TYPE_PICKER_OPTIONS = 1
        const val TYPE_PICKER_TIME = 2
    }
}