package com.ty.voogla.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.ToastUtil
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

    }

    override fun initTwoView() {

        login!!.setOnClickListener {
            val name = et_user_name!!.text.toString().trim { it <= ' ' }
            val pass = et_user_pass!!.text.toString().trim { it <= ' ' }
            gotoActivity(MainActivity::class.java, true)
            //presenter.getData(name, pass);
        }

    }

    override fun showSuccess() {
        gotoActivity(MainActivity::class.java, true)
    }

    override fun showError(msg: String) {

        ToastUtil.showToast(msg)
    }
}
