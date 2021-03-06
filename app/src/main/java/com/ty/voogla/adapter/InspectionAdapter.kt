package com.ty.voogla.adapter

import android.content.Context
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

        // 是否窜货(01否,02是)
        val stateText = if (info.fleeFlag == "02") "取消窜货" else "确认窜货"
        val stateName = if (info.fleeFlag == "02") "已窜货" else "正常"

        // tv_send_address  XML 设置 android:visibility="gone"
        val addr = info.provinceLevel + info.cityLevel + info.countyLevel
        val stringAddr = when (info.deliveryAddress!!.length >= 6) {
           true -> {
               addr + "\n" + info.deliveryAddress
            }
            else -> {
                addr + info.deliveryAddress
            }
        }

        holder.setText(R.id.tv_send_org, info.companyName)
            .setText(R.id.tv_confirm, stateText)
            .setText(R.id.tv_number, info.deliveryNo)
            .setText(R.id.tv_receipt_org, info.customerName)
            .setText(R.id.tv_receipt_address, stringAddr)
            .setText(R.id.tv_receipt_state, stateName)

    }
}