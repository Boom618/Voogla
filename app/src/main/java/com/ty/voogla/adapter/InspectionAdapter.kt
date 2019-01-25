package com.ty.voogla.adapter

import android.content.Context
import android.widget.ImageView
import com.ty.voogla.R
import com.ty.voogla.bean.CheckInfoList
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.NormalAlertDialog
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/7.
 */
class InspectionAdapter(context: Context, layoutId: Int, datas: MutableList<CheckInfoList.ListBean>) :
    CommonAdapter<CheckInfoList.ListBean>(context, layoutId, datas) {

    override fun convert(holder: ViewHolder, info: CheckInfoList.ListBean, position: Int) {

        // tv_send_address  XML 设置 android:visibility="gone"
        holder.setText(R.id.tv_send_org, info.companyName)
            .setText(R.id.tv_number, info.deliveryNo)
            .setText(R.id.tv_receipt_org, info.customerName)
//            .setText(R.id.tv_send_address,info.deliveryAddress)
            .setText(R.id.tv_receipt_address, info.deliveryAddress)

//        holder.itemView.findViewById<ImageView>(R.id.image_confirm).setOnClickListener { view ->
//            DialogUtil.deleteItemDialog(view.context, "温馨提示", "确认窜货", NormalAlertDialog.onNormalOnclickListener {
//                ToastUtil.showToast("确认窜货")
//                it.dismiss()
//                datas.removeAt(position)
//                notifyItemRemoved(position)
//                notifyItemRangeChanged(position, datas.size - position)
//            })
//        }


    }
}