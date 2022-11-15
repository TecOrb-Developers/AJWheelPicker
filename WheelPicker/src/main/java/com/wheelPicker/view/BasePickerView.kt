package com.wheelPicker.view

import android.widget.FrameLayout
import android.graphics.Typeface
import android.view.animation.Animation
import android.app.Activity
import android.content.Context
import android.view.*
import com.wheelPicker.R
import com.wheelPicker.utils.PickerViewAnimateUtil
import com.wheelPicker.view.BasePickerView
import android.view.View.OnTouchListener
import android.view.animation.AnimationUtils
import android.widget.Button
import com.wheelPicker.listener.OnDismissListener

abstract class BasePickerView(private val context: Context) {
    private val params = FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    )
    protected var contentContainer: ViewGroup? = null
    private var decorView //activity的根View
            : ViewGroup? = null
    private var rootView //附加View 的 根View
            : ViewGroup? = null
    private var overlayFrameLayout: FrameLayout? = null
    private var onDismissListener: OnDismissListener? = null
    private var isDismissing = false
    var btnSubmit: Button? = null
    var btnCancel: Button? = null

    fun getBtnSubmit(): View? {
        return btnSubmit
    }

    @JvmName("setBtnSubmit1")
    fun setBtnSubmit(btnSubmit: Button?) {
        this.btnSubmit = btnSubmit
    }

    fun getBtnCancel(): View? {
        return btnCancel
    }

    @JvmName("setBtnCancel1")
    fun setBtnCancel(btnCancel: Button?) {
        this.btnCancel = btnCancel
    }

    fun hideCancelButton() {
        if (btnCancel != null) btnCancel!!.visibility = View.GONE
    }

    fun hideSubmitButton() {
        if (btnSubmit != null) btnSubmit!!.visibility = View.GONE
    }

    fun showCancelButton() {
        if (btnCancel != null) btnCancel!!.visibility = View.VISIBLE
    }

    fun showSubmitButton() {
        if (btnSubmit != null) btnSubmit!!.visibility = View.VISIBLE
    }

    fun setCancelButtonText(newText: String?) {
        if (btnCancel != null) btnCancel!!.text = newText
    }

    fun setSubmitButtonText(newText: String?) {
        if (btnSubmit != null) btnSubmit!!.text = newText
    }

    fun setCancelButtonText(resId: Int) {
        if (btnCancel != null) btnCancel!!.setText(resId)
    }

    fun setSubmitButtonText(resId: Int) {
        if (btnSubmit != null) btnSubmit!!.setText(resId)
    }

    open fun setSubmitButtonTextSize(textSize: Float) {
        if (btnSubmit != null) btnSubmit!!.textSize = textSize
    }

    open fun setSubmitButtonTextColor(color: Int) {
        if (btnSubmit != null) btnSubmit!!.setTextColor(color)
    }

    open fun setCancelButtonTextSize(textSize: Float) {
        if (btnCancel != null) btnCancel!!.textSize = textSize
    }

    open fun setCancelButtonTextColor(color: Int) {
        if (btnCancel != null) btnCancel!!.setTextColor(color)
    }

    abstract fun setCustomFont(typeface: Typeface)
    private var outAnim: Animation? = null
    private var inAnim: Animation? = null
    private val gravity = Gravity.BOTTOM
    protected fun initViews() {
        val layoutInflater = LayoutInflater.from(context)
        decorView =
            (context as Activity).window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
        rootView =
            layoutInflater.inflate(R.layout.layout_basepickerview, decorView, false) as ViewGroup
        rootView!!.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        overlayFrameLayout = rootView!!.findViewById<View>(R.id.outmost_container) as FrameLayout
        contentContainer = rootView!!.findViewById<View>(R.id.content_container) as ViewGroup
        contentContainer!!.layoutParams = params
    }

    fun hideOverlay() {
        overlayFrameLayout!!.setBackgroundResource(0)
    }

    protected fun init() {
        inAnim = inAnimation
        outAnim = outAnimation
    }

    protected fun initEvents() {}

    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private fun onAttached(view: View?) {
        decorView!!.addView(view)
        contentContainer!!.startAnimation(inAnim)
    }

    /**
     * 添加这个View到Activity的根视图
     */
    fun show() {
        if (isShowing) {
            return
        }
        onAttached(rootView)
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    val isShowing: Boolean
        get() {
            val view = decorView!!.findViewById<View>(R.id.outmost_container)
            return view != null
        }

    fun dismiss() {
        if (isDismissing) {
            return
        }

        //消失动画
        outAnim!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                decorView!!.post { //从activity根视图移除
                    decorView!!.removeView(rootView)
                    isDismissing = false
                    if (onDismissListener != null) {
                        onDismissListener!!.onDismiss(this@BasePickerView)
                    }
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        contentContainer!!.startAnimation(outAnim)
        isDismissing = true
    }

    val inAnimation: Animation
        get() {
            val res = PickerViewAnimateUtil.getAnimationResource(gravity, true)
            return AnimationUtils.loadAnimation(context, res)
        }
    val outAnimation: Animation
        get() {
            val res = PickerViewAnimateUtil.getAnimationResource(gravity, false)
            return AnimationUtils.loadAnimation(context, res)
        }

    fun setOnDismissListener(onDismissListener: OnDismissListener?): BasePickerView {
        this.onDismissListener = onDismissListener
        return this
    }

    fun setCancelable(isCancelable: Boolean): BasePickerView {
        val view = rootView!!.findViewById<View>(R.id.outmost_container)
        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener)
        } else {
            view.setOnTouchListener(null)
        }
        return this
    }

    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private val onCancelableTouchListener = OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            dismiss()
        }
        false
    }

    init {
        initViews()
        init()
        initEvents()
    }

    fun findViewById(id: Int): View {
        return contentContainer!!.findViewById(id)
    }
}