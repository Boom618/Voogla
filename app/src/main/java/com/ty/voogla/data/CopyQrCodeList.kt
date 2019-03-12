package com.ty.voogla.data

import com.ty.voogla.bean.sendout.QrCodeListData

/**
 * @author TY on 2019/1/27.
 */
object CopyQrCodeList {

    fun deepCopyList( data : QrCodeListData) :QrCodeListData{

        val temp = QrCodeListData()

        temp.qrCode = data.qrCode
        temp.qrCodeClass = data.qrCodeClass

        return temp

    }
}