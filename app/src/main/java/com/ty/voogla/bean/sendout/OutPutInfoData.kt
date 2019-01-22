package com.ty.voogla.bean.sendout

/**
 * @author TY on 2019/1/18.
 * 获取出库信息
 */
class OutPutInfoData {

    var goodsDeliveryInfo: GoodsDeliveryInfoBean? = null
    var outQrCodeDetailInfos: MutableList<OutQrCodeDetailInfosBean>? = null

    class GoodsDeliveryInfoBean {
        /**
         * companyNo : 企业编号
         * deliveryNo : 发货单号
         * customerName : 客户名称
         * deliveryDate : 发货日期
         * provinceLevel : 省级
         * cityLevel : 市级
         * countyLevel : 县级
         * deliveryAddress : 详细地址
         * deliveryState : 发货状态
         * deliveryStateName : 发货状态中文
         * outTime : 出库时间
         * outWareDesc : 出库描述
         */

        var companyNo: String? = null
        var deliveryNo: String? = null
        var customerName: String? = null
        var deliveryDate: String? = null
        var provinceLevel: String? = null
        var cityLevel: String? = null
        var countyLevel: String? = null
        var deliveryAddress: String? = null
        var deliveryState: String? = null
        var deliveryStateName: String? = null
        var outTime: String? = null
        var outWareDesc: String? = null
    }

    class OutQrCodeDetailInfosBean {
        /**
         * inBatchNo : 入库批次号
         * wareName : 仓库名称
         * goodsNo : 商品编号
         * outBoxNum : 出库箱数
         * outGoodsNum : 出库产品数
         * unit : 商品单位
         * qrCodeInfos : [{"qrCodeClass":"二维码分类A0701(箱码)/A0702(产品码)","qrCode":"二维码号"}]
         */

        var inBatchNo: String? = null
        var wareName: String? = null
        var goodsNo: String? = null
        var outBoxNum: String? = null
        var outGoodsNum: String? = null
        var unit: String? = null
        var qrCodeInfos: MutableList<QrCodeListData>? = null

    }
}