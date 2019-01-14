package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.ui.activity.SendOutLookActivity
import com.ty.voogla.ui.activity.SendOutNextActivity
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/14.
 */
class SendOutAdapter(val context: Context, layout: Int, datas: MutableList<String>) :
    CommonAdapter<String>(context, layout, datas) {

    override fun convert(holder: ViewHolder, t: String?, position: Int) {

        val type = when {
            position % 2 == 0 -> "立即发货"
            else -> "查看明细"
        }

        val state = when (type) {
            "立即发货" -> "待发货"
            else -> "已发货"
        }

        holder.setText(R.id.tv_state_type, type)
            .setText(R.id.tv_send_out_date, "2019年1月16日")
            .setText(R.id.tv_send_out_address, "上海市静安区 581 号")
            .setText(R.id.tv_send_out_state, state)

        holder.itemView.findViewById<TextView>(R.id.tv_state_type).setOnClickListener {
            when (type) {
                "立即发货" -> context.startActivity(Intent(context, SendOutNextActivity::class.java))
                else -> context.startActivity(Intent(context, SendOutLookActivity::class.java))
            }
        }

    }

}