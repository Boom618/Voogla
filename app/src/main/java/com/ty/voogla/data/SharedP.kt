package com.ty.voogla.data

import android.content.Context
import android.util.SparseArray
import com.ty.voogla.constant.CodeConstant

/**
 * @author TY on 2019/1/3.
 *
 * 主要存 Int 数据
 */
object SharedP {


    /**
     * 存商品编号
     * CodeConstant.SP_SHARED：文件名
     * CodeConstant.GOODS_NO：字段名
     */
    @JvmStatic
    fun putGoodNo(context: Context, position: Int) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .putInt(CodeConstant.GOODS_NO, position)
            .apply()
    }


    @JvmStatic
    fun getGoodNo(context: Context): Int {
        return context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .getInt(CodeConstant.GOODS_NO, -1)
    }

    /**
     * 重置 GoodNo
     */
    @JvmStatic
    fun clearGoodNo(context: Context) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .putInt(CodeConstant.GOODS_NO, -1)
            .apply()
    }

    /** ---------------------存指定 key 的 position ----------------------------*/


    @JvmStatic
    fun putKeyPosition(context: Context, key: String, position: Int) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .putInt(key, position)
            .apply()
    }

    @JvmStatic
    fun getKeyPosition(context: Context, key: String): Int {
        return context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .getInt(key, -1)
    }

    /**
     * 重置 key position
     */
    @JvmStatic
    fun clearPosition(context: Context, key: String) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .putInt(key, -1)
            .apply()
    }

    /** ---------------------存指定 key 的 Boolean 值 ----------------------------*/


    @JvmStatic
    fun putKeyBoolean(context: Context, key: String, boolean: Boolean) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(key, boolean)
            .apply()
    }

    @JvmStatic
    fun getKeyBoolean(context: Context, key: String): Boolean {
        return context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .getBoolean(key, false)
    }

    /**
     * 重置 key
     */
    @JvmStatic
    fun clearKeyBoolean(context: Context, key: String) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .putInt(key, -1)
            .apply()
    }

    /**
     * 退出程序，清除所有数据
     */
    @JvmStatic
    fun clearAll(context: Context) {
        context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()

    }
}