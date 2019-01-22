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

        holder.setText(R.id.tv_code,info.qrCode)
        val deleteView = holder.itemView.findViewById<ImageView>(R.id.image_delete)

        deleteView.setOnClickListener {
            datas.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, datas.size - position)
        }

    }

}