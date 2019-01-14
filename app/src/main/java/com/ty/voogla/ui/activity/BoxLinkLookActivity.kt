package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import com.ty.voogla.R
import com.ty.voogla.adapter.BoxLinkLookAdapter
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.activity_box_link_code_look.*

/**
 * @author TY on 2019/1/14.
 * 查看箱码关联
 */
class BoxLinkLookActivity : BaseActivity() {
    override val activityLayout: Int
        get() = R.layout.activity_box_link_code_look

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {

        val list = mutableListOf("a", "b")
        LayoutInit.initLayoutManager(this, box_look_recycler)
        box_look_recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        box_look_recycler.adapter = BoxLinkLookAdapter(this, R.layout.item_box_link_look, list)
    }

    override fun initTwoView() {
        initToolBar(R.string.box_link)
    }
}