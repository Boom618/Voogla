package com.ty.voogla.ui.activity

import android.os.Bundle
import android.util.Log
import com.ty.voogla.BuildConfig.*
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.UserInfo
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.constant.TipString
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.FullDialog
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.util.WindowUtil
import com.ty.voogla.widght.LoadingDialog
import kotlinx.android.synthetic.main.activity_login_mob.*
import com.tencent.mmkv.MMKV

/**
 * @author TY on 2018/12/20.
 */
class LoginMobActivity : BaseActivity(), VooglaContract.View<UserInfo> {

    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_login_mob


    override fun onBaseCreate(savedInstanceState: Bundle?) {
        MMKV.initialize(this)
        // rootDir : /data/data/com.ty.voogla/files/mmkv

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

        login_mob.setOnClickListener { _ ->
            val name = et_user_name.text.toString().trim { it <= ' ' }
            val pass = et_user_pass.text.toString().trim { it <= ' ' }
            if (name.isNotEmpty() && pass.isNotEmpty()) {
                presenter.login(name, pass)
            } else {
                ToastUtil.showWarning(TipString.phoneAndPassNotNull)
            }
        }

    }

    override fun showSuccess(data: UserInfo) {
        // check@corp 稽查  im@corp PDA
        val httpPhone = data.roleNo.contains(CodeConstant.USER_PHONE)
        val httpPda = data.roleNo.contains(CodeConstant.USER_PDA)
        if (isPhone) {
            when (httpPhone) {
                true -> gotoActivity(MainMobActivity::class.java, true)
                false -> ToastUtil.showWarning(TipString.notPermission)
            }
        } else {
            when (httpPda) {
                true -> gotoActivity(MainPdaJavaActivity::class.java, true)
                false -> ToastUtil.showWarning(TipString.notPermission)
            }
        }
    }

    override fun showResponse(response: ResponseInfo) {

    }

    override fun showError(msg: String) {

        ToastUtil.showError(msg)
    }

    private var dialog: LoadingDialog? = null
    override fun showLoading() {
        dialog = FullDialog.showLoading(this, TipString.loading)
    }

    override fun hideLoading() {
        dialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.disposable()
    }
}
