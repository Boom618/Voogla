package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.bean.produce.ProductInputInfo
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.ui.activity.BoxLinkLookActivity
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/11.
 *
 * 生产入库查看
 */
class ProIntoLookAdapter(val context: Context, layout: Int, datas: List<ProductInputInfo.InWareDetailInfosBean>) :
    CommonAdapter<ProductInputInfo.InWareDetailInfosBean>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: ProductInputInfo.InWareDetailInfosBean, position: Int) {

        holder.setText(R.id.tv_code, info.qrCode)

        holder.itemView.findViewById<TextView>(R.id.tv_look).setOnClickListener {
            val intent = Intent(context, BoxLinkLookActivity::class.java)

            intent.putExtra(CodeConstant.LOOK_TYPE,"product")
            intent.putExtra("qrCode",info.qrCode)
            context.startActivity(intent)
        }


    }
}