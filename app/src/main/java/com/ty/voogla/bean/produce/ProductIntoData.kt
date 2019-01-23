package com.ty.voogla.bean.produce

/**
 * @author TY on 2019/1/10.
 * 入库列表
 */
class ProductIntoData {

    var list: List<ListBean>? = null

    class ListBean {
        /**
         * companyNo : P000001
         * inBatchNo : SPI20190117000001
         * wareName : yu
         * goodsNo : S002
         * inNum : 1
         * nowNum : 1
         * unit : 11
         * inTime : 2019-01-17 18:06:25
         * inWareDesc : null
         * goodsName : ceshi -002
         * unitName : null
         */

        var companyNo: String? = null
        var inBatchNo: String? = null
        var wareName: String? = null
        var goodsNo: String? = null
        var inBoxNum: Int = 0
        var nowBoxNum: Int = 0
        var unit: String? = null
        var inTime: String? = null
        var inWareDesc: String? = null
        var goodsName: String? = null
        var unitName: String? = null
    }
}