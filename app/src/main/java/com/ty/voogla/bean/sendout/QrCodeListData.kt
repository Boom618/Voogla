package com.ty.voogla.bean.sendout

/**
 * @author TY on 2019/1/18.
 *
 * 箱码和产品码
 */
class QrCodeListData {

    var qrCode: String? = null
    var qrCodeInfos: List<QrCodeInfosBean>? = null

    class QrCodeInfosBean {
        /**
         * qrCodeClass : 二维码分类A0701(箱码)/A0702(产品码)
         * qrCode : 二维码号
         */

        var qrCodeClass: String? = null
    }
}