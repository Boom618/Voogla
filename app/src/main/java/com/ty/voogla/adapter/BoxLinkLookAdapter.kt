package com.ty.voogla.adapter

import android.content.Context
import com.ty.voogla.R
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/11.
 */
class BoxLinkLookAdapter(context: Context, layout: Int, datas: MutableList<String>) :
    CommonAdapter<String>(context, layout, datas) {

    var string:String = "产品码"
    override fun convert(holder: ViewHolder, t: String?, position: Int) {


//        when {
//            position % 2 == 0 -> string = "箱码"
//            else -> string = "产品码"
//        }

        string = when {
            position % 2 == 0 -> "箱码"
            else -> "产品码"
        }
        holder.setText(R.id.tv_type, string)
            .setText(R.id.tv_code, "2019043 $position")

    }

}