package com.ty.voogla.bean.sendout

import java.io.Serializable

/**
 * @author TY on 2019/1/18.
 *
 * 箱码和产品码
 */
class QrCodeListData : Serializable {

    // 码号
    var qrCode: String? = null
    // 类别
    var qrCodeClass: String? = null
    // 二维码生成编号(校验生成)
    var generateNo: String? = null
}