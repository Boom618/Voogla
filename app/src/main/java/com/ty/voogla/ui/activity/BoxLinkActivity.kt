package com.ty.voogla.ui.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.adapter.BoxLinkAdapter
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.util.ToastUtil
import kotlinx.android.synthetic.main.activity_box_link_code.*

/**
 * @author TY on 2019/1/2.
 *
 * 箱码绑定 --> 扫码
 */
class BoxLinkActivity : BaseActivity() {

//    private val manager: AidcManager? = null


    lateinit var adapter: BoxLinkAdapter

    override val activityLayout: Int
        get() = R.layout.activity_box_link_code

    override fun onBaseCreate(savedInstanceState: Bundle?) {

    }

    override fun initOneData() {
    }

    override fun initTwoView() {

        initToolBar(R.string.box_link, "保存", View.OnClickListener {
            ToastUtil.showToast("保存信息")
        })

        val list = mutableListOf("a", "b", "c", "d", "e")
        LayoutInit.initLayoutManager(this, box_recycler)

        adapter = BoxLinkAdapter(this, R.layout.item_box_link, list)
        box_recycler.adapter = adapter

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {


        return super.onKeyDown(keyCode, event)
    }
}