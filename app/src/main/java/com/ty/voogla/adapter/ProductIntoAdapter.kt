package com.ty.voogla.adapter

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.bean.produce.ProductIntoData
import com.ty.voogla.ui.activity.ProductIntoLookActivity
import com.ty.voogla.data.SimpleCache
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/10.
 *
 * 生产入库
 */
class ProductIntoAdapter(val context: Context, layout: Int, datas: MutableList<ProductIntoData.ListBean>) :
    CommonAdapter<ProductIntoData.ListBean>(context, layout, datas) {

    override fun convert(holder: ViewHolder, info: ProductIntoData.ListBean, position: Int) {
        val userName = SimpleCache.getUserInfo().userName

        holder.setText(R.id.tv_number, info.inBatchNo)
            .setText(R.id.tv_product_name, info.goodsName)
            .setText(R.id.tv_into_user, userName)
            .setText(R.id.tv_product_spec, info.unit)
            .setText(R.id.tv_into_number, info.inNum.toString())
            .setText(R.id.tv_into_time, info.inTime)
            .setText(R.id.tv_into_address, info.wareName)


        holder.itemView.findViewById<TextView>(R.id.image_confirm).setOnClickListener {
            val intent = Intent(context, ProductIntoLookActivity::class.java)
            intent.putExtra("batchNumber", info.inBatchNo)
            intent.putExtra("productName", info.goodsName)
            intent.putExtra("productHouse", info.wareName)
            intent.putExtra("productTime", info.inTime)
            intent.putExtra("productSpec", info.unit)
            context.startActivity(intent)
        }


    }
}