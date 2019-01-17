package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.bean.sendout.SendOutListData
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.SimpleCache
import com.ty.voogla.util.ToastUtil
import kotlinx.android.synthetic.main.activity_send_out.*

/**
 * @author TY on 2019/1/14.
 * 发货出库
 */
class SendOutActivity : BaseActivity(), VooglaContract.ListView<SendOutListData.ListBean> {

    private val presenter = VooglaPresenter(this)
    private val listData: MutableList<SendOutListData.ListBean> = mutableListOf()

    override val activityLayout: Int
        get() = R.layout.activity_send_out

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        initToolBar(R.string.send_out)
        presenter.getSendOutList(SimpleCache.getUserInfo().companyNo)
    }

    override fun initTwoView() {


    }

    override fun showSuccess(data: MutableList<SendOutListData.ListBean>) {
        listData.addAll(data)
        LayoutInit.initLayoutManager(this, recycler_view_send)
        recycler_view_send.adapter = SendOutAdapter(this, R.layout.item_send_out, data)

    }

    override fun showError(msg: String) {
        ToastUtil.showToast(msg)
    }
}