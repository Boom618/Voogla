package com.ty.voogla.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProIntoLookAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.produce.ProductInputInfo
import com.ty.voogla.constant.TipString
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.FullDialog
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.LoadingDialog
import kotlinx.android.synthetic.main.activity_product_into_look.*

/**
 * @author TY on 2019/1/14.
 * 查看生产入库
 */
class ProductIntoLookActivity : BaseActivity(), VooglaContract.View<ProductInputInfo> {


    private val presenter = VooglaPresenter(this)

    lateinit var adapter: ProIntoLookAdapter

    override val activityLayout: Int
        get() = R.layout.activity_product_into_look

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    @SuppressLint("SetTextI18n")
    override fun initOneData() {
        initToolBar(R.string.produce_into)

        // 入库批次号（生产入库手动输入）
        val batchNo = intent.getStringExtra("batchNo")
        // 系统自动生成的
        val batchNumber = intent.getStringExtra("batchNumber")
        val productName = intent.getStringExtra("productName")
        val productHouse = intent.getStringExtra("productHouse")
        val productTime = intent.getStringExtra("productTime")
        val productSpec = intent.getStringExtra("productSpec")
        val userName = SimpleCache.userInfo.userName

        tv_batch_number.text = batchNo
        tv_select_pro_name.text = "产品名称：$productName"
        tv_select_house.text = "所在仓库：$productHouse"
        tv_select_time.text = "入库时间：$productTime"
        tv_select_spec.text = "产品规格：$productSpec"
        tv_select_user.text = "操作员：$userName"

        presenter.getInputProductInfo(SimpleCache.userInfo.companyNo, batchNumber)

    }

    override fun initTwoView() {}


    override fun showSuccess(data: ProductInputInfo) {
        val list = data.inWareDetailInfos!!

        tv_number.text = list.size.toString()

        LayoutInit.initLayoutManager(this, house_look_recycler)
        // R.layout.item_box_link_look
        house_look_recycler.adapter = ProIntoLookAdapter(this, R.layout.item_house_look, list)

    }

    override fun showResponse(response: ResponseInfo) {

    }

    override fun showError(msg: String) {
        ToastUtil.showError(msg)
    }

    private var dialog: LoadingDialog? = null
    override fun showLoading() {
        dialog = FullDialog.showLoading(this, TipString.loading)
    }

    override fun hideLoading() {
        dialog?.dismiss()
    }

}