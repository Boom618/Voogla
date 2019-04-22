package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.ty.voogla.R
import com.ty.voogla.bean.produce.InBoxCodeDetailInfosBean
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.ui.activity.BoxLinkJavaActivity2
import com.ty.voogla.ui.activity.ProduceIntoDetailActivity
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/11.
 *
 * 生产入库详情
 */
class ProIntoDetailAdapter(val context: Context, layout: Int,datas: MutableList<InBoxCodeDetailInfosBean>) :
    CommonAdapter<InBoxCodeDetailInfosBean>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: InBoxCodeDetailInfosBean, position: Int) {

        holder.setText(R.id.tv_code, info.qrCode)
            .setText(R.id.tv_company_box,"企业箱号：${info.comBoxCode}")

        val editView = holder.itemView.findViewById<ImageView>(R.id.image_edit)

        editView.setOnClickListener {
            val intent = Intent(context, BoxLinkJavaActivity2::class.java)
            intent.putExtra(CodeConstant.PAGE_STATE_KEY,CodeConstant.PAGE_BOX_LINK_EDIT)
            intent.putExtra("boxCode",info.qrCode)
            intent.putExtra("position",position)
            intent.putExtra("comBoxCode",info.comBoxCode)
            (context as ProduceIntoDetailActivity).startActivityForResult(intent, CodeConstant.REQUEST_CODE_INTO)
        }

    }
}