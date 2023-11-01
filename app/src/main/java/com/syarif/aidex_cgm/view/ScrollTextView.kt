package com.syarif.aidex_cgm.view

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.syarif.aidex_cgm.utils.CustomStringBuilder


class ScrollTextView : AppCompatTextView {
    var mErrorMsg: CustomStringBuilder? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    private fun initView() {
        movementMethod = ScrollingMovementMethod()
        mErrorMsg = CustomStringBuilder()
    }

    override fun setText(text: CharSequence, type: BufferType) {
        if (mErrorMsg != null) {
            super.setText(mErrorMsg!!.append(text), type)
        } else {
            super.setText(text, type)
        }
        val line = lineCount
        if (line > 11) { //超出屏幕自动滚动显示(11是当前页面显示的最大行数)
            val offset = lineCount * lineHeight
            scrollTo(0, offset - height + lineHeight)
        }
    }
}

