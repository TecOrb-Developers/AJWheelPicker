# AJWheelPicker
**OptionsPickerView** Like Ios Three Types of Options Pickers

- Single OptionPicker
- Double OptionPicker
- Triple OptionPicker
# Screen Shot
![This is an image](https://s4.aconvert.com/convert/p3r68-cdx67/ayic2-yb8ka.jpg)
![This is an image](https://s4.aconvert.com/convert/p3r68-cdx67/a3agx-3tnyn.jpg)
![This is an image](https://s4.aconvert.com/convert/p3r68-cdx67/aicii-57905.jpg)

#How to Use

        singleTVOptions:TextView = findViewById<View>(R.id.tvsingleOptions) as TextView

       singleTVOptions!!.setOnClickListener { singlePicker?.show() }

       MyOptionsPickerView singlePicker = MyOptionsPickerView(this@MainActivity)
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
