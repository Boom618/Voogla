package com.ty.voogla.bean.sendout

import java.io.Serializable

/**
 * @author TY on 2019/1/18.
 *
 * 箱码和产品码（数据展示）
 */
class QrCodeListData : Serializable {

    // 码号
    var qrCode: String? = null
    // 类别
    var qrCodeClass: String? = null
    // 仓库编号 入库批次号
    var wareName: String? = null
    var inBatchNo: String? = null
    // 二维码生成编号(校验生成)
    var generateNo: String? = null
}