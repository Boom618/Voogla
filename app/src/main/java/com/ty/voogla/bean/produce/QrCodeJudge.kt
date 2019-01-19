package com.ty.voogla.bean.produce

/**
 * @author TY on 2019/1/19.
 *
 * 二维码校验
 */
class QrCodeJudge {

    var qrCodeInfo: QrCodeInfoBean? = null

    class QrCodeInfoBean {
        /**
         * companyNo : 企业编号
         * generateNo : 二维码生成编号
         */

        var companyNo: String? = null
        var generateNo: String? = null
    }
}