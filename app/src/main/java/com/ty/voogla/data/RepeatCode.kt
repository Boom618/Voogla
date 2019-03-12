package com.ty.voogla.data

import com.ty.voogla.bean.produce.InBoxCodeDetailInfosBean

/**
 * @author TY on 2019/1/28.
 *
 * 检测是否为重复码
 */
object RepeatCode {


    /**
     * currentCode 在 list 中
     * 返回 true ：表示当前码在 list 中已存在
     */
    @JvmStatic
    fun isRepeatCode(currentCode: String, list: List<InBoxCodeDetailInfosBean>): Boolean {

        val size = list.size

        for (i in 0 until size) {
            val qrCode = list[i].qrCode!!
            if (currentCode == qrCode) {
                return true
            } else {
                val qrCodeInfos = list[i].qrCodeInfos!!
                val tempSize = qrCodeInfos.size
                for (j in 0 until tempSize) {
                    val code = qrCodeInfos[j].qrCode!!
                    if (currentCode == code) {
                        return true
                    }
                }
            }
        }
        return false
    }
}