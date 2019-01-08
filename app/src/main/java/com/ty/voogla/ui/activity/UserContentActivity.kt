package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.ui.ActivitiesHelper
import kotlinx.android.synthetic.main.activity_user_content.*

/**
 * @author TY on 2019/1/5.
 * 用户个人中心
 */
class UserContentActivity : BaseActivity() {
    override val activityLayout: Int
        get() = R.layout.activity_user_content

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {



    }

    override fun initTwoView() {

        tv_company_profile.text = "敷尔佳科技发展有限公司"

        image_return.setOnClickListener { finish() }

        bt_login_out.setOnClickListener { ActivitiesHelper.get().finishAll() }
    }
}