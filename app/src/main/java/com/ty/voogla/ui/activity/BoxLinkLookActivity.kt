package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import com.ty.voogla.R
import com.ty.voogla.adapter.BoxLinkLookAdapter
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.data.SparseArrayUtil
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


        val type = intent.getStringExtra("product")
        val position = intent.getIntExtra("position",0)
        val qrCodeListData = SparseArrayUtil.getQrCodeListData(position)

        LayoutInit.initLayoutManager(this, box_look_recycler)
        box_look_recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        if (type == "product") {
            // 发货查看
        }else{
            // 出库查看
            box_look_recycler.adapter = BoxLinkLookAdapter(this, R.layout.item_box_link_look, qrCodeListData)
        }



    }

    override fun initTwoView() {
        initToolBar(R.string.box_link)
    }
}