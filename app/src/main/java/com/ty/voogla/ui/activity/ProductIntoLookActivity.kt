package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProIntoLookAdapter
import com.ty.voogla.base.BaseActivity
import kotlinx.android.synthetic.main.activity_product_into_look.*

/**
 * @author TY on 2019/1/14.
 * 查看生产入库
 */
class ProductIntoLookActivity : BaseActivity() {

    lateinit var adapter: ProIntoLookAdapter
    override val activityLayout: Int
        get() = R.layout.activity_product_into_look

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        initToolBar(R.string.produce_into)

        val batchNumber = intent.getStringExtra("batchNumber")
        val productName = intent.getStringExtra("productName")
        val productHous = intent.getStringExtra("productHouse")
        val productTime = intent.getStringExtra("productTime")
        val productSpec = intent.getStringExtra("productSpec")

        tv_batch_number.text = batchNumber
        tv_select_pro_name.text = productName
        tv_select_house.text = productHous
        tv_select_time.text = productTime
        tv_select_spec.text = productSpec

        val list = mutableListOf("1","2")

        LayoutInit.initLayoutManager(this, house_look_recycler)
        // R.layout.item_box_link_look
        house_look_recycler.adapter = ProIntoLookAdapter(this,R.layout.item_house_look,list)


    }

    override fun initTwoView() {

    }
}