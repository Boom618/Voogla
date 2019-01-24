package com.ty.voogla.adapter

import android.content.Context
import android.widget.ImageView
import com.ty.voogla.R
import com.ty.voogla.bean.sendout.QrCodeListData
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/11.
 */
class BoxLinkAdapter(context: Context, layout: Int, datas: MutableList<QrCodeListData>?) :
    CommonAdapter<QrCodeListData>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: QrCodeListData, position: Int) {
        val type = if (info.qrCodeClass == "A0701" || info.qrCodeClass == "产品码") "产品码" else "箱码"
        holder.setText(R.id.tv_code,info.qrCode)
            .setText(R.id.tv_type,type)

//        val deleteView = holder.itemView.findViewById<ImageView>(R.id.image_delete)
//
//        deleteView.setOnClickListener {
//            datas.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position, datas.size - position)
//        }

    }

}