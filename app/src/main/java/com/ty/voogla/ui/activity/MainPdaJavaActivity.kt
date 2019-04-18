package com.ty.voogla.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.ty.voogla.R
import com.ty.voogla.base.BaseActivity

/**
 * @author TY on 2019/1/12.
 */
class MainPdaJavaActivity : BaseActivity(), View.OnClickListener {

    override val activityLayout: Int
        get() = R.layout.activity_main_pda

    override fun onBaseCreate(savedInstanceState: Bundle?) {

    }

    override fun initOneData() {

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    override fun initTwoView() {

        val product = findViewById<ImageView>(R.id.image_product)
        val send = findViewById<ImageView>(R.id.image_send)
        val user = findViewById<ImageView>(R.id.image_user)
        val back = findViewById<Button>(R.id.back_goods)
        setViewOnClickListener(this, product, send, user,back)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.image_product -> gotoActivity(ProduceIntoActivity::class.java)
            R.id.image_send -> gotoActivity(SendOutActivity::class.java)
            R.id.image_user -> gotoActivity(UserContentActivity::class.java)
            R.id.back_goods -> gotoActivity(BackGoodsActivity::class.java)
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
