package com.ty.voogla.adapter

import android.content.Context
import android.widget.ImageView
import com.ty.voogla.R
import com.ty.voogla.util.ToastUtil
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/11.
 *
 * 生产入库详情
 */
class ProIntoDetailAdapter(context: Context, layout: Int, datas: MutableList<String>) :
    CommonAdapter<String>(context, layout, datas) {

    override fun convert(holder: ViewHolder, t: String?, position: Int) {
        holder.setText(R.id.tv_code, "280000000 $position")

        val editView = holder.itemView.findViewById<ImageView>(R.id.image_edit)
        val deleteView = holder.itemView.findViewById<ImageView>(R.id.image_delete)

        editView.setOnClickListener {
            ToastUtil.showToast("修改 $position")
        }

        deleteView.setOnClickListener {

            datas.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position,datas.size - position)
        }
    }
}