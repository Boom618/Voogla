package com.ty.voogla.bean.sendout

import java.io.Serializable

/**
 * @author TY on 2019/1/19.
 * 新增出库
 */
class AddSendOutData : Serializable {

    var outWareInfo: WareInfoBean? = null
    var outQrCodeDetailInfos: List<OutQrCodeDetailInfosBean>? = null

    class WareInfoBean : Serializable {
        /**
         * companyNo : 企业编号
         * creator : 创建者
         * deliveryNo : 发货单号
         * outTime : 出库时间
         * outWareDesc : 出库描述
         */

        var companyNo: String? = null
        var creator: String? = null
        var deliveryNo: String? = null
        var outTime: String? = null
        var deliveryAddress: String? = null
    }

    class OutQrCodeDetailInfosBean : Serializable {
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
        var goodsNo: String? = null
        var outBoxNum: String? = null
        var outGoodsNum: String? = null
        var unit: String? = null
        var qrCodeInfos: List<QrCodeListData>? = null


    }
}