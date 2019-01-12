package com.ty.voogla.ui.activity

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProductIntoAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.ToastUtil
import kotlinx.android.synthetic.main.activity_product_into.*

/**
 * @author TY on 2018/12/20.
 *
 *
 * 生产入库（PDA 端）
 */
class ProduceIntoActivity : BaseActivity(), VooglaContract.View {

    lateinit var adapter: ProductIntoAdapter


    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_product_into


    override fun onBaseCreate(savedInstanceState: Bundle?) {

    }

    override fun initOneData() {
        presenter.getProduceList()

    }

    override fun initTwoView() {

        initToolBar(R.string.produce_into)


        search_view.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val tempString = v.text.toString().trim { it <= ' ' }
                ToastUtil.showToast(tempString)
            }
            true
        }



        tv_product.setOnClickListener { gotoActivity(ProduceIntoDetailActivity::class.java) }

    }

    override fun showSuccess() {

        val list: MutableList<String> = mutableListOf("北京", "上海", "广东", "杭州", "天津")
//        val list: ArrayList<String> = ArrayList(10)
//        list.add("121")
        list.add("121")

        LayoutInit.initLayoutManager(this,recycler_view_pro)
//        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        recycler_view_pro.layoutManager = layoutManager

        adapter = ProductIntoAdapter(this, R.layout.item_produce_into, list)
        recycler_view_pro.adapter = adapter

    }

    override fun showError(msg: String) {

    }
}
