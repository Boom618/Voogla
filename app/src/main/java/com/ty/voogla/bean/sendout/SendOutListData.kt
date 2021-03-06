package com.ty.voogla.bean.sendout

/**
 * @author TY on 2019/1/17.
 * 发货出库列表
 */
class SendOutListData {

    var list: List<ListBean>? = null

    class ListBean {
        /**
         * companyNo : 企业编号
         * deliveryNo : 发货单号
         * customerName : 客户名称
         * deliveryDate : 发货日期
         * deliveryState : 发货状态
         * deliveryStateName : 发货状态名称
         * provinceLevel : 省级
         * cityLevel : 市级
         * countyLevel : 县级
         * deliveryAddress : 发货地址
         * outTime : 出库时间
         * outWareDesc : 出库描述
         */

        var companyNo: String? = null
        var deliveryNo: String? = null
        var customerName: String? = null
        var deliveryDate: String? = null
        var deliveryState: String? = null
        var deliveryStateName: String? = null
        var provinceLevel: String? = null
        var cityLevel: String? = null
        var countyLevel: String? = null
        var deliveryAddress: String? = null
        var outTime: String? = null
        var outWareDesc: String? = null
    }
}