package com.ty.voogla.bean.produce

import java.io.Serializable

/**
 * @author TY on 2019/1/16.
 * 获取产品列表信息
 *
 */
class ProductListInfoData : Serializable {

    var list: List<ListBean>? = null

    class ListBean : Serializable {
        /**
         * companyNo : 企业编号
         * goodsNo : 商品编号
         * goodsName : 商品名称
         * goodsSpec : 商品规格
         */

        var companyNo: String? = null
        var goodsNo = ""
        var goodsName = ""
        var goodsSpec: String? = null
        var unit: String? = null
        var unitName: String? = null
    }
}
