package com.wheelPicker

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.wheelPicker.view.BasePickerView
import com.wheelPicker.view.MyWheelOptions

class MyOptionsPickerView(context: Context?) : BasePickerView(context!!), View.OnClickListener {
    var wheelOptions: MyWheelOptions
    private val tvTitle: TextView

    private val tvMIn: TextView
    private val tvHour: TextView
    private val tvSpace: TextView
    private val linearLayoutHourMin: LinearLayout
    private val linearLayoutTopBarHight: ConstraintLayout
    private var optionsSelectListener: OnOptionsSelectListener? = null
    init {
        LayoutInflater.from(context).inflate(R.layout.pickerview_options, contentContainer)

        // -----确定和取消按钮
        setBtnSubmit(findViewById(R.id.btnSubmit) as Button)
        getBtnSubmit()!!.tag = TAG_SUBMIT
        setBtnCancel(findViewById(R.id.btnCancel) as Button)
        getBtnCancel()!!.tag = TAG_CANCEL
        getBtnSubmit()!!.setOnClickListener(this)
        getBtnCancel()!!.setOnClickListener(this)
        //顶部标题
        tvTitle = findViewById(R.id.tvTitle) as TextView
        btnCancel = findViewById(R.id.btnCancel) as Button
        btnSubmit = findViewById(R.id.btnSubmit) as Button
        tvMIn = findViewById(R.id.tvMin) as TextView
        tvHour = findViewById(R.id.tvHour) as TextView
        tvSpace = findViewById(R.id.tvSpace) as TextView
        linearLayoutTopBarHight = findViewById(R.id.pickerTop) as ConstraintLayout
        linearLayoutHourMin = findViewById(R.id.llHourMin) as LinearLayout

        // ----转轮
        val optionspicker = findViewById(R.id.optionspicker)
        wheelOptions = MyWheelOptions(optionspicker)
    }

    fun setPicker(optionsItems: ArrayList<String>) {
        wheelOptions.setPicker(optionsItems, null, null, false)
    }

    fun setPicker(options1Items: ArrayList<String>?, options2Items: ArrayList<String>?, linkage: Boolean) {
        wheelOptions.setPicker(options1Items, options2Items, null, linkage)
    }

    fun setPicker(
        options1Items: ArrayList<String>?,
        options2Items: ArrayList<String>?,
        options3Items: ArrayList<String>?,
        linkage: Boolean) {
        wheelOptions.setPicker(options1Items, options2Items, options3Items, linkage)
    }

    override fun setCustomFont(typeface: Typeface) {
        wheelOptions.setCustomTypeface(typeface)
    }

    /**
     * 设置选中的item位置
     * @param option1
     */
    fun setSelectOptions(option1: Int) {
        wheelOptions.setCurrentItems(option1, 0, 0)
    }

    /**
     * 设置选中的item位置
     *
     * @param option1
     * @param option2
     */
    fun setSelectOptions(option1: Int, option2: Int) {
        wheelOptions.setCurrentItems(option1, option2, 0)
    }

    /**
     * 设置选中的item位置
     * @param option1
     * @param option2
     * @param option3
     */
    fun setSelectOptions(option1: Int, option2: Int, option3: Int) {
        wheelOptions.setCurrentItems(option1, option2, option3)
    }

    /**
     * 设置选项的单位
     * @param label1
     */
    fun setLabels(label1: String?) {
        wheelOptions.setLabels(label1, null, null)
    }

    /**
     * 设置选项的单位
     * @param label1
     * @param label2
     */
    fun setLabels(label1: String?, label2: String?) {
        wheelOptions.setLabels(label1, label2, null)
    }

    /**
     * 设置选项的单位
     *
     * @param label1
     * @param label2
     * @param label3
     */
    fun setLabels(label1: String?, label2: String?, label3: String?) {
        wheelOptions.setLabels(label1, label2, label3)
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    fun setCyclic(cyclic: Boolean) {
        wheelOptions.setCyclic(cyclic)
    }

    fun setCyclic(cyclic1: Boolean, cyclic2: Boolean, cyclic3: Boolean) {
        wheelOptions.setCyclic(cyclic1, cyclic2, cyclic3)
    }

    override fun onClick(v: View) {
        val tag = v.tag as String
        if (tag == TAG_CANCEL) {
            dismiss()
            return
        } else {
            if (optionsSelectListener != null) {
                val optionsCurrentItems = wheelOptions.currentItems
                optionsSelectListener?.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2])
            }
            dismiss()
            return
        }
    }
    interface OnOptionsSelectListener {
        fun onOptionsSelect(options1: Int, option2: Int, options3: Int)
    }

    fun setOnoptionsSelectListener(optionsSelectListener: OnOptionsSelectListener?) {
        this.optionsSelectListener = optionsSelectListener
    }

    fun setTitle(title: String?) {
        tvTitle.text = title
    }

    fun setTvMinVisibility(isVisible: Boolean) {
        if (isVisible) linearLayoutHourMin.visibility =
            View.GONE else linearLayoutHourMin.visibility = View.VISIBLE
    }

    fun setTvHourVisibility(isVisible: Boolean) {
        if (isVisible) linearLayoutHourMin.visibility =
            View.GONE else linearLayoutHourMin.visibility = View.VISIBLE
    }

    fun setTvSpaceVisibility(isVisible: Boolean) {
        if (isVisible) linearLayoutHourMin.visibility =
            View.GONE else linearLayoutHourMin.visibility = View.VISIBLE
    }

    fun setLinearLayoutTopBarHight() {
// Gets the layout params that will allow you to resize the layout
        val params = linearLayoutTopBarHight.layoutParams
        // Changes the height and width to the specified *pixels*
        params.height = 90
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        linearLayoutTopBarHight.layoutParams = params
    }

    fun setTitleTypeFace(titleTypeFace: Typeface?) {
        tvTitle.typeface = titleTypeFace
    }

    fun setCancelButtonTypeFace(titleTypeFace: Typeface?) {
        btnCancel?.typeface = titleTypeFace
        wheelOptions.setCustomTypeface(titleTypeFace!!)
    }

    override fun setCancelButtonTextSize(textSize: Float) {
        btnCancel?.textSize = textSize
    }

    override fun setCancelButtonTextColor(color: Int) {
        btnCancel?.setTextColor(color)
    }

    fun setSubmitButtonTypeFace(titleTypeFace: Typeface?) {
        btnSubmit?.typeface = titleTypeFace
    }

    override fun setSubmitButtonTextSize(textSize: Float) {
        btnSubmit?.textSize = textSize
    }

    override fun setSubmitButtonTextColor(color: Int) {
        btnSubmit?.setTextColor(color)
    }

    fun setTitleTextSize(textSize: Float) {
        tvTitle.textSize = textSize
    }

    fun setTitleTextColor(color: Int) {
        tvTitle.setTextColor(color)
    }

    // options font size
    fun setOption1FontSize(textSizeOption1: Float){
        wheelOptions!!.setOption1FontSize(textSizeOption1)
    }
    fun setOption2FontSize(textSizeOption2: Float){
        wheelOptions!!.setOption2FontSize(textSizeOption2)
    }
    fun setOption3FontSize(textSizeOption3: Float) {
        wheelOptions!!.setOption3FontSize(textSizeOption3)
    }

    companion object {
        private const val TAG_SUBMIT = "submit"
        private const val TAG_CANCEL = "cancel"
    }
}