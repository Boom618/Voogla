package com.ty.voogla.adapter

import android.content.Context
import com.ty.voogla.R
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/11.
 *
 * 入库查看：全是产品码
 */
class BoxLinkProLookAdapter(context: Context, layout: Int, datas: List<String>) :
    CommonAdapter<String>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: String, position: Int) {

        holder.setText(R.id.tv_type, "产品码")
            .setText(R.id.tv_code, info)

    }

}