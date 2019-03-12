package com.ty.voogla.bean.produce

/**
 * @author TY on 2019/1/17.
 * 获取入库详情信息
 */
class ProductInputInfo {

    var inWareInfo: InWareInfoBean? = null
    var inWareDetailInfos: List<InWareDetailInfosBean>? = null

    class InWareInfoBean {
        /**
         * companyAttr : 归属单位
         * companyNo : 企业编号
         * creator : 创建者
         * goodsNo : 商品编号
         * inNum : 入库数量
         * inTime : 入库时间
         * inWareDesc : 入库描述
         * unit : 单位
         * wareName : 仓库名称
         */

        var companyAttr: String? = null
        var companyNo: String? = null
        var creator: String? = null
        var goodsNo: String? = null
        var inNum: String? = null
        var inTime: String? = null
        var inWareDesc: String? = null
        var unit: String? = null
        var wareName: String? = null
        var productBatchNo: String? = null
    }

    class InWareDetailInfosBean {


        var companyNo: String? = null
        var inBatchNo: String? = null
        var batchNo: String? = null
        var goodsNo: String? = null
        var qrCodeClass: String? = null
        var qrCode: String? = null
        var goodsName: String? = null
    }
}