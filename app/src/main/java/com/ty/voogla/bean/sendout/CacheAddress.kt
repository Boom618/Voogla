package com.ty.voogla.bean.sendout

import java.io.Serializable

/**
 * @author  TY
 * @Date:   2019/5/14 20:59
 * @Description: 发货出库 缓存地址信息
 */
class CacheAddress:Serializable {

    // 缓存地址，
    var cityLevel = ""
    var countyLevel = ""
    var provinceLevel = ""
    var deliveryAddress = ""

    // 缓存 goodsNo
    var unit = ""
    var goodsNo = ""
}