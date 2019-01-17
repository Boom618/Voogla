package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProIntoDetailAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.produce.AddProduct
import com.ty.voogla.bean.produce.ProductListInfoData
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.data.SharedP
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.net.HttpMethods
import com.ty.voogla.net.RequestBodyJson
import com.ty.voogla.util.SimpleCache
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.TimeWidght
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_product_into_detail.*
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author TY on 2019/1/11.
 * 生产入库 详情
 */
class ProduceIntoDetailActivity : BaseActivity(), VooglaContract.View<ProductListInfoData> {

    private lateinit var selectTime: String

    private lateinit var adapter: ProIntoDetailAdapter

    // 箱码
    private var boxCode: String? = null
    // 产品码
    private var qrCodeInfos: ArrayList<String>? = null
    // 入库箱码明细列表
    private val listDetail:MutableList<String> = mutableListOf()

    // 商品名称
    private var goodsName: MutableList<String> = mutableListOf()
    // 规格
    private var goodsSpec: MutableList<String> = mutableListOf()
    // 商品编号
    private var goodsNo:MutableList<String> = mutableListOf()

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
            produceIntoSave(initReqBody())
        })

        val format = SimpleDateFormat(CodeConstant.DATE_SIMPLE_H_M_S, Locale.CHINA)
        selectTime = format.format(Date())
        tv_select_time.text = selectTime

        // 输入批次号
//        et_batch_number.setOnFocusChangeListener { v, hasFocus ->
//            requestHttp(hasFocus)
//        }

        // 产品选择
        tv_select_pro_name.setOnClickListener {

            DialogUtil.selectProName(it.context, goodsName, goodsSpec, tv_select_pro_name, tv_select_spec)
        }

        // 时间选择
        tv_select_time.setOnClickListener { v ->
            TimeWidght.showPickDate(this) { date, _ ->
                selectTime = TimeWidght.getTime(CodeConstant.DATE_SIMPLE_H_M_S,date)
                tv_select_time.text = selectTime

                ToastUtil.showToast(selectTime)
            }
        }

        tv_to_box_link.setOnClickListener {
            // 广播跳转
            val intent = Intent("android.intent.action.AUTOCODEACTIVITY")
            intent.putExtra(CodeConstant.PAGE_STATE_KEY, CodeConstant.PAGE_BOX_LINK)
            startActivityForResult(intent, 100)
        }

        LayoutInit.initLayoutManager(this, house_recycler)

        adapter = ProIntoDetailAdapter(this, R.layout.item_house_detail, listDetail)
        house_recycler.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 100) {
            boxCode = data?.getStringExtra("boxCode")
            qrCodeInfos = data?.getStringArrayListExtra("qrCodeInfos")
            listDetail.addAll(qrCodeInfos!!)
        }
    }

    /**
     * 入库保存
     */
    private fun produceIntoSave(body: RequestBody?) {

        if (body == null) {
            return
        }

        HttpMethods.getInstance().addProduct(object : SingleObserver<ResponseInfo> {
            override fun onSuccess(info: ResponseInfo) {
                if (CodeConstant.SERVICE_SUCCESS == info.msg) {
                    // 入库成功（保存）
                    ToastUtil.showToast("入库成功")
                    finish()
                } else {
                    ToastUtil.showToast("入库失败")
                }
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                ToastUtil.showToast(e.message)
            }

        }, body)

    }


    /**
     * 构建请求参数
     */
    private fun initReqBody(): RequestBody? {

        val saveBean = AddProduct()
        val boxInfo: MutableList<AddProduct.InBoxCodeDetailInfosBean> = mutableListOf()
        val wareInfo = AddProduct.InWareInfoBean()

        // 箱码
        val boxCodeInfo = AddProduct.InBoxCodeDetailInfosBean()
//        val temp = Math.random().
        boxCodeInfo.boxCode = "1901101100182" + ""//boxCode
        boxCodeInfo.qrCodeInfos = qrCodeInfos
        boxInfo.add(boxCodeInfo)

        //主信息
        // 归属单位  缺少产品名称字段
        val userInfo = SimpleCache.getUserInfo()
        val goodsNoStr = goodsNo[SharedP.getGoodNo(this)]
        val wareName = tv_select_house.text.toString().trim { it <= ' ' }
        val inTime = tv_select_time.text.toString().trim { it <= ' ' }
        val unit = tv_select_spec.text.toString().trim { it <= ' ' }

        if (goodsNo.isNullOrEmpty() ||
            unit.isNullOrEmpty() ||
            wareName.isNullOrEmpty()
        ) {
            ToastUtil.showToast("请补全入库信息")
            return null
        }

        wareInfo.companyAttr = userInfo.companyAttr
        wareInfo.companyNo = userInfo.companyNo
        wareInfo.creator = userInfo.userNo
        wareInfo.goodsNo = goodsNoStr
        wareInfo.inNum = "1"//"入库数量"
        wareInfo.inTime = inTime
        wareInfo.unit = unit
        wareInfo.wareName = wareName

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

    /**
     * 产品列表信息
     */
    override fun showSuccess(data: ProductListInfoData?) {
        val list = data?.list
        val size = list!!.size
        for (i in 0 until size) {

            goodsName.add(list[i].goodsName!!)
            goodsSpec.add(list[i].goodsSpec!!)
            goodsNo.add(list[i].goodsNo!!)
        }
    }

    override fun showError(msg: String?) {
        ToastUtil.showToast(msg)
    }
}