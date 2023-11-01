package com.syarif.aidex_cgm.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import com.syarif.aidex_cgm.R
import java.util.Locale


/**
 * Created by XIAS on 2018/9/17.
 */
class CEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    androidx.appcompat.widget.AppCompatEditText(context, attrs) {
    private var height = 40 //view的高度
    private var count = 6 //矩形数目
    private var borderPaint: Paint? = null //边框画笔
    private var fillPaint: Paint? = null //填充画笔
    private var focusBorderPaint: Paint? = null //焦点边框画笔
    private var focusFillPaint: Paint? = null //焦点填充画笔
    private var paintText: Paint? = null //文字画笔
    private var paintCircle: Paint? = null //圆形画笔
    private var startX = 0 //开始坐标
    private var lineWidth = 1 //边框宽度
    private var lineColor = Color.WHITE //边框颜色
    private var stokesColor = Color.WHITE //填充颜色
    private var focusStokeColor = Color.WHITE //焦点填充颜色
    private var focusLineColor = Color.WHITE //焦点边框颜色
    private var textColor = Color.WHITE //文字的颜色
    private var textSize = 64 //文字的大小
    private var position = 0 //当前输入的位置
    private var isDrawLine = false //是否绘制边框，true绘制，false不绘制
    private var spaceWidth = 0 //边框宽度
    private var isDrawCircle = false //是绘制圆还是文字；true绘制圆，false绘制文字
    private var circleRadius = 10 //如果不显示文字则绘制圆，此为圆半径
    private var circleColor = Color.WHITE //如果不显示文字则绘制圆，此为圆填充色
    private var onFinishListener: OnFinishListener? = null
    fun setOnFinishListener(onFinishListener: OnFinishListener?) {
        this.onFinishListener = onFinishListener
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CEditText)
        if (typedArray != null) {
            height = typedArray.getDimensionPixelSize(R.styleable.CEditText_height, height)
            count = typedArray.getInt(R.styleable.CEditText_count, count)
            lineWidth = typedArray.getDimensionPixelSize(R.styleable.CEditText_lineWidth, lineWidth)
            lineColor = typedArray.getColor(R.styleable.CEditText_lineColor, lineColor)
            focusLineColor =
                typedArray.getColor(R.styleable.CEditText_focusLineColor, focusLineColor)
            focusStokeColor =
                typedArray.getColor(R.styleable.CEditText_focusStokeColor, focusStokeColor)
            stokesColor = typedArray.getColor(R.styleable.CEditText_stokesColor, stokesColor)
            textColor = typedArray.getColor(R.styleable.CEditText_textColor, textColor)
            spaceWidth =
                typedArray.getDimensionPixelSize(R.styleable.CEditText_spaceWidth, spaceWidth)
            textSize = typedArray.getDimensionPixelSize(R.styleable.CEditText_textSize, textSize)
            isDrawCircle = typedArray.getBoolean(R.styleable.CEditText_isDrawCircle, isDrawCircle)
            isDrawLine = typedArray.getBoolean(R.styleable.CEditText_isDrawLine, isDrawLine)
            circleRadius =
                typedArray.getDimensionPixelSize(R.styleable.CEditText_circleRadius, circleRadius)
            circleColor = typedArray.getColor(R.styleable.CEditText_circleColor, circleColor)
            typedArray.recycle()
        }
        setBackgroundColor(Color.TRANSPARENT)
        setCursorVisible(false)
        setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS)

//        String digits = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        setFilters(new InputFilter[]{new InputFilter.LengthFilter(count),new MyInputFilter(digits)});
//        setKeyListener(DigitsKeyListener.getInstance(digits));
        init()
        //        setTransformationMethod(new TransInformation());
    }

    private fun init() {
        initPaint()
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint!!.strokeWidth = lineWidth.toFloat()
        borderPaint!!.setColor(lineColor)
        borderPaint!!.isAntiAlias = true
        borderPaint!!.style = Paint.Style.STROKE
        fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        fillPaint!!.isAntiAlias = true
        fillPaint!!.style = Paint.Style.FILL
        fillPaint!!.setColor(stokesColor)
        focusBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        focusBorderPaint!!.strokeWidth = lineWidth.toFloat()
        focusBorderPaint!!.setColor(focusLineColor)
        focusBorderPaint!!.isAntiAlias = true
        focusBorderPaint!!.style = Paint.Style.STROKE
        focusFillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        focusFillPaint!!.isAntiAlias = true
        focusFillPaint!!.style = Paint.Style.FILL
        focusFillPaint!!.setColor(focusStokeColor)
        if (!isDrawCircle) {
            paintText = Paint(Paint.ANTI_ALIAS_FLAG)
            paintText!!.textAlign = Paint.Align.CENTER
            paintText!!.isAntiAlias = true
            paintText!!.textSize = textSize.toFloat()
            paintText!!.setColor(textColor)
        } else {
            paintCircle = Paint(Paint.ANTI_ALIAS_FLAG)
            paintCircle!!.isAntiAlias = true
            paintCircle!!.strokeWidth = 2f
            paintCircle!!.style = Paint.Style.FILL
            paintCircle!!.setColor(circleColor)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        require(count * height <= w) { "View must be less than the width of the screen!" }
        startX = (w - count * height - (count - 1) * spaceWidth) / 2
    }

    override fun onDraw(canvas: Canvas) {
        drawRectBorder(canvas)
        drawRectFocused(canvas, position)
        if (!isDrawCircle) {
            drawText(canvas)
        } else {
            drawCircle(canvas)
        }
    }

    /**
     * 绘制圆
     *
     * @param canvas
     */
    private fun drawCircle(canvas: Canvas) {
        val chars = getText().toString().toCharArray()
        for (i in chars.indices) {
            drawRectFocused(canvas, i)
            canvas.drawCircle(
                (startX + i * height + i * spaceWidth + height / 2).toFloat(),
                (height / 2).toFloat(),
                circleRadius.toFloat(),
                paintCircle!!
            )
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private fun drawText(canvas: Canvas) {
        val chars = getText().toString().toCharArray()
        for (i in chars.indices) {
            drawRectFocused(canvas, i)
            val fontMetrics = paintText!!.getFontMetrics()
            val baseLineY = (height / 2 - fontMetrics.top / 2 - fontMetrics.bottom / 2).toInt()
            canvas.drawText(
                chars[i].toString().uppercase(Locale.getDefault()),
                (startX + i * height + i * spaceWidth + height / 2).toFloat(),
                baseLineY.toFloat(),
                paintText!!
            )
        }
    }

    /**
     * 绘制默认状态
     *
     * @param canvas
     */
    private fun drawRectBorder(canvas: Canvas) {
        for (i in 0 until count) {
//            if (isDrawLine){
//
//            }
//            RectF rectBorder = new RectF(startX + i * height + i * spaceWidth,
//                    1, startX + i * height + i * spaceWidth + height,
//                    height);
//            canvas.drawRoundRect(rectBorder,5,5, borderPaint);
            val rectFill = RectF(
                (startX + i * height + i * spaceWidth + lineWidth).toFloat(),
                (lineWidth + 1).toFloat(),
                (startX + i * height + i * spaceWidth + height - lineWidth).toFloat(),
                (height - lineWidth).toFloat()
            )
            canvas.drawRoundRect(rectFill, 6f, 6f, fillPaint!!)
        }
    }

    /**
     * 绘制输入状态
     *
     * @param canvas
     * @param position
     */
    private fun drawRectFocused(canvas: Canvas, position: Int) {
        if (position > count - 1) {
            return
        }
        if (isDrawLine) canvas.drawRect(
            (startX + position * height + position * spaceWidth).toFloat(),
            1f,
            (startX + position * height + position * spaceWidth + height).toFloat(),
            height.toFloat(),
            focusBorderPaint!!
        )
        val rectFill = RectF(
            (startX + position * height + position * spaceWidth + lineWidth).toFloat(),
            (lineWidth + 1).toFloat(),
            (startX + position * height + position * spaceWidth + height - lineWidth).toFloat(),
            (height - lineWidth).toFloat()
        )
        canvas.drawRoundRect(rectFill, 6f, 6f, focusFillPaint!!)
    }

    override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        position = start + lengthAfter
        if (!TextUtils.isEmpty(text) && text.toString().length == count) if (onFinishListener != null) {
            onFinishListener!!.onFinish(text.toString())
        }
        invalidate()
    }

    interface OnFinishListener {
        fun onFinish(msg: String?)
    }
}