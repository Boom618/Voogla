package com.ty.voogla.bean.sendout

/**
 * @author TY on 2019/1/18.
 *
 * 发货单详情
 */
class SendOutListInfo {

    var goodsDeliveryInfo: GoodsDeliveryInfoBean? = null
    var deliveryDetailInfos: MutableList<DeliveryDetailInfosBean>? = null

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
    }

    class DeliveryDetailInfosBean {
        /**
         * goodsNo : 商品编号
         * goodsName : 商品名称
         * deliveryNum : 发货数量
         * unit : 商品单位
         * unitName : 商品单位中文
         */

        var goodsNo: String? = null
        var goodsName: String? = null
        var deliveryNum: String? = null
        var unit: String? = null
        var unitName: String? = null

        // 箱码信息
        var qrCode:ArrayList<String>? = null
//        var qrCode:MutableSet<QrCodeListData>? = null

    }

    /**
     * 箱码信息
     */
}