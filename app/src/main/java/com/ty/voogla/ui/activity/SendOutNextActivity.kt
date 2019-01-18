package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutNextAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.bean.sendout.QrCodeListData
import com.ty.voogla.bean.sendout.SendOutListInfo
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.SimpleCache
import com.ty.voogla.util.ToastUtil
import kotlinx.android.synthetic.main.activity_send_out_next.*

/**
 * @author TY on 2019/1/14.
 *
 * 发货出库 明细
 */
class SendOutNextActivity : BaseActivity(), VooglaContract.View<SendOutListInfo> {

    private val presenter = VooglaPresenter(this)

    private val qrCodeInfos = mutableListOf<QrCodeListData>()

    override val activityLayout: Int
        get() = R.layout.activity_send_out_next

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {

        // 发货单编号
        val deliveryNo = intent.getStringExtra(CodeConstant.DELIVERY_NO)
        presenter.getSendOutListInfo(SimpleCache.getUserInfo().companyNo, deliveryNo)

    }

    override fun initTwoView() {

        initToolBar(R.string.send_out_detail, "保存", View.OnClickListener {
            ToastUtil.showToast("保存信息")
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 出库扫码
        if (requestCode == CodeConstant.REQUEST_CODE_OUT && resultCode == CodeConstant.RESULT_CODE) {
            val qrCode = data?.getStringExtra(CodeConstant.BOX_CODE)
            val sendPosition = data?.getIntExtra(CodeConstant.SEND_POSITION,-1)
            val qrCodeClass = data?.getStringArrayListExtra(CodeConstant.QR_CODE_INFOS)

//            val data = QrCodeListData()
//            data.qrCode = qrCode
//            //data.qrCodeClass = qrCodeClass
//            qrCodeInfos.add(sendPosition!!,data)
        }
    }


    override fun showSuccess(data: SendOutListInfo) {

        val list = data.deliveryDetailInfos

        LayoutInit.initLayoutManager(this, recycler_view_send_next)
        recycler_view_send_next.adapter = SendOutNextAdapter(this, R.layout.item_send_out_next, list!!)

    }

    override fun showError(msg: String) {
    }
}