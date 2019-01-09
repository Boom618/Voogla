package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import com.ty.voogla.BuildConfig.*
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.ui.ActivitiesHelper
import com.ty.voogla.util.SimpleCache
import kotlinx.android.synthetic.main.activity_user_content_mob.*

/**
 * @author TY on 2019/1/5.
 * 用户个人中心
 */
class UserContentActivity : BaseActivity() {
    override val activityLayout: Int
        get() = getLayout()


    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {


    }

    private fun getLayout(): Int {
        return if (isPhone) {
            R.layout.activity_user_content_mob
        } else {
            R.layout.activity_user_content_pda
        }
    }

    override fun initTwoView() {

        val userInfo = SimpleCache.getUserInfo()

        tv_company_profile.text = userInfo.companyName

        image_return.setOnClickListener { finish() }

        bt_login_out.setOnClickListener {
//            if (isPhone) {
//                val intent = Intent(this, LoginMobActivity::class.java)
//                startActivity(intent)
//            } else {
//                val intent = Intent(this, LoginPdaActivity::class.java)
//                startActivity(intent)
//            }
            val intent = Intent(this, LoginMobActivity::class.java)
            startActivity(intent)
            SimpleCache.clearAll()
            ActivitiesHelper.get().finishAll()
        }
    }
}