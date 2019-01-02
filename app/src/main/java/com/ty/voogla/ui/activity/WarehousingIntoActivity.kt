package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.activity_housing_into.*

/**
 * @author TY on 2018/12/27.
 *
 * 生产入库
 */
class WarehousingIntoActivity : BaseActivity() {

    override val activityLayout: Int
        get() = R.layout.activity_housing_into

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {

    }

    override fun initTwoView() {

        bt_into.setOnClickListener { gotoActivity(WarehousingIntoDetailActivity::class.java) }
    }

}