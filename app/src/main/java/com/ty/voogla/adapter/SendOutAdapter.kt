package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.bean.sendout.SendOutListData
import com.ty.voogla.ui.activity.SendOutLookActivity
import com.ty.voogla.ui.activity.SendOutNextActivity
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/14.
 */
class SendOutAdapter(val context: Context, layout: Int, datas: MutableList<SendOutListData.ListBean>) :
    CommonAdapter<SendOutListData.ListBean>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: SendOutListData.ListBean, position: Int) {

        val type = when {
            position % 2 == 0 -> "立即发货"
            else -> "查看明细"
        }

        holder.setText(R.id.tv_state_type, type)
            .setText(R.id.tv_send_out_date, info.deliveryDate)
            .setText(R.id.tv_send_out_address, info.deliveryAddress)
            .setText(R.id.tv_send_out_state, info.deliveryState)

        holder.itemView.findViewById<TextView>(R.id.tv_state_type).setOnClickListener {
            when (type) {
                "立即发货" -> context.startActivity(Intent(context, SendOutNextActivity::class.java))
                else -> context.startActivity(Intent(context, SendOutLookActivity::class.java))
            }
        }

    }

}