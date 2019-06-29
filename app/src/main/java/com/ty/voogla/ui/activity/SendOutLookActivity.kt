package com.ty.voogla.ui.activity

import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutLookAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.sendout.OutPutInfoData
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.constant.TipString
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.FullDialog
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.LoadingDialog
import kotlinx.android.synthetic.main.activity_send_out_look.*

/**
 * @author TY on 2019/1/14.
 * 发货出库 - 查看
 */
class SendOutLookActivity : BaseActivity(), VooglaContract.View<OutPutInfoData> {

    // 发货单号
    private var deliveryNo: String = ""
    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_send_out_look

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        initToolBar(R.string.send_out)
        // 发货单编号
        deliveryNo = intent.getStringExtra(CodeConstant.DELIVERY_NO)
        presenter.getSendOutPutInfo(SimpleCache.userInfo.companyNo, deliveryNo)

    }

    override fun initTwoView() {


    }

    override fun showSuccess(data: OutPutInfoData) {

        val goodsInfo = data.outWareInfo
        val list = data.outQrCodeDetailInfos

        val addr = goodsInfo?.provinceLevel + goodsInfo?.cityLevel + goodsInfo?.countyLevel
        val addrAll = addr + goodsInfo?.deliveryAddress

        val stringAddr = when (addrAll.length >= 12) {
            true -> addr + "\n" + goodsInfo?.deliveryAddress
            else -> addrAll
        }

        tv_send_out_receipt.text = deliveryNo
        tv_send_out_date.text = goodsInfo?.outTime
        tv_send_out_address.text = stringAddr

        LayoutInit.initLayoutManager(this, recycler_view_send_look)
        recycler_view_send_look.adapter = SendOutLookAdapter(this, R.layout.item_send_out_look, list!!)

    }

    override fun showError(msg: String) {
        ToastUtil.showToast(msg)
    }

    override fun showResponse(response: ResponseInfo?) {
    }

    private var dialog: LoadingDialog? = null
    override fun showLoading() {
        dialog = FullDialog.showLoading(this, TipString.loading)
    }

    override fun hideLoading() {
        dialog?.dismiss()
    }
}