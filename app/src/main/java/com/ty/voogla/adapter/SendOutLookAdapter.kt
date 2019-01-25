package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.bean.sendout.OutPutInfoData
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.data.SparseArrayUtil
import com.ty.voogla.ui.activity.BoxLinkLookActivity
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/14.
 */
class SendOutLookAdapter(val context: Context,layout:Int,datas:MutableList<OutPutInfoData.OutQrCodeDetailInfosBean>)
    :CommonAdapter<OutPutInfoData.OutQrCodeDetailInfosBean>(context,layout,datas){

    override fun convert(holder: ViewHolder, info: OutPutInfoData.OutQrCodeDetailInfosBean, position: Int) {

        // tv_send_amount_wait  设置了 android:visibility="gone"

        holder.setText(R.id.tv_select_pro_name,info.goodsNo)
            .setText(R.id.tv_send_amount_wait,"?")
            .setText(R.id.tv_box_amount,info.outBoxNum)
            .setText(R.id.tv_product_amount,info.outGoodsNum)

        holder.itemView.findViewById<TextView>(R.id.tv_code_detail).setOnClickListener {
            val list = info.qrCodeInfos!!

            val intent = Intent(context,BoxLinkLookActivity::class.java)
            intent.putExtra(CodeConstant.LOOK_TYPE,"send_out")
            SparseArrayUtil.putQrCodeListLook(context,list)
            context.startActivity(intent)
        }
    }


}