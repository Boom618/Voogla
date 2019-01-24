package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
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
import com.ty.voogla.data.SharedP
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.net.HttpMethods
import com.ty.voogla.net.RequestBodyJson
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.data.SparseArrayUtil
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.TimeWidght
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
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
class ProduceIntoDetailActivity : BaseActivity(), VooglaContract.View<ProductListInfoData>,SelectGoods {

    private lateinit var selectTime: String

    private lateinit var adapter: ProIntoDetailAdapter

    // 箱码
    private var boxCode: String? = null
    // 箱码集合
//    private var boxCodeSparse: SparseArray<String> = SparseArray()
    /**
     * 产品码对象
     */
    private var qrCodeInfos: InBoxCodeDetailInfosBean = InBoxCodeDetailInfosBean()
    // 入库箱码明细列表
//    private val listDetail: SparseArray<List<QrCodeListData>> = SparseArray()
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

        // 产品选择
        tv_select_pro_name.setOnClickListener {

            //
            DialogUtil.selectProName(it.context, goodsName, goodsSpec, tv_select_pro_name, tv_select_spec,this)
        }

        // 时间选择
        tv_select_time.setOnClickListener { v ->
            TimeWidght.showPickDate(this) { date, _ ->
                selectTime = TimeWidght.getTime(CodeConstant.DATE_SIMPLE_H_M_S, date)
                tv_select_time.text = selectTime

                ToastUtil.showToast(selectTime)
            }
        }

        tv_to_box_link.setOnClickListener {
            // 广播跳转
            val spec = tv_select_spec.text.toString().trim { spec -> spec <= ' ' }
            if (tv_select_spec.text.isNotEmpty()) {
                val intent = Intent("android.intent.action.AUTOCODEACTIVITY")
                intent.putExtra(CodeConstant.PAGE_STATE_KEY, CodeConstant.PAGE_BOX_LINK)
                // 商品规格
                SimpleCache.putString(CodeConstant.GOODS_SPEC,spec)
                startActivityForResult(intent, CodeConstant.REQUEST_CODE_INTO)
            } else {
                ToastUtil.showToast("请选择对应的商品和规格")
            }
        }

        LayoutInit.initLayoutManager(this, house_recycler)

        adapter = ProIntoDetailAdapter(this, R.layout.item_house_detail, listDetail)
        house_recycler.adapter = adapter

        adapter.setOnItemClickListener(object :MultiItemTypeAdapter.OnItemClickListener{
            override fun onItemLongClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int): Boolean {
                return false
            }

            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {

                val deleteView = holder.itemView.findViewById<ImageView>(R.id.image_delete)

                deleteView.setOnClickListener {

                    listDetail.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position,listDetail.size - position)

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
        adapter.notifyDataSetChanged()

        tv_number.text = "0"
        ToastUtil.showToast("清空数据")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CodeConstant.REQUEST_CODE_INTO && resultCode == CodeConstant.RESULT_CODE) {

            val type = data?.getStringExtra(CodeConstant.RESULT_TYPE)
            when (type) {
                "productIn" -> {
                    boxCode = data.getStringExtra("boxCode")
                    qrCodeInfos = SimpleCache.getBoxCode()

                    listDetail.add(qrCodeInfos)
                }
                else -> {
                    // 修改
                    boxCode = data?.getStringExtra("boxCode")
                    val position = data?.getIntExtra(CodeConstant.SEND_POSITION,0)!!
                    qrCodeInfos = SimpleCache.getBoxCode()
                    listDetail.add(position,qrCodeInfos)
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
        val boxInfo: MutableList<InBoxCodeDetailInfosBean> = mutableListOf()
        val wareInfo = AddProduct.InWareInfoBean()
        val boxSize = listDetail.size

        for (i in 0 until boxSize){
            // 箱码 A0702  产品吗 A0701
            val boxCodeInfo = InBoxCodeDetailInfosBean()

            // 删除最后一列箱码,
            val infos = listDetail[i].qrCodeInfos
            val dropLast = infos!!.dropLast(1).toMutableList()
            boxCodeInfo.qrCode = listDetail[i].qrCode
            boxCodeInfo.qrCodeClass = "A0702"
            boxCodeInfo.qrCodeInfos = dropLast
            boxInfo.add(boxCodeInfo)
        }


        //主信息
        // 归属单位  缺少产品名称字段
        val userInfo = SimpleCache.getUserInfo()
        val position = SharedP.getGoodNo(this)
        if (position == -1) {
            ToastUtil.showToast("请选择商品和规格")
            return null
        }
        if (boxInfo.size == 0){
            ToastUtil.showToast("请前往箱码关联")
            return null
        }
        val goodsNoStr = goodsNo[position]
        val unit = goodsUnit[position]

        val productBatchNo = et_batch_number.text.toString().trim { it <= ' ' }
        val wareName = et_select_house.text.toString().trim { it <= ' ' }
        val inTime = tv_select_time.text.toString().trim { it <= ' ' }
        //val unit = tv_select_spec.text.toString().trim { it <= ' ' }

        if (goodsNo.isNullOrEmpty() ||
            wareName.isNullOrEmpty()
        ) {
            ToastUtil.showToast("请补全入库信息")
            return null
        }

        wareInfo.batchNo = productBatchNo
        wareInfo.companyAttr = userInfo.companyAttr
        wareInfo.companyNo = userInfo.companyNo
        wareInfo.creator = userInfo.userNo
        wareInfo.goodsNo = goodsNoStr
        wareInfo.inBoxNum = boxSize.toString()//"入库数量"
        wareInfo.inTime = inTime
        wareInfo.unit = unit
        wareInfo.wareName = wareName

        saveBean.inQrCodeDetailInfos = boxInfo
        saveBean.inWareInfo = wareInfo

        val json = Gson().toJson(saveBean)

        return RequestBodyJson.requestBody(json)


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
            goodsUnit.add(list[i].unit!!)
            goodsNo.add(list[i].goodsNo!!)
        }
    }

    override fun showResponse(response: ResponseInfo) {

    }

    override fun showError(msg: String?) {
        ToastUtil.showToast(msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedP.clearGoodNo(this)
    }
}