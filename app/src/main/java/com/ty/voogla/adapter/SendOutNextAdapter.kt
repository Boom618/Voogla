package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.ui.activity.BoxLinkJavaActivity
import com.ty.voogla.widght.DialogUtil
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/14.
 */
class SendOutNextAdapter(val context: Context, layout: Int, datas: MutableList<String>) :
    CommonAdapter<String>(context, layout, datas) {

    override fun convert(holder: ViewHolder, t: String?, position: Int) {

        holder.itemView.findViewById<TextView>(R.id.tv_select_pro_name).setOnClickListener {

            DialogUtil.selectSendName(context, datas)
        }

        holder.setText(R.id.tv_send_amount_wait, "1$position 箱")
            .setText(R.id.tv_box_amount, "5$position 箱")
            .setText(R.id.tv_product_amount, "2$position 箱")

        holder.itemView.findViewById<TextView>(R.id.tv_scan_code_box).setOnClickListener {

            val intent = Intent(context, BoxLinkJavaActivity::class.java)
            intent.putExtra(CodeConstant.PAGE_STATE_KEY,CodeConstant.PAGE_SCAN_OUT)
            context.startActivity(intent)
        }

    }


}