package com.ty.voogla.ui.activity

import android.os.Bundle
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity

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

        findViewById<View>(R.id.box_link).setOnClickListener{gotoActivity(BoxLinkActivity::class.java)}
        findViewById<View>(R.id.warehousing_into).setOnClickListener{gotoActivity(WarehousingIntoActivity::class.java)}
        findViewById<View>(R.id.warehousing_out).setOnClickListener{}


    }
}
