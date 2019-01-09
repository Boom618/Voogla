//package com.ty.voogla.ui.activity
//
//import android.os.Bundle
//import com.ty.voogla.R
//import com.ty.voogla.base.BaseActivity
//import com.ty.voogla.mvp.contract.VooglaContract
//import com.ty.voogla.mvp.presenter.VooglaPresenter
//import com.ty.voogla.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_login_pda.*
//
///**
// * @author TY on 2018/12/20.
// */
//class LoginPdaActivity : BaseActivity(), VooglaContract.View {
//
//    private val presenter = VooglaPresenter(this)
//
//    override val activityLayout: Int
//        get() = R.layout.activity_login_pda
//
//
//    override fun onBaseCreate(savedInstanceState: Bundle?) {
//
//    }
//
//    override fun initOneData() {
//
//    }
//
//    override fun initTwoView() {
//
//        login_pda.setOnClickListener { _ ->
//            val name = et_user_name.text.toString().trim { it <= ' ' }
//            val pass = et_user_pass.text.toString().trim { it <= ' ' }
//            presenter.getData(name, pass)
//        }
//    }
//
//    override fun showSuccess() {
//        gotoActivity(MainPdaActivity::class.java, true)
//    }
//
//    override fun showError(msg: String) {
//
//        ToastUtil.showToast(msg)
//    }
//}
