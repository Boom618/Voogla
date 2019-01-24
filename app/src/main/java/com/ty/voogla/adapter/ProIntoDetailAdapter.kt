package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.util.SparseArray
import android.widget.ImageView
import com.ty.voogla.R
import com.ty.voogla.bean.produce.InBoxCodeDetailInfosBean
import com.ty.voogla.bean.sendout.QrCodeListData
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.ui.activity.BoxLinkJavaActivity
import com.ty.voogla.util.ToastUtil
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

        val editView = holder.itemView.findViewById<ImageView>(R.id.image_edit)
//        val deleteView = holder.itemView.findViewById<ImageView>(R.id.image_delete)

        editView.setOnClickListener {
            val intent = Intent(context, BoxLinkJavaActivity::class.java)
            // boxCode 箱码  qrCodeInfos 产品 list
            intent.putExtra(CodeConstant.PAGE_STATE_KEY,CodeConstant.PAGE_BOX_LINK_EDIT)
            intent.putExtra("boxCode",info.qrCode)
            intent.putExtra("position",position)
            context.startActivity(intent)
            //ToastUtil.showToast("修改 $position")
        }

//        deleteView.setOnClickListener {
//
//            datas.removeAt(position)
//            notifyItemRemoved(position)
//            notifyItemRangeChanged(position,datas.size - position)
//        }
    }
}