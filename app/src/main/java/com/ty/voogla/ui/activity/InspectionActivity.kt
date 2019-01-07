package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.ThemedSpinnerAdapter
import com.ty.voogla.R
import com.ty.voogla.adapter.InspectionAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.bean.InspectionData
import kotlinx.android.synthetic.main.activity_inspection.*

/**
 * @author TY on 2019/1/7.
 * 稽查主页
 */
class InspectionActivity : BaseActivity() {

    lateinit var adapter: InspectionAdapter

//    val list: MutableList<InspectionData.ListBean>? = null

    val list:ArrayList<String> = ArrayList(10)

    override val activityLayout: Int
        get() = R.layout.activity_inspection

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
    }

    override fun initTwoView() {

        list.add("12345")
        list.add("12345")
        list.add("12345")
        list.add("12345")
        initToolBar(R.string.inspection_system)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        adapter = InspectionAdapter(this, R.layout.item_inspection, list)

        recycler_view.adapter = adapter


    }
}