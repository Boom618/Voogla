package com.ty.voogla.adapter

import android.content.Context
import com.ty.voogla.R
import com.ty.voogla.bean.sendout.QrCodeListData
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/11.
 */
class BoxLinkLookAdapter(context: Context, layout: Int, datas: List<QrCodeListData>) :
    CommonAdapter<QrCodeListData>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: QrCodeListData, position: Int) {

        holder.setText(R.id.tv_type, info.qrCodeClass)
            .setText(R.id.tv_code, info.qrCode)

    }

}