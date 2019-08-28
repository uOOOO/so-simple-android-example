package com.uoooo.simple.example.ui.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.exoplayer2.ui.PlayerView

class CustomPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : PlayerView(context, attrs, defStyleAttr) {

//    private var isShowController: Boolean = false

    init {
        controllerHideOnTouch = true
    }

//    fun isShowController(): Boolean {
//        return isShowController
//    }
//
//    override fun showController() {
//        isShowController = true
//        super.showController()
//    }
//
//    override fun hideController() {
//        isShowController = false
//        super.hideController()
//    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        for (i in 0..childCount) {
//            getChildAt(i)?.let {
//                val consumed = it.dispatchTouchEvent(ev)
//                if (consumed) {
//                    return consumed
//                }
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}
