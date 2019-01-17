package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.ui.activity.ProductIntoLookActivity
import com.ty.voogla.util.SimpleCache
import com.ty.voogla.util.ToastUtil
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/10.
 *
 * 生产入库
 */
class ProductIntoAdapter(val context: Context, layout: Int, datas: MutableList<String>) :
    CommonAdapter<String>(context, layout, datas) {

    override fun convert(holder: ViewHolder, t: String?, position: Int) {
        val userName = SimpleCache.getUserInfo().userName

        holder.setText(R.id.tv_product_name, "甲酸钠修复帖")
            .setText(R.id.tv_into_user, userName)


        holder.itemView.findViewById<TextView>(R.id.image_confirm).setOnClickListener {
            val intent = Intent(context, ProductIntoLookActivity::class.java)
            intent.putExtra("batchNumber", "1726789765678909876")
            intent.putExtra("productName", "甲酸钠修复帖")
            intent.putExtra("productHouse", "敷尔佳上海仓")
            intent.putExtra("productTime", "2019年1月15日")
            intent.putExtra("productSpec", "30件/箱")
            context.startActivity(intent)
        }


    }
}