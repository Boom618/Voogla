package com.ty.voogla.widght

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.ty.voogla.R

/**
 * @author TY on 2019/1/9.
 */
class ButtonView:View {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}



    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = resources.getColor(R.color.theme_color)


    }
}