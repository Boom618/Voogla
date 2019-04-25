package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ty.voogla.BuildConfig
import com.ty.voogla.BuildConfig.*
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.data.SharedP
import com.ty.voogla.ui.ActivitiesHelper
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.util.ToastUtil
import kotlinx.android.synthetic.main.activity_user_content_mob.*

/**
 * @author TY on 2019/1/5.
 * 用户个人中心
 */
class UserContentActivity : BaseActivity() {
    override val activityLayout: Int
        get() = R.layout.activity_user_content_mob


    override fun onBaseCreate(savedInstanceState: Bundle?) {}

    override fun initOneData() {
        when (isPhone) {
            true -> {
                tv_scan_sett.visibility = View.GONE
                velocity.visibility = View.GONE
                tv_light_sett.visibility = View.GONE
                aimLight.visibility = View.GONE
            }
        }
    }

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
        setSwitchVelocity()
        setAimLight()
    }

    /**
     * 设置扫码速度
     */
    private fun setSwitchVelocity() {
        when (SharedP.getKeyBoolean(this, CodeConstant.SP_VELOCITY)) {
            false -> velocity.isChecked = false
            true -> velocity.isChecked = true
        }
        velocity.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                false -> {
                    ToastUtil.showSuccess("慢")
                    SharedP.putKeyBoolean(this, CodeConstant.SP_VELOCITY, false)
                }
                true -> {
                    ToastUtil.showSuccess("快")
                    SharedP.putKeyBoolean(this, CodeConstant.SP_VELOCITY, true)
                }
            }
        }
    }

    /**
     * 设置补光
     */
    private fun setAimLight() {
        when (SharedP.getKeyBoolean(this, CodeConstant.SP_LIGHT)) {
            false -> aimLight.isChecked = false
            true -> aimLight.isChecked = true
        }
        aimLight.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                false -> SharedP.putKeyBoolean(this, CodeConstant.SP_LIGHT, false)
                true -> SharedP.putKeyBoolean(this, CodeConstant.SP_LIGHT, true)
            }
            ToastUtil.showSuccess(if (isChecked) "开" else "关")
        }
    }
}