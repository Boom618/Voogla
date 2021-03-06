package com.ty.voogla.adapter

import android.content.Context
import com.ty.voogla.R
import com.ty.voogla.bean.sendout.QrCodeListData
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/11.
 * 出库查看 含箱码和产品码
 */
class BoxLinkLookAdapter(context: Context, layout: Int, datas: List<QrCodeListData>) :
    CommonAdapter<QrCodeListData>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: QrCodeListData, position: Int) {

        val type = if (info.qrCodeClass == "A0701") "产品码" else "箱码"
        holder.setText(R.id.tv_type, type)
            .setText(R.id.tv_code, info.qrCode)

    }

}