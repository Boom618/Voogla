package com.ty.voogla.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.ty.voogla.R
import com.ty.voogla.bean.CheckInfoList
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/7.
 */
class InspectionAdapter(context: Context, layoutId: Int, datas: MutableList<CheckInfoList.ListBean>) :
    CommonAdapter<CheckInfoList.ListBean>(context, layoutId, datas) {

    override fun convert(holder: ViewHolder, info: CheckInfoList.ListBean, position: Int) {

        val state = if (info.deliveryState == "01") View.GONE else View.VISIBLE

        holder.itemView.findViewById<ImageView>(R.id.image_confirm).visibility = state
        // tv_send_address  XML 设置 android:visibility="gone"
        val addr = info.provinceLevel + info.cityLevel + info.countyLevel + info.deliveryAddress
        holder.setText(R.id.tv_send_org, info.companyName)
            .setText(R.id.tv_number, info.deliveryNo)
            .setText(R.id.tv_receipt_org, info.customerName)
            .setText(R.id.tv_receipt_address, addr)
            .setText(R.id.tv_receipt_state, info.deliveryStateName)

    }
}