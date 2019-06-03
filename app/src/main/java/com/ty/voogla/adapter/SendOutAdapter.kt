package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.bean.sendout.SendOutListData
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.ui.activity.SendOutLookActivity
import com.ty.voogla.ui.activity.SendOutNextActivity2
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/14.unitNum
 */
class SendOutAdapter(
    val context: Context,
    layout: Int,
    private val deliverySet: MutableSet<String>,
    datas: MutableList<SendOutListData.ListBean>
) :
    CommonAdapter<SendOutListData.ListBean>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: SendOutListData.ListBean, position: Int) {

        var type = when {
            info.deliveryState.equals("01") -> "立即发货"
            else -> "查看明细"
        }

        deliverySet.forEach {
            if (it.contains(info.deliveryNo)) {
                type = "继续发货"
            }
        }

        val addr = info.provinceLevel + info.cityLevel + info.countyLevel
        val addrAll = addr + info.deliveryAddress
        val stringAddr = when (addrAll.length >= 15) {
            true -> addr + "\n" + info.deliveryAddress
            else -> addrAll
        }
        holder.setText(R.id.tv_state_type, type)
            .setText(R.id.tv_send_out_date, info.deliveryDate)
            .setText(R.id.tv_responsible, info.shipperName)
            .setText(R.id.tv_goods_name, info.goodsName)
            .setText(R.id.tv_receiver_name, info.receiverName)
            .setText(R.id.tv_goods_number, "${info.deliveryNum} ${info.unitName}")
            .setText(R.id.tv_unit_number, "${info.unitNum} 个")
            .setText(R.id.tv_send_out_address, stringAddr)
            .setText(R.id.tv_bat_no, info.batchNo)

        holder.itemView.findViewById<TextView>(R.id.tv_state_type).setOnClickListener {
            when (type) {
                "查看明细" -> {
                    val intent = Intent(context, SendOutLookActivity::class.java)
                    intent.putExtra(CodeConstant.DELIVERY_NO, info.deliveryNo)
                    context.startActivity(intent)
                }
                else -> {
                    val intent = Intent(context, SendOutNextActivity2::class.java)
                    intent.putExtra(CodeConstant.DELIVERY_NO, info.deliveryNo)
                    context.startActivity(intent)
                }
            }
        }
    }

}