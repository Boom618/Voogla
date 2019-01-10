package com.ty.voogla.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * @author TY on 2019/1/10.
 */
object LayoutInit {

    fun initLayoutManager(context: Context,recycler_view:RecyclerView){
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
    }
}