package com.ty.voogla.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.util.WindowUtil
import kotlinx.android.synthetic.main.activity_login_voogla.*

/**
 * @author TY on 2018/12/20.
 */
class LoginActivity : BaseActivity(), VooglaContract.View {

    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_login_voogla


    override fun onBaseCreate(savedInstanceState: Bundle?) {

    }

    override fun initOneData() {

        // 低（120dpi）、中（160dpi）、高（240dpi）和超高（320dpi）
        // 手持机尺寸    screenHeight = 800  screenWidth = 480   display = 1.5  dpi = 240
        // p20 手机尺寸  screenHeight = 2240 screenWidth = 1080  display = 3.0  dpi = 480

        val getScreenWidth = WindowUtil.getScreenWidth()
        val getScreenHeight = WindowUtil.getScreenHeight()

        val screenDensity = WindowUtil.getScreenDensity()
        val screenDensityDpi = WindowUtil.getScreenDensityDpi()

        Log.e("TAG", "getScreenWidth = $getScreenWidth  getScreenHeight = $getScreenHeight")
        Log.e("TAG", "screenDensity = $screenDensity  screenDensityDpi = $screenDensityDpi")

    }

    override fun initTwoView() {

        login.setOnClickListener { _ ->
            val name = et_user_name!!.text.toString().trim { it <= ' ' }
            val pass = et_user_pass!!.text.toString().trim { it <= ' ' }
            gotoActivity(MainMobActivity::class.java, true)
            //presenter.getData(name, pass);
        }

        login_mob.setOnClickListener { gotoActivity(MainPdaActivity::class.java, true) }
    }

    override fun showSuccess() {
        gotoActivity(MainPdaActivity::class.java, true)
    }

    override fun showError(msg: String) {

        ToastUtil.showToast(msg)
    }
}
