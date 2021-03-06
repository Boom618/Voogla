package com.ty.voogla.bean.produce

import com.ty.voogla.bean.sendout.QrCodeListData
import java.io.Serializable

/**
 * @author TY on 2019/1/23.
 *
 * 入库 （新增） 一个箱码/ 套码 对应 产品码列表
 */
class InBoxCodeDetailInfosBean : Serializable {

    /**
     * boxCode : 箱码
     * buApplyNo: 套码
     * qrCodeInfos : [{"generateNo":"二维码生成编号","qrCode":"产品码"}]
     */

    var qrCode: String? = null
    var buApplyNo: String? = null
    var qrCodeClass: String? = null
    var qrCodeInfos: MutableList<QrCodeListData>? = null
}