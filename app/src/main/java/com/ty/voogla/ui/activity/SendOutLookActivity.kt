package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutLookAdapter
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.activity_send_out_look.*

/**
 * @author TY on 2019/1/14.
 * 发货出库 - 查看
 */
class SendOutLookActivity : BaseActivity() {
    override val activityLayout: Int
        get() = R.layout.activity_send_out_look

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        initToolBar(R.string.send_out)

        tv_send_out_receipt.text = "123456788765434567"
        tv_send_out_date.text = "2019年1月1日"
        tv_send_out_address.text = "上海市静安区光复路 581 号"
    }

    override fun initTwoView() {

        val list = mutableListOf("a", "b")
        LayoutInit.initLayoutManager(this, recycler_view_send_look)
//        recycler_view_send_look.adapter = BoxLinkLookAdapter(this, R.layout.item_box_link_look, list)
        recycler_view_send_look.adapter = SendOutLookAdapter(this,R.layout.item_send_out_look,list)

    }
}