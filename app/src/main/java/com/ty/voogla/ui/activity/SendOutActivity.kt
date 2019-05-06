package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.sendout.SendOutListData
import com.ty.voogla.constant.TipString
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.ui.fragment.WaitShipFragment
import com.ty.voogla.util.FullDialog
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.LoadingDialog
import kotlinx.android.synthetic.main.activity_send_out.*
import kotlinx.android.synthetic.main.content_list_fragment.*
import kotlinx.android.synthetic.main.content_list_fragment.view.*

/**
 * @author TY on 2019/1/14.
 * 发货出库
 */
class SendOutActivity : BaseActivity(), VooglaContract.ListView<SendOutListData.ListBean> {

//    private val mFragment = mutableListOf<Fragment>()

    private val presenter = VooglaPresenter(this)
    private val listData: MutableList<SendOutListData.ListBean> = mutableListOf()

    private var companyNo: String? = null

    private var adapter: SendOutAdapter? = null

    override val activityLayout: Int
        get() = R.layout.activity_send_out

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        refreshLayout_out.setColorSchemeResources(
            android.R.color.holo_blue_light, android.R.color.holo_red_light,
            android.R.color.holo_orange_light, android.R.color.holo_green_light
        )
        refreshLayout_out.setOnRefreshListener {
            presenter.getSendOutList2(companyNo, "01")
        }
        LayoutInit.initLayoutManager(this, recyclerView_out)
        adapter = SendOutAdapter(this, R.layout.item_send_out, listData)
        recyclerView_out.adapter = adapter
    }

    override fun initOneData() {
        initToolBar(R.string.send_out)
        companyNo = SimpleCache.getUserInfo().companyNo
        presenter.getSendOutList2(companyNo, "01")

    }

    override fun initTwoView() {}


    override fun showSuccess(data: MutableList<SendOutListData.ListBean>) {
        refreshLayout_out.isRefreshing = false
        listData.clear()
        listData.addAll(data)
        adapter?.notifyDataSetChanged()
    }

    override fun showError(msg: String) {
        refreshLayout.isRefreshing = false
        ToastUtil.showError(msg)
    }

    override fun showResponse(response: ResponseInfo) {
        ToastUtil.showSuccess("成功")
    }

    private var dialog: LoadingDialog? = null
    override fun showLoading() {
        dialog = FullDialog.showLoading(this, TipString.loading)
    }

    override fun hideLoading() {
        dialog?.dismiss()
    }

}
