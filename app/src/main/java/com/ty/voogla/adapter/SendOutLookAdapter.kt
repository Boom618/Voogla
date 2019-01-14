package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.ui.activity.BoxLinkLookActivity
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/14.
 */
class SendOutLookAdapter(val context: Context,layout:Int,datas:MutableList<String>)
    :CommonAdapter<String>(context,layout,datas){

    override fun convert(holder: ViewHolder, t: String?, position: Int) {


        holder.itemView.findViewById<TextView>(R.id.tv_code_detail).setOnClickListener {
            context.startActivity(Intent(context,BoxLinkLookActivity::class.java))
        }
    }


}