package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.activity_box_link_code.*

/**
 * @author TY on 2019/1/2.
 *
 * 箱码绑定 --> 扫码
 */
class BoxLinkDetailActivity : BaseActivity() {
    override val activityLayout: Int
        get() = R.layout.activity_box_link_code

    override fun onBaseCreate(savedInstanceState: Bundle?) {

    }

    override fun initOneData() {
    }

    override fun initTwoView() {
        bt_complete.setOnClickListener { }

    }
}