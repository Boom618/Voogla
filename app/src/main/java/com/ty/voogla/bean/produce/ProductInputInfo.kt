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

        var companyNo: String? = null
        var goodsNo: String? = null
        var unit: String? = null
    }

    class InWareDetailInfosBean {


        // comBoxCode 企业箱号
        var companyNo: String? = null
        var inBatchNo: String? = null
        var goodsNo: String? = null
        var comBoxCode: String? = null
        var qrCodeClass: String? = null
        var qrCode: String? = null
        var goodsName: String? = null
    }
}