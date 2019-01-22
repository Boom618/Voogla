package com.ty.voogla.data

import android.util.SparseArray
import com.ty.voogla.bean.sendout.QrCodeListData

import java.util.ArrayList

/**
 * @author TY on 2019/1/22.
 */
object SparseArrayUtil {

    private val qrCodeList = SparseArray<MutableList<QrCodeListData>>()

    /**
     * 存 已出库/生产入库 查看详情的二维码
     * @param position
     * @param dataMutableList
     */
    @JvmStatic
    fun putQrCodeListData(position: Int, dataMutableList: MutableList<QrCodeListData>) {
        qrCodeList.put(position, dataMutableList)
    }

    @JvmStatic
    fun getQrCodeListData(position: Int): MutableList<QrCodeListData> {
        return qrCodeList.get(position)
    }
}
