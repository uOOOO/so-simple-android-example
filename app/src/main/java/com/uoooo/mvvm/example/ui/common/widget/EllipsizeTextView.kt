package com.uoooo.mvvm.example.ui.common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView

class EllipsizeTextView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    TextView(context, attrs, defStyleAttr) {

    init {
        addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            maxLines = (height / lineHeight)
            ellipsize = TextUtils.TruncateAt.END
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        ellipsize = null
        maxLines = Integer.MAX_VALUE
        super.setText(text, type)
    }
}
