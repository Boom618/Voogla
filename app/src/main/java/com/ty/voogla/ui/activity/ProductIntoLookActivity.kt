package com.ty.voogla.ui.activity

import android.os.Bundle
import android.util.SparseArray
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProIntoLookAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.bean.produce.ProductInputInfo
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.util.SimpleCache
import kotlinx.android.synthetic.main.activity_product_into_look.*

/**
 * @author TY on 2019/1/14.
 * 查看生产入库
 */
class ProductIntoLookActivity : BaseActivity(), VooglaContract.View<ProductInputInfo> {


    // 箱码和产品码
    private var boxCode: String? = null
    private var qrCodeInfos: MutableList<String>? = null

    // 箱码和产品码 列表
    private var boxCodeList:MutableList<String> = mutableListOf()
    private var qrCodeList:SparseArray<MutableList<String>> = SparseArray()

    private val presenter = VooglaPresenter(this)

    lateinit var adapter: ProIntoLookAdapter

    override val activityLayout: Int
        get() = R.layout.activity_product_into_look

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        initToolBar(R.string.produce_into)

        // 入库批次号
        val batchNumber = intent.getStringExtra("batchNumber")
        val productName = intent.getStringExtra("productName")
        val productHous = intent.getStringExtra("productHouse")
        val productTime = intent.getStringExtra("productTime")
        val productSpec = intent.getStringExtra("productSpec")

        tv_select_pro_name.text = "产品名称：$productName"
        tv_select_house.text = "所在仓库：$productHous"
        tv_select_time.text = productTime
        tv_select_spec.text = productSpec

        presenter.getInputProductInfo(SimpleCache.getUserInfo().companyNo,batchNumber)


    }

    override fun initTwoView() {


    }


    override fun showSuccess(data: ProductInputInfo) {
        val wareInfo = data.inWareInfo
        val list = data.inWareDetailInfos
        tv_batch_number.text = wareInfo?.productBatchNo

        for (i in 0 until list!!.size) {
            boxCodeList.add(i,list[i].boxCode!!)
            qrCodeList.put(i,list[i].qrCodeInfos)
        }

        LayoutInit.initLayoutManager(this, house_look_recycler)
        // R.layout.item_box_link_look
        house_look_recycler.adapter = ProIntoLookAdapter(this, R.layout.item_house_look, boxCodeList)

    }

    override fun showError(msg: String) {

    }

}