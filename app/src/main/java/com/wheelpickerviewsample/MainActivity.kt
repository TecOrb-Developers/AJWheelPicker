package com.wheelpickerviewsample

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.wheelPicker.MyOptionsPickerView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var singleTVOptions: TextView? = null
    private var twoTVOptions: TextView? = null
    private var threeTVOptions: TextView? = null
    var singlePicker: MyOptionsPickerView? = null
    var twoPicker: MyOptionsPickerView? = null
    var threePicker: MyOptionsPickerView? = null
    var vMasker: View? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        singleTVOptions = findViewById<View>(R.id.tvsingleOptions) as TextView
        twoTVOptions = findViewById<View>(R.id.tvTwoOptions) as TextView
        threeTVOptions = findViewById<View>(R.id.tvThreeOptions) as TextView
        singleTVOptions!!.setOnClickListener(this)
        twoTVOptions!!.setOnClickListener(this)
        threeTVOptions!!.setOnClickListener(this)

        //Single String Picker
        singlePicker = MyOptionsPickerView(this@MainActivity)
        val items = ArrayList<String>()
        items.add("A")
        items.add("B")
        items.add("C")
        items.add("D")
        items.add("E")
        items.add("F")
        items.add("G")
        items.add("H")
        items.add("I")
        items.add("J")
        items.add("K")
        singlePicker?.setPicker(items)
        singlePicker?.setTitle("")
        singlePicker?.setTitleTypeFace(ResourcesCompat.getFont(this, R.font.poppins_semi_bold))
        singlePicker?.setCyclic(true)
        singlePicker?.setTitleTextSize(18f)
        singlePicker?.setCancelButtonTextSize(16f)
        singlePicker?.setOption1FontSize(22f)
        singlePicker?.setOption2FontSize(22f)
        singlePicker?.setOption2FontSize(22f)
        singlePicker?.setSubmitButtonTextSize(16f)
        singlePicker?.setSelectOptions(0)
        singlePicker?.setTitleTypeFace(ResourcesCompat.getFont(this, R.font.poppins_regular))
        singlePicker?.setTitleTextColor(getColor(com.wheelPicker.R.color.colorBlue))
        singlePicker?.setSubmitButtonTextColor(R.color.black)
        singlePicker?.setCancelButtonTextColor(resources.getColor(com.wheelPicker.R.color.colorBlue))
        singlePicker?.setSubmitButtonTypeFace(ResourcesCompat.getFont(this, R.font.poppins_regular))
        singlePicker?.setCustomFont(ResourcesCompat.getFont(this, R.font.poppins_medium)!!)

        singlePicker?.setOnoptionsSelectListener(object :MyOptionsPickerView.OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, option2: Int, options3: Int) {
                Toast.makeText(this@MainActivity, "" + items[options1], Toast.LENGTH_SHORT).show()
            }
        })
        singleTVOptions!!.setOnClickListener { singlePicker?.show() }


        //Two Options PickerView
        twoPicker = MyOptionsPickerView(this@MainActivity)
        val twoItemsOptions1 = ArrayList<String>()
        twoItemsOptions1.add("AA")
        twoItemsOptions1.add("BB")
        twoItemsOptions1.add("CC")
        twoItemsOptions1.add("DD")
        twoItemsOptions1.add("EE")
        val twoItemsOptions2 = ArrayList<String>()
        twoItemsOptions2.add("00")
        twoItemsOptions2.add("11")
        twoItemsOptions2.add("22")
        twoItemsOptions2.add("33")
        twoItemsOptions2.add("44")
        twoPicker?.setPicker(twoItemsOptions1, twoItemsOptions2, false)
        twoPicker?.setTitle("")
        twoPicker?.setOption1FontSize(22f)
        twoPicker?.setOption2FontSize(22f)
        twoPicker?.setOption2FontSize(22f)
        twoPicker?.setCyclic(false, false, false)
        twoPicker?.setSelectOptions(0, 0)
        twoPicker?.setOnoptionsSelectListener(object :MyOptionsPickerView.OnOptionsSelectListener{
            override fun onOptionsSelect(options1: Int, option2: Int, options3: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "" + twoItemsOptions1[options1] + " " + twoItemsOptions2[option2],
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        twoTVOptions!!.setOnClickListener { twoPicker?.show() }

        //Three Options PickerView
        threePicker = MyOptionsPickerView(this@MainActivity)
        val threeItemsOptions1 = ArrayList<String>()
        threeItemsOptions1.add("AA")
        threeItemsOptions1.add("BB")
        threeItemsOptions1.add("CC")
        threeItemsOptions1.add("DD")
        threeItemsOptions1.add("EE")
        val threeItemsOptions2 = ArrayList<String>()
        threeItemsOptions2.add("00")
        threeItemsOptions2.add("11")
        threeItemsOptions2.add("22")
        threeItemsOptions2.add("33")
        threeItemsOptions2.add("44")
        val threeItemsOptions3 = ArrayList<String>()
        threeItemsOptions3.add("FF")
        threeItemsOptions3.add("GG")
        threeItemsOptions3.add("HH")
        threeItemsOptions3.add("II")
        threeItemsOptions3.add("JJ")
        threePicker?.setPicker(threeItemsOptions1, threeItemsOptions2, threeItemsOptions3, false)
        threePicker?.setTitle("")
        threePicker?.setOption1FontSize(22f)
        threePicker?.setOption2FontSize(22f)
        threePicker?.setOption2FontSize(22f)
        threePicker?.setCyclic(false, false, false)
        threePicker?.setSelectOptions(0, 0, 0)

        threePicker?.setOnoptionsSelectListener(object :MyOptionsPickerView.OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, option2: Int, options3: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "" + threeItemsOptions1[options1] + " " + threeItemsOptions2[option2] + " " + threeItemsOptions3[options3],
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        threeTVOptions!!.setOnClickListener { threePicker?.show() }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvsingleOptions -> singlePicker!!.show()
            R.id.tvTwoOptions -> twoPicker!!.show()
            R.id.tvThreeOptions -> threePicker!!.show()
        }
    }

    companion object {
        fun getTime(date: Date?): String {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
            return format.format(date)
        }
    }
}