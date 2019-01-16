package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProIntoDetailAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.bean.AddProduct
import com.ty.voogla.bean.ProductListInfoData
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.net.RequestBodyJson
import com.ty.voogla.util.SimpleCache
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.TimeWidght
import kotlinx.android.synthetic.main.activity_product_into_detail.*
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author TY on 2019/1/11.
 * 生产入库 详情
 */
class ProduceIntoDetailActivity : BaseActivity(), VooglaContract.View<ProductListInfoData>  {

    private lateinit var selectTime: String

    private lateinit var adapter: ProIntoDetailAdapter

//    private var barcodeReader: BarcodeReader? = null
    private var boxCode: String? = null
    private var qrCodeInfos:MutableList<String>?= null

    // 商品名称
    private var goodsName: MutableList<String> = mutableListOf()

    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_product_into_detail

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {

        presenter.getProductListInfo(SimpleCache.getUserInfo().companyNo)
    }

    override fun initTwoView() {


        initToolBar(R.string.produce_into, "保存", View.OnClickListener {
            ToastUtil.showToast("生产入库保存")
        })

        val format = SimpleDateFormat(CodeConstant.DATE_SIMPLE_Y_M_D, Locale.CHINA)
        selectTime = format.format(Date())


        // 输入批次号
        et_batch_number.setOnFocusChangeListener { v, hasFocus ->
            requestHttp(hasFocus)
        }

        // 产品选择
        tv_select_pro_name.setOnClickListener {

            DialogUtil.selectProName(it.context, goodsName,tv_select_pro_name)
        }

        // 时间选择
        tv_select_time.setOnClickListener { v ->
            TimeWidght.showPickDate(this) { date, _ ->
                selectTime = TimeWidght.getTime(date)
                tv_select_time.text = selectTime

                ToastUtil.showToast(selectTime)
            }
        }

        tv_to_box_link.setOnClickListener {
            // 广播跳转
            val intent = Intent("android.intent.action.AUTOCODEACTIVITY")
            intent.putExtra(CodeConstant.PAGE_STATE, CodeConstant.PAGE_BOX_LINK)
            startActivityForResult(intent, 100)
        }

        val list = mutableListOf("a", "b", "c")
        LayoutInit.initLayoutManager(this, house_recycler)

        adapter = ProIntoDetailAdapter(this, R.layout.item_house_detail, list)
        house_recycler.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            boxCode = data?.getStringExtra("boxCode")
            qrCodeInfos = data?.getStringArrayListExtra("qrCodeInfos")
        }
    }


    /**
     * 构建请求参数
     */
    fun initReqBody(): RequestBody {

        val saveBean = AddProduct()
        val boxInfo:MutableList<AddProduct.InBoxCodeDetailInfosBean> = mutableListOf()
        val wareInfo = AddProduct.InWareInfoBean()

        // 箱码
        val boxCodeInfo = AddProduct.InBoxCodeDetailInfosBean()
        boxCodeInfo.boxCode = boxCode
        boxCodeInfo.qrCodeInfos = qrCodeInfos
        boxInfo.add(boxCodeInfo)

        //主信息
        // 归属单位
        val userInfo = SimpleCache.getUserInfo()
        wareInfo.companyAttr = userInfo.companyAttr
        wareInfo.companyNo = userInfo.companyNo

        saveBean.inBoxCodeDetailInfos = boxInfo
        saveBean.inWareInfo = wareInfo

        val json = Gson().toJson(saveBean)

        return RequestBodyJson.requestBody(json)


    }

    /**
     * 批次号搜索
     */
    private fun requestHttp(hasFocus: Boolean) {
        val result = et_batch_number.text.toString().trim { it <= ' ' }
        if (!hasFocus && result.isNotEmpty()) {
            ToastUtil.showToast("批次号是 $result")
        }
    }

    override fun showSuccess(data: ProductListInfoData?) {
        val list = data?.list
        val size = list!!.size
        for (i in 1..size ){

            goodsName.add(list[i].goodsName!!)

        }
    }

    override fun showError(msg: String?) {
        ToastUtil.showToast(msg)
    }
}