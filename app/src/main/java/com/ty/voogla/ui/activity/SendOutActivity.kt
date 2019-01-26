package com.ty.voogla.ui.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.sendout.SendOutListData
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.NormalAlertDialog
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.activity_send_out.*

/**
 * @author TY on 2019/1/14.
 * 发货出库
 */
class SendOutActivity : BaseActivity(), VooglaContract.ListView<SendOutListData.ListBean> {

    private val presenter = VooglaPresenter(this)
    private val listData: MutableList<SendOutListData.ListBean> = mutableListOf()

    private var companyNo:String? = null

    override val activityLayout: Int
        get() = R.layout.activity_send_out

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        initToolBar(R.string.send_out)
        companyNo = SimpleCache.getUserInfo().companyNo
        presenter.getSendOutList(companyNo)
    }

    override fun initTwoView() {


    }

    override fun showSuccess(data: MutableList<SendOutListData.ListBean>) {
        listData.addAll(data)
        LayoutInit.initLayoutManager(this, recycler_view_send)
        val adapter = SendOutAdapter(this, R.layout.item_send_out, data)
        recycler_view_send.adapter = adapter

        adapter.setOnItemClickListener(object :MultiItemTypeAdapter.OnItemClickListener{
            override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {

                val deliveryNo = listData[position].deliveryNo!!
                if (listData[position].deliveryState == "02") {
                    DialogUtil.deleteItemDialog(view.context, "温馨提示","确认删除", NormalAlertDialog.onNormalOnclickListener {

                        presenter.deleteSendOut(companyNo, deliveryNo)

                        it.dismiss()
                    })
                }
                return true
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {
            }

        })

    }

    override fun showResponse(response: ResponseInfo) {
        presenter.getSendOutList(companyNo)
    }

    override fun showError(msg: String) {
        ToastUtil.showToast(msg)
    }
}