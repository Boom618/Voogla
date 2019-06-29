package com.ty.voogla.bean.produce

/**
 * @author TY on 2019/1/16.
 *
 * 二维码解码
 */
class DecodeCode {
    var status = 500
    var msg = ""
    var result: ResultBean? = null

    class ResultBean {
        var code: String? = null
        // 1 为产品码,2 为箱码
        var qrCodeType: String? = null
        // 套码
        var buApplyNo: String? = null
        // 申请批次
        var applyNo: String? = null
        // 所属者（产品码 ——> 对应的箱码）
        var ownerCode: String? = null
    }
}