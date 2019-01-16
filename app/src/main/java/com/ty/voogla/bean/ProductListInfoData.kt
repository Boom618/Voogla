package com.ty.voogla.bean

/**
 * @author TY on 2019/1/16.
 * 获取产品列表信息
 *
 */
class ProductListInfoData {

    var list: List<ListBean>? = null

    class ListBean {
        /**
         * companyNo : 企业编号
         * goodsNo : 商品编号
         * goodsName : 商品名称
         * goodsSpec : 商品规格
         */

        var companyNo: String? = null
        var goodsNo: String? = null
        var goodsName: String? = null
        var goodsSpec: String? = null
    }
}
