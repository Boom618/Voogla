package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import com.ty.voogla.R
import com.ty.voogla.adapter.BoxLinkLookAdapter
import com.ty.voogla.adapter.BoxLinkProLookAdapter
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.constant.CodeConstant
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


        val type = intent.getStringExtra(CodeConstant.LOOK_TYPE)

        LayoutInit.initLayoutManager(this, box_look_recycler)
        box_look_recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        if (type == "product") {
            // 入库查看
            val qrCodeInfos = intent.getStringArrayListExtra("qrCodeInfos")
            box_look_recycler.adapter = BoxLinkProLookAdapter(this, R.layout.item_box_link_look,qrCodeInfos)
        }else{
            // 出库查看
            val qrCodeListData = SparseArrayUtil.getQrCodeList(this)
            box_look_recycler.adapter = BoxLinkLookAdapter(this, R.layout.item_box_link_look, qrCodeListData)
        }



    }

    override fun initTwoView() {
        initToolBar(R.string.box_link)
    }
}