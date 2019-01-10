package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_pda.*

/**
 * @author TY on 2018/12/20.
 */
class MainPdaActivity : BaseActivity(){

    override val activityLayout: Int
        get() = R.layout.activity_main_pda

    override fun onBaseCreate(savedInstanceState: Bundle?) {

    }

    override fun initOneData() {

    }

    override fun initTwoView() {

        image_product.setOnClickListener {
            gotoActivity(BoxLinkActivity::class.java)
        }

        image_send.setOnClickListener {
            gotoActivity(BoxLinkActivity::class.java)
        }
        image_user.setOnClickListener {
            gotoActivity(UserContentActivity::class.java)
        }


    }
}
