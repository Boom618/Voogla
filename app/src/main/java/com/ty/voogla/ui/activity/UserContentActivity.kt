package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import com.ty.voogla.BuildConfig.*
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.ui.ActivitiesHelper
import com.ty.voogla.data.SimpleCache
import kotlinx.android.synthetic.main.activity_user_content_mob.*

/**
 * @author TY on 2019/1/5.
 * 用户个人中心
 */
class UserContentActivity : BaseActivity() {
    override val activityLayout: Int
        get() = R.layout.activity_user_content_mob


    override fun onBaseCreate(savedInstanceState: Bundle?) {}

    override fun initOneData() {}

    override fun initTwoView() {

        val userInfo = SimpleCache.getUserInfo()

        tv_user_name.text = userInfo.userName
        tv_user_phone.text = userInfo.companyNo

        tv_company_profile.text = userInfo.companyName

        image_return.setOnClickListener { finish() }

        bt_login_out.setOnClickListener {

            val intent = Intent(this, LoginMobActivity::class.java)
            startActivity(intent)
            // 清数据
            SimpleCache.clearAll()
            ActivitiesHelper.get().finishAll()
        }
    }
}