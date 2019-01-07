package com.ty.voogla.adapter

import android.content.Context
import com.ty.voogla.bean.InspectionData
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder

/**
 * @author TY on 2019/1/7.
 */
class InspectionAdapter(context: Context,layoutId:Int,datas:MutableList<String>)
    : CommonAdapter<String>(context,layoutId,datas) {

    override fun convert(holder: ViewHolder?, t: String, position: Int) {


    }
}