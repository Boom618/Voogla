package com.ty.voogla.ui.activity

import android.os.Bundle
import com.honeywell.aidc.AidcManager
import com.honeywell.aidc.BarcodeReader
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_mob.*

/**
 * @author TY on 2019/1/2.
 *
 * 窜货稽查主页
 */
class MainMobActivity : BaseActivity() {

    private lateinit var barcodeReader: BarcodeReader
    private var manager: AidcManager? = null

    override val activityLayout: Int
        get() = R.layout.activity_main_mob

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {

        AidcManager.create(this) { aidcManager ->
            manager = aidcManager
            barcodeReader = manager!!.createBarcodeReader()
        }
    }

    override fun initTwoView() {
        iv_jc.setOnClickListener { gotoActivity(InspectionActivity::class.java) }
        iv_user.setOnClickListener { gotoActivity(UserContentActivity::class.java) }

    }

    override fun onBackPressed() {
        // exist app 会调用：onPause()和 onStop()
        moveTaskToBack(true)
    }

    companion object {


        var barcodeObject: BarcodeReader? = null
            private set
    }

    override fun onDestroy() {
        super.onDestroy()

        if (barcodeObject != null) {
            // close BarcodeReader to clean up resources.
            barcodeObject!!.close()
            barcodeObject = null
        }

        if (manager != null) {
            // close AidcManager to disconnect from the scanner service.
            // once closed, the object can no longer be used.
            manager!!.close()
        }
    }
}