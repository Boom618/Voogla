package com.ty.voogla.ui.activity

import android.os.Bundle
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutNextAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.util.ToastUtil
import kotlinx.android.synthetic.main.activity_send_out_next.*

/**
 * @author TY on 2019/1/14.
 *
 * 发货出库 明细
 */
class SendOutNextActivity : BaseActivity() {

    override val activityLayout: Int
        get() = R.layout.activity_send_out_next

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {

        val list = mutableListOf("a", "b", "c")

        LayoutInit.initLayoutManager(this, recycler_view_send_next)
        recycler_view_send_next.adapter = SendOutNextAdapter(this, R.layout.item_send_out_next, list)
    }

    override fun initTwoView() {

        initToolBar(R.string.send_out_detail, "保存", View.OnClickListener {
            ToastUtil.showToast("保存信息")
        })
    }
}