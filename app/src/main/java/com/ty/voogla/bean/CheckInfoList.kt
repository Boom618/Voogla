package com.ty.voogla.bean

/**
 * @author TY on 2019/1/25.
 * 稽查列表
 */
class CheckInfoList {

    var list: MutableList<ListBean>? = null

    class ListBean {
        /**
         * companyNo : 企业编号
         * companyName 企业名称
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
         * fleeFlag : 是否窜货(01否,02是)
         */

        var companyNo: String? = null
        var customerType: String? = null
        var deliveryNo: String? = null
        var customerName: String? = null
        var companyName: String? = null
        var deliveryDate: String? = null
        var deliveryState: String? = null
        var deliveryStateName: String? = null
        var provinceLevel: String? = null
        var cityLevel: String? = null
        var countyLevel: String? = null
        var deliveryAddress: String? = null
        var outTime: String? = null
        var outWareDesc: String? = null
        var fleeFlag: String? = null
    }
}