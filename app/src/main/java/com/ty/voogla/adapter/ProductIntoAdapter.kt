package com.ty.voogla.adapter

import android.content.Context
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.util.ToastUtil
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/10.
 *
 * 生产入库
 */
class ProductIntoAdapter(context: Context,layout:Int,datas:MutableList<String>)
    :CommonAdapter<String>(context,layout,datas){

    override fun convert(holder: ViewHolder, t: String?, position: Int) {

        holder.setText(R.id.tv_product_name,"甲酸钠修复帖")


        holder.itemView.findViewById<TextView>(R.id.image_confirm).setOnClickListener {
            ToastUtil.showToast("查看明细")
        }



    }
}