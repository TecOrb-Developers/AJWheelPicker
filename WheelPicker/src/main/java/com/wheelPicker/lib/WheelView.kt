package com.wheelPicker.lib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import com.wheelPicker.R
import com.wheelPicker.adapter.WheelAdapter
import com.wheelPicker.listener.OnItemSelectedListener
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * 3d滚轮控件
 */
class WheelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {
    enum class ACTION {
        // 点击，滑翔(滑到尽头)，拖拽事件
        CLICK, FLING, DAGGLE
    }
    var mContext: Context? = null
    var mHandler: Handler? = null
    private var gestureDetector: GestureDetector? = null
    var onItemSelectedListener: OnItemSelectedListener? = null

    // Timer mTimer;
    var mExecutor = Executors.newSingleThreadScheduledExecutor()
    private var mFuture: ScheduledFuture<*>? = null
    var paintOuterText: Paint? = null
    var paintCenterText: Paint? = null
    var paintIndicator: Paint? = null
    var adapter: WheelAdapter<*>? = null
    private var label //附加单位
            : String? = null
    var textSize //选项的文字大小
            : Int
    var customTextSize //自定义文字大小，为true则用于使setTextSize函数无效，只能通过xml修改
            : Boolean
    var maxTextWidth = 0
    var maxTextHeight = 0
    var itemHeight //每行高度
            = 0f
    var textColorOut: Int
    var textColorCenter: Int
    var dividerColor: Int
    var isLoop = false

    // 第一条线Y坐标值
    var firstLineY = 0f

    //第二条线Y坐标
    var secondLineY = 0f

    //中间Y坐标
    var centerY = 0f

    //滚动总高度y值
    var totalScrollY = 0

    //初始化默认选中第几个
    var initPosition = 0

    //选中的Item是第几个
    private var selectedItem = 0
    var preCurrentIndex = 0

    //滚动偏移值,用于记录滚动了多少个item
    var change = 0

    // 显示几个条目
    var itemsVisible = 11
    var cMeasuredHeight = 0
    var cMeasuredWidth= 0

    // 半圆周长
    var halfCircumference = 0

    // 半径
    var radius = 0
    private var mOffset = 0
    private var previousY = 0f
    var startTime: Long = 0
    var widthMeasureSpec = 0
    private var mGravity = Gravity.CENTER
    private var drawCenterContentStart = 0 //中间选中文字开始绘制位置
    private var drawOutContentStart = 0 //非中间文字开始绘制位置

    init {
        textColorOut = resources.getColor(R.color.pickerview_wheelview_textcolor_out)
        textColorCenter = resources.getColor(R.color.pickerview_wheelview_textcolor_center)
        dividerColor = resources.getColor(R.color.pickerview_wheelview_textcolor_divider)
        //配合customTextSize使用，customTextSize为true才会发挥效果
        textSize = resources.getDimensionPixelSize(R.dimen.pickerview_textsize)
        customTextSize = resources.getBoolean(R.bool.pickerview_customTextSize)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.wheelview, 0, 0)
            mGravity = a.getInt(R.styleable.wheelview_gravity, Gravity.CENTER)
            textColorOut = a.getColor(R.styleable.wheelview_textColorOut, textColorOut)
            textColorCenter = a.getColor(R.styleable.wheelview_textColorCenter, textColorCenter)
            dividerColor = a.getColor(R.styleable.wheelview_dividerColor, dividerColor)
            textSize = a.getDimensionPixelOffset(R.styleable.wheelview_textSize, textSize)
        }
        initLoopView(context)
    }

    private fun initLoopView(context: Context) {
        this.mContext = context
        mHandler = MessageHandler(this)
        gestureDetector = GestureDetector(context, LoopViewGestureListener(this))
        gestureDetector!!.setIsLongpressEnabled(false)
        isLoop = true
        totalScrollY = 0
        initPosition = -1
        initPaints()
    }

    private fun initPaints() {
        paintOuterText = Paint()
        paintOuterText!!.color = textColorOut
        paintOuterText!!.isAntiAlias = true
        paintOuterText!!.typeface = Typeface.MONOSPACE
        paintOuterText!!.textSize = textSize.toFloat()
        paintCenterText = Paint()
        paintCenterText!!.color = textColorCenter
        paintCenterText!!.isAntiAlias = true
        paintCenterText!!.textScaleX = 1.1f
        paintCenterText!!.typeface = Typeface.MONOSPACE
        paintCenterText!!.textSize = textSize.toFloat()
        paintIndicator = Paint()
        paintIndicator!!.color = dividerColor
        paintIndicator!!.isAntiAlias = true
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }
    }

    private fun remeasure() {
        if (adapter == null) {
            return
        }
        measureTextWidthHeight()

        //最大Text的高度乘间距倍数得到 可见文字实际的总高度，半圆的周长
        halfCircumference = (itemHeight * (itemsVisible - 1)).toInt()
        //整个圆的周长除以PI得到直径，这个直径用作控件的总高度
        cMeasuredHeight = (halfCircumference * 2 / Math.PI).toInt()
        //求出半径
        radius = (halfCircumference / Math.PI).toInt()
        //控件宽度，这里支持weight
        cMeasuredWidth = MeasureSpec.getSize(widthMeasureSpec)
        //计算两条横线和控件中间点的Y位置
        firstLineY = (cMeasuredHeight - itemHeight) / 2.0f
        secondLineY = (cMeasuredHeight + itemHeight) / 2.0f
        centerY = (cMeasuredHeight + maxTextHeight) / 2.0f - CENTERCONTENTOFFSET
        //初始化显示的item的position，根据是否loop
        if (initPosition == -1) {
            initPosition = if (isLoop) {
                (adapter!!.itemsCount + 1) / 2
            } else {
                0
            }
        }
        preCurrentIndex = initPosition
    }

    /**
     * 计算最大len的Text的宽高度
     */
    private fun measureTextWidthHeight() {
        val rect = Rect()
        for (i in 0 until adapter!!.itemsCount) {
            val s1 = getContentText(adapter!!.getItem(i))
            paintCenterText!!.getTextBounds(s1, 0, s1.length, rect)
            val textWidth = rect.width()
            if (textWidth > maxTextWidth) {
                maxTextWidth = textWidth
            }
            paintCenterText!!.getTextBounds("\u661F\u671F", 0, 2, rect) // 星期
            val textHeight = rect.height()
            if (textHeight > maxTextHeight) {
                maxTextHeight = textHeight
            }
        }
        itemHeight = lineSpacingMultiplier * maxTextHeight
    }

    fun smoothScroll(action: ACTION) {
        cancelFuture()
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            mOffset = ((totalScrollY % itemHeight + itemHeight) % itemHeight).toInt()
            mOffset = if (mOffset.toFloat() > itemHeight / 2.0f) {
                (itemHeight - mOffset.toFloat()).toInt()
            } else {
                -mOffset
            }
        }
        //停止的时候，位置有偏移，不是全部都能正确停止到中间位置的，这里把文字位置挪回中间去
        mFuture = mExecutor.scheduleWithFixedDelay(
            SmoothScrollTimerTask(this, mOffset),
            0,
            10,
            TimeUnit.MILLISECONDS
        )
    }

    fun scrollBy(velocityY: Float) {
        cancelFuture()
        mFuture = mExecutor.scheduleWithFixedDelay(
            InertiaTimerTask(this, velocityY),
            0,
            VELOCITYFLING.toLong(),
            TimeUnit.MILLISECONDS
        )
    }

    fun cancelFuture() {
        if (mFuture != null && !mFuture!!.isCancelled) {
            mFuture!!.cancel(true)
            mFuture = null
        }
    }

    /**
     * 设置是否循环滚动
     * @param cyclic
     */
    fun setCyclic(cyclic: Boolean) {
        isLoop = cyclic
    }

    fun setTextSize(size: Float) {
        if (size > 0.0f && !customTextSize) {
            textSize = (context!!.resources.displayMetrics.density * size).toInt()
            paintOuterText!!.textSize = textSize.toFloat()
            paintCenterText!!.textSize = textSize.toFloat()
        }
    }

    fun setFont(typeface: Typeface) {
        paintCenterText!!.typeface = typeface
        paintOuterText!!.typeface = typeface
    }

    @JvmName("setOnItemSelectedListener1")
    fun setOnItemSelectedListener(OnItemSelectedListener: OnItemSelectedListener?) {
        onItemSelectedListener = OnItemSelectedListener
    }

    @JvmName("setAdapter1")
    fun setAdapter(adapter: WheelAdapter<*>?) {
        this.adapter = adapter
        remeasure()
        invalidate()
    }

    @JvmName("getAdapter1")
    fun getAdapter(): WheelAdapter<*>? {
        return adapter
    }

    //回归顶部，不然重设setCurrentItem的话位置会偏移的，就会显示出不对位置的数据
    var currentItem: Int
        get() = selectedItem
        set(currentItem) {
            initPosition = currentItem
            totalScrollY = 0 //回归顶部，不然重设setCurrentItem的话位置会偏移的，就会显示出不对位置的数据
            invalidate()
        }

    fun onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(OnItemSelectedRunnable(this), 200L)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (adapter == null) {
            return
        }
        //可见的item数组
        val visibles = arrayOfNulls<Any>(itemsVisible)
        //滚动的Y值高度除去每行Item的高度，得到滚动了多少个item，即change数
        change = (totalScrollY / itemHeight).toInt()
        try {
            //滚动中实际的预选中的item(即经过了中间位置的item) ＝ 滑动前的位置 ＋ 滑动相对位置
            preCurrentIndex = initPosition + change % adapter!!.itemsCount
        } catch (e: ArithmeticException) {
            println("出错了！adapter.getItemsCount() == 0，联动数据不匹配")
        }
        if (!isLoop) { //不循环的情况
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0
            }
            if (preCurrentIndex > adapter!!.itemsCount - 1) {
                preCurrentIndex = adapter!!.itemsCount - 1
            }
        } else { //循环
            if (preCurrentIndex < 0) { //举个例子：如果总数是5，preCurrentIndex ＝ －1，那么preCurrentIndex按循环来说，其实是0的上面，也就是4的位置
                preCurrentIndex = adapter!!.itemsCount + preCurrentIndex
            }
            if (preCurrentIndex > adapter!!.itemsCount - 1) { //同理上面,自己脑补一下
                preCurrentIndex = preCurrentIndex - adapter!!.itemsCount
            }
        }

        //跟滚动流畅度有关，总滑动距离与每个item高度取余，即并不是一格格的滚动，每个item不一定滚到对应Rect里的，这个item对应格子的偏移值
        val itemHeightOffset = (totalScrollY % itemHeight).toInt()
        // 设置数组中每个元素的值
        var counter = 0
        while (counter < itemsVisible) {
            var index =
                preCurrentIndex - (itemsVisible / 2 - counter) //索引值，即当前在控件中间的item看作数据源的中间，计算出相对源数据源的index值

            //判断是否循环，如果是循环数据源也使用相对循环的position获取对应的item值，如果不是循环则超出数据源范围使用""空白字符串填充，在界面上形成空白无数据的item项
            if (isLoop) {
                index = getLoopMappingIndex(index)
                visibles[counter] = adapter!!.getItem(index)
            } else if (index < 0) {
                visibles[counter] = ""
            } else if (index > adapter!!.itemsCount - 1) {
                visibles[counter] = ""
            } else {
                visibles[counter] = adapter!!.getItem(index)
            }
            counter++
        }

        //中间两条横线
        canvas.drawLine(0.0f, firstLineY, cMeasuredWidth.toFloat(), firstLineY, paintIndicator!!)
        canvas.drawLine(0.0f, secondLineY, cMeasuredWidth.toFloat(), secondLineY, paintIndicator!!)
        //单位的Label
        if (label != null) {
            val drawRightContentStart = cMeasuredWidth - getTextWidth(paintCenterText, label)
            //靠右并留出空隙
            canvas.drawText(
                label!!,
                drawRightContentStart - CENTERCONTENTOFFSET,
                centerY,
                paintCenterText!!
            )
        }
        counter = 0
        while (counter < itemsVisible) {
            canvas.save()
            // L(弧长)=α（弧度）* r(半径) （弧度制）
            // 求弧度--> (L * π ) / (π * r)   (弧长X派/半圆周长)
            val itemHeight = maxTextHeight * lineSpacingMultiplier
            val radian = (itemHeight * counter - itemHeightOffset) * Math.PI / halfCircumference
            // 弧度转换成角度(把半圆以Y轴为轴心向右转90度，使其处于第一象限及第四象限
            val angle = (90.0 - radian / Math.PI * 180.0).toFloat()
            // 九十度以上的不绘制
            if (angle >= 90f || angle <= -90f) {
                canvas.restore()
            } else {
                val contentText = getContentText(visibles[counter])

                //计算开始绘制的位置
                measuredCenterContentStart(contentText)
                measuredOutContentStart(contentText)
                val translateY =
                    (radius - Math.cos(radian) * radius - Math.sin(radian) * maxTextHeight / 2.0).toFloat()
                //根据Math.sin(radian)来更改canvas坐标系原点，然后缩放画布，使得文字高度进行缩放，形成弧形3d视觉差
                canvas.translate(0.0f, translateY)
                canvas.scale(1.0f, Math.sin(radian).toFloat())
                if (translateY <= firstLineY && maxTextHeight + translateY >= firstLineY) {
                    // 条目经过第一条线
                    canvas.save()
                    canvas.clipRect(0f, 0f, cMeasuredWidth.toFloat(), firstLineY - translateY)
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * SCALECONTENT)
                    canvas.drawText(
                        contentText,
                        drawOutContentStart.toFloat(),
                        maxTextHeight.toFloat(),
                        paintOuterText!!
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(
                        0f,
                        firstLineY - translateY,
                        cMeasuredWidth.toFloat(),
                        itemHeight.toInt().toFloat()
                    )
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * 1f)
                    canvas.drawText(
                        contentText,
                        drawCenterContentStart.toFloat(),
                        maxTextHeight - CENTERCONTENTOFFSET,
                        paintCenterText!!
                    )
                    canvas.restore()
                } else if (translateY <= secondLineY && maxTextHeight + translateY >= secondLineY) {
                    // 条目经过第二条线
                    canvas.save()
                    canvas.clipRect(0f, 0f, cMeasuredWidth.toFloat(), secondLineY - translateY)
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * 1.0f)
                    canvas.drawText(
                        contentText,
                        drawCenterContentStart.toFloat(),
                        maxTextHeight - CENTERCONTENTOFFSET,
                        paintCenterText!!
                    )
                    canvas.restore()
                    canvas.save()
                    canvas.clipRect(
                        0f,
                        secondLineY - translateY,
                        cMeasuredWidth.toFloat(),
                        itemHeight.toInt().toFloat()
                    )
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * SCALECONTENT)
                    canvas.drawText(
                        contentText,
                        drawOutContentStart.toFloat(),
                        maxTextHeight.toFloat(),
                        paintOuterText!!
                    )
                    canvas.restore()
                } else if (translateY >= firstLineY && maxTextHeight + translateY <= secondLineY) {
                    // 中间条目
                    canvas.clipRect(0, 0, cMeasuredWidth, itemHeight.toInt())
                    canvas.drawText(
                        contentText,
                        drawCenterContentStart.toFloat(),
                        maxTextHeight - CENTERCONTENTOFFSET,
                        paintCenterText!!
                    )
                    val preSelectedItem = adapter!!.indexOf(visibles[counter])
                    if (preSelectedItem != -1) {
                        selectedItem = preSelectedItem
                    }
                } else {
                    // 其他条目
                    canvas.save()
                    canvas.clipRect(0, 0, cMeasuredWidth, itemHeight.toInt())
                    canvas.scale(1.0f, Math.sin(radian).toFloat() * SCALECONTENT)
                    canvas.drawText(
                        contentText,
                        drawOutContentStart.toFloat(),
                        maxTextHeight.toFloat(),
                        paintOuterText!!
                    )
                    canvas.restore()
                }
                canvas.restore()
            }
            counter++
        }
    }

    //递归计算出对应的index
    private fun getLoopMappingIndex(index: Int): Int {
        var index = index
        if (index < 0) {
            index = index + adapter!!.itemsCount
            index = getLoopMappingIndex(index)
        } else if (index > adapter!!.itemsCount - 1) {
            index = index - adapter!!.itemsCount
            index = getLoopMappingIndex(index)
        }
        return index
    }

    /**
     * 根据传进来的对象反射出getPickerViewText()方法，来获取需要显示的值
     * @param item
     * @return
     */
    private fun getContentText(item: Any?): String {
        var contentText = item.toString()
        try {
            val clz: Class<*> = item!!.javaClass
            val m = clz.getMethod(GETPICKERVIEWTEXT)
            contentText = m.invoke(item, *arrayOfNulls(0)).toString()
        } catch (e: NoSuchMethodException) {
        } catch (e: InvocationTargetException) {
        } catch (e: IllegalAccessException) {
        } catch (e: Exception) {
        }
        return contentText
    }

    private fun measuredCenterContentStart(content: String) {
        val rect = Rect()
        paintCenterText!!.getTextBounds(content, 0, content.length, rect)
        when (mGravity) {
            Gravity.CENTER -> drawCenterContentStart =
                ((cMeasuredWidth - rect.width()) * 0.5).toInt()
            Gravity.LEFT -> drawCenterContentStart = 0
            Gravity.RIGHT -> drawCenterContentStart = cMeasuredWidth - rect.width()
        }
    }

    private fun measuredOutContentStart(content: String) {
        val rect = Rect()
        paintOuterText!!.getTextBounds(content, 0, content.length, rect)
        when (mGravity) {
            Gravity.CENTER -> drawOutContentStart = ((cMeasuredWidth - rect.width()) * 0.5).toInt()
            Gravity.LEFT -> drawOutContentStart = 0
            Gravity.RIGHT -> drawOutContentStart = cMeasuredWidth - rect.width()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.widthMeasureSpec = widthMeasureSpec
        remeasure()
        setMeasuredDimension(cMeasuredWidth, cMeasuredHeight)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val eventConsumed = gestureDetector!!.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startTime = System.currentTimeMillis()
                cancelFuture()
                previousY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = previousY - event.rawY
                previousY = event.rawY
                totalScrollY = (totalScrollY + dy).toInt()

                // 边界处理。
                if (!isLoop) {
                    var top = -initPosition * itemHeight
                    var bottom = (adapter!!.itemsCount - 1 - initPosition) * itemHeight
                    if (totalScrollY - itemHeight * 0.3 < top) {
                        top = totalScrollY - dy
                    } else if (totalScrollY + itemHeight * 0.3 > bottom) {
                        bottom = totalScrollY - dy
                    }
                    if (totalScrollY < top) {
                        totalScrollY = top.toInt()
                    } else if (totalScrollY > bottom) {
                        totalScrollY = bottom.toInt()
                    }
                }
            }
            MotionEvent.ACTION_UP -> if (!eventConsumed) {
                val y = event.y
                val l = Math.acos(((radius - y) / radius).toDouble()) * radius
                val circlePosition = ((l + itemHeight / 2) / itemHeight).toInt()
                val extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight
                mOffset = ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset).toInt()
                if (System.currentTimeMillis() - startTime > 120) {
                    // 处理拖拽事件
                    smoothScroll(ACTION.DAGGLE)
                } else {
                    // 处理条目点击事件
                    smoothScroll(ACTION.CLICK)
                }
            }
            else -> if (!eventConsumed) {
                val y = event.y
                val l = Math.acos(((radius - y) / radius).toDouble()) * radius
                val circlePosition = ((l + itemHeight / 2) / itemHeight).toInt()
                val extraOffset = (totalScrollY % itemHeight + itemHeight) % itemHeight
                mOffset = ((circlePosition - itemsVisible / 2) * itemHeight - extraOffset).toInt()
                if (System.currentTimeMillis() - startTime > 120) {
                    smoothScroll(ACTION.DAGGLE)
                } else {
                    smoothScroll(ACTION.CLICK)
                }
            }
        }
        invalidate()
        return true
    }

    /**
     * 获取Item个数
     * @return
     */
    val itemsCount: Int
        get() = if (adapter != null) adapter!!.itemsCount else 0

    /**
     * 附加在右边的单位字符串
     * @param label
     */
    fun setLabel(label: String?) {
        this.label = label
    }

    fun setGravity(gravity: Int) {
        mGravity = gravity
    }

    fun getTextWidth(paint: Paint?, str: String?): Int {
        var iRet = 0
        if (str != null && str.length > 0) {
            val len = str.length
            val widths = FloatArray(len)
            paint!!.getTextWidths(str, widths)
            for (j in 0 until len) {
                iRet += Math.ceil(widths[j].toDouble()).toInt()
            }
        }
        return iRet
    }

    companion object {
        // 条目间距倍数
        const val lineSpacingMultiplier = 1.7f

        // 修改这个值可以改变滑行速度
        private const val VELOCITYFLING = 5
        private const val SCALECONTENT = 0.8f //非中间文字则用此控制高度，压扁形成3d错觉
        private const val CENTERCONTENTOFFSET = 6f //中间文字文字居中需要此偏移值
        private const val GETPICKERVIEWTEXT = "getPickerViewText" //反射的方法名
    }
}