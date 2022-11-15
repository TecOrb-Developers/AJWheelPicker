package com.wheelPicker

import android.content.Context
import com.wheelPicker.view.BasePickerView
import com.wheelPicker.view.WheelOptions
import android.widget.TextView
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.wheelPicker.R
import com.wheelPicker.OptionsPickerView
import java.util.ArrayList

/**
 * Created by ajay on 11/11/22.
 */
class OptionsPickerView<T>(context: Context?, onOptionsSelectListener: OnOptionsSelectListener?) :
    BasePickerView(context!!), View.OnClickListener {
    var wheelOptions: WheelOptions<*>
    private val tvTitle: TextView
    private var optionsSelectListener: OnOptionsSelectListener? = null
    override fun setCustomFont(typeface: Typeface) {
        wheelOptions.setCustomTypeface(typeface)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.pickerview_options, contentContainer)
        // -----确定和取消按钮
        setBtnSubmit(findViewById(R.id.btnSubmit) as Button)
        btnSubmit!!.tag = TAG_SUBMIT
        setBtnCancel(findViewById(R.id.btnCancel) as Button)
        btnCancel!!.tag = TAG_CANCEL
        btnSubmit!!.setOnClickListener(this)
        btnCancel!!.setOnClickListener(this)
        //顶部标题
        tvTitle = findViewById(R.id.tvTitle) as TextView
        // ----转轮
        val optionspicker = findViewById(R.id.optionspicker)
        wheelOptions = WheelOptions<Any?>(optionspicker)
    }

  /*  fun setPicker(optionsItems: ArrayList<T>?) {
        wheelOptions.setPicker(optionsItems, null, null, false)
    }

    fun setPicker(
        options1Items: ArrayList<T>?,
        options2Items: ArrayList<ArrayList<T>?>?, linkage: Boolean
    ) {
        wheelOptions.setPicker(options1Items, options2Items, null, linkage)
    }

    fun setPicker(options1Items: ArrayList<T>?, options2Items: ArrayList<ArrayList<T>?>?, options3Items: ArrayList<ArrayList<ArrayList<T>?>?>?,
        linkage: Boolean) {
        wheelOptions.setPicker(options1Items, options2Items, options3Items, linkage)
    }*/

    /**
     * 设置选中的item位置
     * @param option1
     */
    fun setSelectOptions(option1: Int) {
        wheelOptions.setCurrentItems(option1, 0, 0)
    }

    /**
     * 设置选中的item位置
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
     * @param label1
     * @param label2
     * @param label3
     */
    fun setLabels(label1: String?, label2: String?, label3: String?) {
        wheelOptions.setLabels(label1, label2, label3)
    }

    /**
     * 设置是否循环滚动
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
                optionsSelectListener!!.onOptionsSelect(
                    optionsCurrentItems[0],
                    optionsCurrentItems[1],
                    optionsCurrentItems[2]
                )
            }
            dismiss()
            return
        }
    }

    interface OnOptionsSelectListener {
        fun onOptionsSelect(options1: Int, option2: Int, options3: Int)
    }

    fun setOnoptionsSelectListener(
        optionsSelectListener: OnOptionsSelectListener?
    ) {
        this.optionsSelectListener = optionsSelectListener
    }

    fun setTitle(title: String?) {
        tvTitle.text = title
    }

    companion object {
        private const val TAG_SUBMIT = "submit"
        private const val TAG_CANCEL = "cancel"
    }
}