package com.uoooo.mvvm.example.ui.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.exoplayer2.ui.PlayerView

class CustomPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : PlayerView(context, attrs, defStyleAttr) {
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (parent is MotionLayout) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> hideController()
                MotionEvent.ACTION_UP -> showController()
            }
            return (parent as MotionLayout).onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }
}
