package com.ty.voogla.data

import android.content.Context
import android.util.SparseArray
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



    fun getGoodNo(context: Context): Int {
        return context.getSharedPreferences(CodeConstant.SP_SHARED, Context.MODE_PRIVATE)
            .getInt(CodeConstant.GOODS_NO, -1)
    }



    /**
     * 存 SparseArray（hashMap）
     */
    fun putSparseArray(context: Context,sparseArray: SparseArray<String>){


    }
}