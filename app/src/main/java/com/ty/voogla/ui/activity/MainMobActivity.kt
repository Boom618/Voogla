package com.ty.voogla.ui.activity

import android.content.Context
import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_mob.*

/**
 * @author TY on 2019/1/2.
 *
 * 窜货稽查主页
 */
class MainMobActivity : BaseActivity() {
    override val activityLayout: Int
        get() = R.layout.activity_main_mob

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
    }

    override fun initTwoView() {
        getSharedPreferences("key", Context.MODE_PRIVATE).edit()
            .putBoolean("kye", false)
            .apply()

        val string = "www.baidu.com"

        iv_jc.setOnClickListener { gotoActivity(InspectionActivity::class.java) }
        iv_user.setOnClickListener { gotoActivity(UserContentActivity::class.java) }


    }
}