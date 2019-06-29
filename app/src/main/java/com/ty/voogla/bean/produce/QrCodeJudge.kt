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
         * =============================== 校验
         * "inBatchNo": "SPI20190124000002",
         * "batchNo": "hm20190123002",
         * "goodsNo": "17706-产品1",
         * "qrCodeClass": "A0702",
         *  wareName: "仓库编号",
         * "qrCode": "19012400000002",
         * "goodsName": "17706-产品1"
         *
         */

        var companyNo: String? = null
        var inBatchNo: String? = null
        var batchNo: String? = null
        var goodsNo: String? = null
        var goodsName: String? = null
        var qrCode: String? = null
        var qrCodeClass: String? = null
    }
}