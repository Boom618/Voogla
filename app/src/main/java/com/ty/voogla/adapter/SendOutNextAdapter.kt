package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.bean.sendout.SendOutListInfo
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.ui.activity.BoxLinkJavaActivity3
import com.ty.voogla.ui.activity.SendOutNextActivity2
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/14.
 */
class SendOutNextAdapter(val context: Context, layout: Int, datas: MutableList<SendOutListInfo.DeliveryDetailInfosBean>) :
    CommonAdapter<SendOutListInfo.DeliveryDetailInfosBean>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: SendOutListInfo.DeliveryDetailInfosBean, position: Int) {

        holder.setText(R.id.tv_send_amount_wait, info.deliveryNum)
            .setText(R.id.tv_select_pro_name, info.goodsName)
            .setText(R.id.tv_send_unit_number, info.unitNum)
            .setText(R.id.tv_box_amount, "${info.outBoxNum} 箱")
            .setText(R.id.tv_product_amount, "${info.outGoodsNum} 盒")

        holder.itemView.findViewById<TextView>(R.id.tv_scan_code_box).setOnClickListener {

            val intent = Intent(context, BoxLinkJavaActivity3::class.java)
            intent.putExtra(CodeConstant.SEND_POSITION,position)
            intent.putExtra("deliveryNum",info.deliveryNum)
            intent.putExtra("unitNum",info.unitNum)
            intent.putExtra("goodsNo",info.goodsNo)
            (context as SendOutNextActivity2).startActivityForResult(intent,CodeConstant.REQUEST_CODE_OUT)
        }

    }


}