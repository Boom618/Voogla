package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.acitivity_housing_into_detail.*

/**
 * @author TY on 2019/1/2.
 *
 * 生产入库 确认
 */
class WarehousingIntoDetailActivity : BaseActivity() {
    override val activityLayout: Int
        get() = R.layout.acitivity_housing_into_detail

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
    }

    override fun initTwoView() {

        bt_confirm.setOnClickListener {}
    }
}