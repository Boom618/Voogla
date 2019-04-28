package com.ty.voogla.data

import com.ty.voogla.bean.sendout.QrCodeListData

/**
 * @author TY on 2019/4/28.
 * 移出 List 中重复元素
 */
object RemoveDupData {


    @JvmStatic
    fun removeDupString(list: MutableList<String>): MutableList<String> {
        val set = list.toMutableSet()
        return set.toMutableList()
    }

    @JvmStatic
    fun removeDupQrCode(list: MutableList<QrCodeListData>): MutableList<QrCodeListData> {
        val set = list.toMutableSet()
        return set.toMutableList()
    }
}