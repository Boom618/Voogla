package com.ty.voogla.bean

import com.ty.voogla.bean.produce.InBoxCodeDetailInfosBean
import java.io.Serializable

/**
 * @author  TY
 * @Date:   2019/5/9 10:29
 * @Description: 生产入库暂存数据
 */
class ProStorageData : Serializable {

    var productBatchNo: String? = null
    var nameWare: String? = null
    var inTime: String? = null
    var nameGoods: String? = null
    var specGoods: String? = null
    // 是否暂存标记
    var isStorage = false

    var listDetail: MutableList<InBoxCodeDetailInfosBean> = mutableListOf()
}