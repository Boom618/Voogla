package com.ty.voogla.data

import android.content.Context
import com.ty.voogla.constant.CodeConstant

/**
 * @author TY on 2019/1/3.
 */
object SharedP {


    /**
     * 存商品编号
     * CodeConstant.SP_SHARED：文件名
     * CodeConstant.GOODS_NO：字段名
     */
    fun putGoodNo(context: Context, position: Int) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .putInt(CodeConstant.GOODS_NO, position)
            .apply()
    }


    /**
     * 存 点击库位码是否有焦点和位置
     */
    fun putHasFocusAndPosition(context: Context, hasFocus: Boolean, position: Int) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean("hasFocus", hasFocus)
            .putInt("position", position)
            .apply()
    }

    fun getFocus(context: Context): Boolean {
        return context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .getBoolean("hasFocus", false)
    }

    fun getGoodNo(context: Context): Int {
        return context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .getInt(CodeConstant.GOODS_NO, -1)
    }

    fun clearFocusAndPosition(context: Context) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean("hasFocus", false)
            .putInt("position", -1)
            .apply()
    }
}