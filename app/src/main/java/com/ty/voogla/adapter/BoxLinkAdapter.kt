package com.ty.voogla.adapter

import android.content.Context
import android.widget.ImageView
import com.ty.voogla.R
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/11.
 */
class BoxLinkAdapter(context: Context, layout: Int, datas: MutableList<String>?) :
    CommonAdapter<String>(context, layout, datas) {

    override fun convert(holder: ViewHolder, t: String?, position: Int) {

        val deleteView = holder.itemView.findViewById<ImageView>(R.id.image_delete)

        deleteView.setOnClickListener {
            datas.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, datas.size - position)
        }

    }

}