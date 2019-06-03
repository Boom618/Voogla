package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import com.google.gson.Gson
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProIntoDetailAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.produce.AddProduct
import com.ty.voogla.bean.produce.InBoxCodeDetailInfosBean
import com.ty.voogla.bean.produce.ProductListInfoData
import com.ty.voogla.bean.sendout.QrCodeListData
import com.ty.voogla.connector.SelectGoods
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.constant.TipString
import com.ty.voogla.data.SharedP
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.data.SparseArrayUtil
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.net.RequestBodyJson
import com.ty.voogla.util.FullDialog
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.LoadingDialog
import com.ty.voogla.widght.TimeWidght
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.activity_product_into_detail.*
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author TY on 2019/1/11.
 * 生产入库 详情
 */
class ProduceIntoDetailActivity : BaseActivity(), VooglaContract.View<ProductListInfoData>, SelectGoods {

    private lateinit var selectTime: String

    private lateinit var adapter: ProIntoDetailAdapter

    // 箱码
    private var boxCode: String? = null
    /**
     * 产品码对象 扫码列表
     */
    private var qrCodeInfos: MutableList<QrCodeListData> = mutableListOf()

    // 外层列表
    // 入库箱码明细列表( 含箱码和产品码 )
    private val listDetail: MutableList<InBoxCodeDetailInfosBean> = mutableListOf()

    // 商品名称
    private var goodsName: MutableList<String> = mutableListOf()
    // 规格
    private var goodsSpec: MutableList<String> = mutableListOf()
    // 单位
    private var goodsUnit: MutableList<String> = mutableListOf()
    // 商品编号
    private var goodsNo: MutableList<String> = mutableListOf()

    private val presenter = VooglaPresenter(this)
    // 套码编号
    private var buApplyNo: String? = null
    // 企业箱号
    private var comBoxCode: String? = null

    override val activityLayout: Int
        get() = R.layout.activity_product_into_detail

    override fun onBaseCreate(savedInstanceState: Bundle?) {
        // 首次进来清空入库数据
        SparseArrayUtil.clearInBoxCode(this)
    }

    override fun initOneData() {

        // proInto : 是生产入库 实时更新
        presenter.getProductListInfo(SimpleCache.userInfo.companyNo, "proInto")
    }

    override fun initTwoView() {


        initToolBar(R.string.produce_into, TipString.proInto, View.OnClickListener {
            produceIntoSave(initReqBody())
        })

        val format = SimpleDateFormat(CodeConstant.DATE_SIMPLE_H_M_S, Locale.CHINA)
        selectTime = format.format(Date())
        tv_select_time.text = selectTime

        // 产品选择
        tv_select_pro_name.setOnClickListener {

            //
            DialogUtil.selectProName(this, goodsName, goodsSpec, tv_select_pro_name, tv_select_spec, this)
        }

        // 时间选择
        tv_select_time.setOnClickListener {
            TimeWidght.showPickDate(this) { date, _ ->
                selectTime = TimeWidght.getTime(CodeConstant.DATE_SIMPLE_H_M_S, date)
                tv_select_time.text = selectTime

                ToastUtil.showSuccess(selectTime)
            }
        }

        tv_to_box_link.setOnClickListener {

            if (listDetail.size >= 100) {
                ToastUtil.showWarning(TipString.commitPlease)
                return@setOnClickListener
            }
            // 跳转
            val spec = tv_select_spec.text.toString().trim { spec -> spec <= ' ' }
            if (tv_select_spec.text.isNotEmpty()) {
                val intent = Intent(this, BoxLinkJavaActivity2::class.java)
                intent.putExtra(CodeConstant.PAGE_STATE_KEY, CodeConstant.PAGE_BOX_LINK)
                // 商品规格
                SimpleCache.putString(CodeConstant.GOODS_SPEC, spec)
                startActivityForResult(intent, CodeConstant.REQUEST_CODE_INTO)
            } else {
                ToastUtil.showWarning(TipString.selectGoodsAndSpec)
            }
        }

        LayoutInit.initLayoutManager(this, house_recycler)

        adapter = ProIntoDetailAdapter(this, R.layout.item_house_detail, listDetail)
        house_recycler.adapter = adapter

        adapter.setOnItemClickListener(
            object : MultiItemTypeAdapter.OnItemClickListener {
                override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                    return false
                }

                override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {

                    val deleteView = holder.itemView.findViewById<ImageView>(R.id.image_delete)

                    deleteView.setOnClickListener {

                        listDetail.removeAt(position)
                        SparseArrayUtil.putQrCodeList(view.context, listDetail)

                        try {
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, listDetail.size - position)
                        } catch (e: Exception) {
                            adapter.notifyDataSetChanged()
                        }

                        tv_number.text = listDetail.size.toString()
                    }
                }

            })
    }

    /**
     * 用户更换商品 重置数据
     */
    override fun removeGoods() {
        listDetail.clear()
        SparseArrayUtil.putQrCodeList(this, listDetail)
        adapter.notifyDataSetChanged()

        tv_number.text = "0"
        ToastUtil.showWarning(TipString.deleteData)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CodeConstant.REQUEST_CODE_INTO && resultCode == CodeConstant.RESULT_CODE) {

            when (data!!.getStringExtra(CodeConstant.RESULT_TYPE)) {
                "productIn" -> {
                    // 入库
                    boxCode = data.getStringExtra("boxCode")
                    buApplyNo = data.getStringExtra("buApplyNo")
                    comBoxCode = data.getStringExtra("comBoxCode")
                    qrCodeInfos = SimpleCache.qrCode

                    val boxBean = InBoxCodeDetailInfosBean()
                    boxBean.qrCode = boxCode
                    boxBean.buApplyNo = buApplyNo
                    boxBean.qrCodeClass = "A0702"
                    boxBean.comBoxCode = comBoxCode
                    boxBean.qrCodeInfos = qrCodeInfos

                    listDetail.add(boxBean)
                    // 【文件写入】存所有数据,在扫码是校验是否有重复码（箱码和产品码）
                    SparseArrayUtil.putQrCodeList(this, listDetail)
                }
                "productChange" -> {
                    // 修改
                    boxCode = data.getStringExtra("boxCode")
                    buApplyNo = data.getStringExtra("buApplyNo")
                    comBoxCode = data.getStringExtra("comBoxCode")
                    val position = data.getIntExtra(CodeConstant.SEND_POSITION, 0)
                    qrCodeInfos = SimpleCache.qrCode
                    val boxBean = InBoxCodeDetailInfosBean()
                    boxBean.qrCode = boxCode
                    boxBean.buApplyNo = buApplyNo
                    boxBean.qrCodeClass = "A0702"
                    boxBean.comBoxCode = comBoxCode
                    boxBean.qrCodeInfos = qrCodeInfos

                    listDetail[position] = boxBean
                    // 【文件写入】修改成功更新存的数据,在扫码是校验是否有重复码（箱码和产品码）
                    SparseArrayUtil.putQrCodeList(this, listDetail)
                }
            }

            val size = listDetail.size
            tv_number.text = size.toString()
        }
    }

    /**
     * 入库保存
     */
    private fun produceIntoSave(body: RequestBody?) {

        if (body == null) {
            return
        }
        presenter.addProduct(body)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // 扫码按键
        if (keyCode == CodeConstant.KEY_CODE_223 || keyCode == CodeConstant.KEY_CODE_224) {
            // 禁用扫码

            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    /**
     * 构建请求参数
     */
    private fun initReqBody(): RequestBody? {

        val saveBean = AddProduct()
        val wareInfo = AddProduct.InWareInfoBean()
        val boxSize = listDetail.size

        //主信息
        // 归属单位
        val userInfo = SimpleCache.userInfo
        val position = SharedP.getGoodNo(this)

        if (boxSize == 0) {
            ToastUtil.showWarning(TipString.gotoBoxLink)
            return null
        }
        if (position == -1) {
            ToastUtil.showWarning(TipString.selectGoodsAndSpec)
            return null
        }
        val goodsNoStr = goodsNo[position]
        val unit = goodsUnit[position]

        val productBatchNo = et_batch_number.text.toString().trim { it <= ' ' }
        val wareName = et_select_house.text.toString().trim { it <= ' ' }
        val inTime = tv_select_time.text.toString().trim { it <= ' ' }

        if (goodsNo.isNullOrEmpty() || wareName.isEmpty()) {
            ToastUtil.showWarning(TipString.perfectIntoMessage)
            return null
        }
        for (i in 0 until boxSize){
            listDetail[i].qrCodeInfos?.clear()
        }


        wareInfo.batchNo = productBatchNo
        wareInfo.companyAttr = userInfo.companyAttr
        wareInfo.companyNo = userInfo.companyNo
        wareInfo.creator = userInfo.userNo

        wareInfo.goodsNo = goodsNoStr
        wareInfo.inBoxNum = listDetail.size.toString()//"入库数量"
        wareInfo.inTime = inTime
        wareInfo.unit = unit
        wareInfo.wareName = wareName

        saveBean.inQrCodeDetailInfos = listDetail
        saveBean.inWareInfo = wareInfo

        val json = Gson().toJson(saveBean)

        return RequestBodyJson.requestBody(json)


    }

    /**
     * 产品列表信息
     */
    override fun showSuccess(data: ProductListInfoData) {
        val list = data.list!!
        val size = list.size
        for (i in 0 until size) {

            goodsName.add(list[i].goodsName)
            goodsSpec.add(list[i].goodsSpec!!)
            goodsUnit.add(list[i].unit!!)
            goodsNo.add(list[i].goodsNo)
        }
    }

    override fun showResponse(response: ResponseInfo) {
        ToastUtil.showSuccess(TipString.intoSuccess)
        finish()
    }

    override fun showError(msg: String?) {
        ToastUtil.showError(msg)
    }

    private var dialog: LoadingDialog? = null
    override fun showLoading() {
        dialog = FullDialog.showLoading(this, TipString.loading)
    }

    override fun hideLoading() {
        dialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 清空商品编号
        SharedP.clearGoodNo(this)
        // 清空所有已保存的码
        SparseArrayUtil.clearInBoxCode(this)
    }
}