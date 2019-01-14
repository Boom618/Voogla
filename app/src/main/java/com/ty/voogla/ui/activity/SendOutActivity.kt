package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutAdapter
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.activity_send_out.*

/**
 * @author TY on 2019/1/14.
 * 发货出库
 */
class SendOutActivity : BaseActivity() {
    override val activityLayout: Int
        get() = R.layout.activity_send_out

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        initToolBar(R.string.send_out)
    }

    override fun initTwoView() {

        val list = mutableListOf("a", "b", "c")
        LayoutInit.initLayoutManager(this, recycler_view_send)
        recycler_view_send.adapter = SendOutAdapter(this, R.layout.item_send_out, list)
    }
}