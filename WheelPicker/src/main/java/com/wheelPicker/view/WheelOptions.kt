package com.wheelPicker.view

import android.graphics.Typeface
import android.view.View
import com.wheelPicker.R
import com.wheelPicker.adapter.ArrayWheelAdapter
import com.wheelPicker.lib.WheelView
import com.wheelPicker.listener.OnItemSelectedListener

class WheelOptions<T>(var view: View) {
    private var wv_option1: WheelView? = null
    private var wv_option2: WheelView? = null
    private var wv_option3: WheelView? = null
    private var mOptions1Items: ArrayList<T>? = null
    private var mOptions2Items: ArrayList<ArrayList<T>>? = null
    private var mOptions3Items: ArrayList<ArrayList<ArrayList<T>>>? = null
    private var linkage = false
    private var wheelListener_option1: OnItemSelectedListener? = null
    private var wheelListener_option2: OnItemSelectedListener? = null

    init {
        view = view
    }

    fun setPicker(optionsItems: ArrayList<T>?) {
        setPicker(optionsItems, null, null, false)
    }

    fun setPicker(
        options1Items: ArrayList<T>?,
        options2Items: ArrayList<ArrayList<T>>?, linkage: Boolean
    ) {
        setPicker(options1Items, options2Items, null, linkage)
    }

    fun setPicker(
        options1Items: ArrayList<T>?, options2Items: ArrayList<ArrayList<T>>?, options3Items: ArrayList<ArrayList<ArrayList<T>>>?,
        linkage: Boolean) {
        this.linkage = linkage
        mOptions1Items = options1Items
        mOptions2Items = options2Items
        mOptions3Items = options3Items
        var len = ArrayWheelAdapter.DEFAULT_LENGTH
        if (mOptions3Items == null) len = 8
        if (mOptions2Items == null) len = 12
        // 选项1
        wv_option1 = view.findViewById<View>(R.id.options1) as WheelView
        wv_option1!!.adapter = ArrayWheelAdapter(mOptions1Items!!, len) // 设置显示数据
        wv_option1!!.currentItem = 0 // 初始化时显示的数据
        // 选项2
        wv_option2 = view.findViewById<View>(R.id.options2) as WheelView
        if (mOptions2Items != null) wv_option2!!.adapter =
            ArrayWheelAdapter(mOptions2Items!!.get(0)) // 设置显示数据
        wv_option2!!.currentItem = wv_option1!!.currentItem // 初始化时显示的数据
        // 选项3
        wv_option3 = view.findViewById<View>(R.id.options3) as WheelView
        if (mOptions3Items != null) wv_option3!!.adapter = ArrayWheelAdapter(mOptions3Items!!.get(0)[0]
        ) // 设置显示数据
        wv_option3!!.currentItem = wv_option3!!.currentItem // 初始化时显示的数据
        val textSize = 25
        wv_option1!!.setTextSize(textSize.toFloat())
        wv_option2!!.setTextSize(textSize.toFloat())
        wv_option3!!.setTextSize(textSize.toFloat())
        if (mOptions2Items == null) wv_option2!!.visibility = View.GONE
        if (mOptions3Items == null) wv_option3!!.visibility = View.GONE

        // 联动监听器
        val wheelListener_option1 = object :OnItemSelectedListener {
            override fun onItemSelected(index: Int) {
                var opt2Select = 0
                if (mOptions2Items != null) {
                    opt2Select = wv_option2!!.currentItem //上一个opt2的选中位置
                    //新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                    opt2Select =
                        if (opt2Select >= mOptions2Items!!.get(index).size - 1) mOptions2Items!!.get(index).size - 1 else opt2Select
                    wv_option2!!.adapter = ArrayWheelAdapter(
                        mOptions2Items!!.get(index)
                    )
                    wv_option2!!.currentItem = opt2Select
                }
                if (mOptions3Items != null) {
                    wheelListener_option2!!.onItemSelected(opt2Select)
                }
            }
        }
        /*wheelListener_option1 = OnItemSelectedListener { index ->
            var opt2Select = 0
            if (mOptions2Items != null) {
                opt2Select = wv_option2!!.currentItem //上一个opt2的选中位置
                //新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                opt2Select =
                    if (opt2Select >= mOptions2Items!!.get(index).size - 1) mOptions2Items!!.get(index).size - 1 else opt2Select
                wv_option2!!.adapter = ArrayWheelAdapter(
                    mOptions2Items!!.get(index)
                )
                wv_option2!!.currentItem = opt2Select
            }
            if (mOptions3Items != null) {
                wheelListener_option2!!.onItemSelected(opt2Select)
            }
        }*/

        val wheelListener_option2 = object :OnItemSelectedListener {
            override fun onItemSelected(index: Int) {
                var index = index
                if (mOptions3Items != null) {
                    var opt1Select = wv_option1!!.currentItem
                    opt1Select =
                        if (opt1Select >= mOptions3Items!!.size - 1) mOptions3Items!!.size - 1 else opt1Select
                    index =
                        if (index >= mOptions2Items!![opt1Select].size - 1) mOptions2Items!![opt1Select].size - 1 else index
                    var opt3 = wv_option3!!.currentItem //上一个opt3的选中位置
                    //新opt3的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                    opt3 =
                        if (opt3 >= mOptions3Items!!.get(opt1Select)[index].size - 1) mOptions3Items!!.get(
                            opt1Select
                        )[index].size - 1 else opt3
                    wv_option3!!.adapter = ArrayWheelAdapter(
                        mOptions3Items!!.get(wv_option1!!.currentItem)[index]
                    )
                    wv_option3!!.currentItem = opt3
                }
            }
        }
        /*wheelListener_option2 = OnItemSelectedListener { index ->
            var index = index
            if (mOptions3Items != null) {
                var opt1Select = wv_option1!!.currentItem
                opt1Select =
                    if (opt1Select >= mOptions3Items!!.size - 1) mOptions3Items!!.size - 1 else opt1Select
                index =
                    if (index >= mOptions2Items!![opt1Select].size - 1) mOptions2Items!![opt1Select].size - 1 else index
                var opt3 = wv_option3!!.currentItem //上一个opt3的选中位置
                //新opt3的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                opt3 =
                    if (opt3 >= mOptions3Items!!.get(opt1Select)[index].size - 1) mOptions3Items!!.get(
                        opt1Select
                    )[index].size - 1 else opt3
                wv_option3!!.adapter = ArrayWheelAdapter(
                    mOptions3Items!!.get(wv_option1!!.currentItem)[index]
                )
                wv_option3!!.currentItem = opt3
            }
        }*/

//		// 添加联动监听
        if (options2Items != null && linkage) wv_option1!!.setOnItemSelectedListener(
            wheelListener_option1
        )
        if (options3Items != null && linkage) wv_option2!!.setOnItemSelectedListener(
            wheelListener_option2
        )
    }

    /**
     * 设置选项的单位
     *
     * @param label1
     * @param label2
     * @param label3
     */
    fun setLabels(label1: String?, label2: String?, label3: String?) {
        if (label1 != null) wv_option1!!.setLabel(label1)
        if (label2 != null) wv_option2!!.setLabel(label2)
        if (label3 != null) wv_option3!!.setLabel(label3)
    }

    /**
     * 设置是否循环滚动
     *
     * @param cyclic
     */
    fun setCyclic(cyclic: Boolean) {
        wv_option1!!.setCyclic(cyclic)
        wv_option2!!.setCyclic(cyclic)
        wv_option3!!.setCyclic(cyclic)
    }

    /**
     * 分别设置第一二三级是否循环滚动
     *
     * @param cyclic1,cyclic2,cyclic3
     */
    fun setCyclic(cyclic1: Boolean, cyclic2: Boolean, cyclic3: Boolean) {
        wv_option1!!.setCyclic(cyclic1)
        wv_option2!!.setCyclic(cyclic2)
        wv_option3!!.setCyclic(cyclic3)
    }

    /**
     * 设置第二级是否循环滚动
     *
     * @param cyclic
     */
    fun setOption2Cyclic(cyclic: Boolean) {
        wv_option2!!.setCyclic(cyclic)
    }

    /**
     * 设置第三级是否循环滚动
     *
     * @param cyclic
     */
    fun setOption3Cyclic(cyclic: Boolean) {
        wv_option3!!.setCyclic(cyclic)
    }

    /**
     * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
     *
     * @return
     */
    val currentItems: IntArray
        get() {
            val currentItems = IntArray(3)
            currentItems[0] = wv_option1!!.currentItem
            currentItems[1] = wv_option2!!.currentItem
            currentItems[2] = wv_option3!!.currentItem
            return currentItems
        }

    fun setCurrentItems(option1: Int, option2: Int, option3: Int) {
        if (linkage) {
            itemSelected(option1, option2, option3)
        }
        wv_option1!!.currentItem = option1
        wv_option2!!.currentItem = option2
        wv_option3!!.currentItem = option3
    }

    private fun itemSelected(opt1Select: Int, opt2Select: Int, opt3Select: Int) {
        if (mOptions2Items != null) {
            wv_option2!!.adapter = ArrayWheelAdapter(
                mOptions2Items!![opt1Select]
            )
            wv_option2!!.currentItem = opt2Select
        }
        if (mOptions3Items != null) {
            wv_option3!!.adapter = ArrayWheelAdapter(
                mOptions3Items!![opt1Select][opt2Select]
            )
            wv_option3!!.currentItem = opt3Select
        }
    }

    fun setCustomTypeface(typeface: Typeface) {
        wv_option1!!.setFont(typeface)
        wv_option2!!.setFont(typeface)
        wv_option3!!.setFont(typeface)
    }

    fun setOption1FontSize(textSizeOption1: Float){
        wv_option1!!.setTextSize(textSizeOption1)
    }
    fun setOption2FontSize(textSizeOption2: Float){
        wv_option2!!.setTextSize(textSizeOption2)
    }
    fun setOption3FontSize(textSizeOption3: Float) {
        wv_option3!!.setTextSize(textSizeOption3)
    }
}