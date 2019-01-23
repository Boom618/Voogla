package com.ty.voogla.bean.produce


/**
 * @author TY on 2019/1/15.
 */
class AddProduct {

    var inWareInfo: InWareInfoBean? = null
    var inBoxCodeDetailInfos: List<InBoxCodeDetailInfosBean>? = null

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

//    class InBoxCodeDetailInfosBean {
//        /**
//         * boxCode : 箱码
//         * qrCodeInfos : [{"generateNo":"二维码生成编号","qrCode":"产品码"}]
//         */
//
//        var boxCode: String? = null
//        var qrCodeInfos: List<QrCodeListData>? = null
//
//    }
}