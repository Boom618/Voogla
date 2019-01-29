//package com.ty.voogla.ui.activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.support.v7.widget.RecyclerView
//import android.util.SparseArray
//import android.view.View
//import android.widget.TextView
//import android.widget.Toast
//import com.google.gson.Gson
//import com.ty.voogla.R
//import com.ty.voogla.adapter.LayoutInit
//import com.ty.voogla.adapter.SendOutNextAdapter
//import com.ty.voogla.base.BaseActivity
//import com.ty.voogla.base.ResponseInfo
//import com.ty.voogla.bean.sendout.AddSendOutData
//import com.ty.voogla.bean.sendout.QrCodeListData
//import com.ty.voogla.bean.sendout.SendOutListInfo
//import com.ty.voogla.constant.CodeConstant
//import com.ty.voogla.data.CopyQrCodeList
//import com.ty.voogla.data.DateUtil
//import com.ty.voogla.mvp.contract.VooglaContract
//import com.ty.voogla.mvp.presenter.VooglaPresenter
//import com.ty.voogla.data.SimpleCache
//import com.ty.voogla.data.SparseArrayUtil
//import com.ty.voogla.net.RequestBodyJson
//import com.ty.voogla.util.ToastUtil
//import com.ty.voogla.widght.DialogUtil
//import com.ty.voogla.widght.NormalAlertDialog
//import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
//import kotlinx.android.synthetic.main.activity_send_out_next.*
//import okhttp3.RequestBody
//import java.util.*
//import kotlin.collections.ArrayList
//import kotlin.collections.HashMap
//
///**
// * @author TY on 2019/1/14.
// *
// * 发货出库 明细（提交数据 不能同步）
// */
//class SendOutNextActivity : BaseActivity(), VooglaContract.View<SendOutListInfo> {
//
//    private val presenter = VooglaPresenter(this)
//    lateinit var adapter: SendOutNextAdapter
//
//    // 发货出库的二维码
//    private var qrCodeList = ArrayList<QrCodeListData>()
//
//    // 发货单号
//    private var deliveryNo: String = ""
//    // 箱码、产品码 数量
//    private var boxSize = SparseArray<Int>()
//    private var proSize = SparseArray<Int>()
//
//    // 地址信息
//    private var provinceLevel: String? = null
//    private var cityLevel: String? = null
//    private var countyLevel: String? = null
//    private var deliveryAddress: String? = null
//
//
//    // 企业编号
//    private var companyNo: String? = null
//
//    // http 获取数据
//    private var deliveryList: MutableList<SendOutListInfo.DeliveryDetailInfosBean> = mutableListOf()
//    // item 存码
//    private var hashMap: HashMap<Int, ArrayList<QrCodeListData>> = HashMap()
//
//    // position 的箱码产品
////    private var itemSparse: SparseArray<ArrayList<QrCodeListData>> = SparseArray()
//
//    override val activityLayout: Int
//        get() = R.layout.activity_send_out_next
//
//    override fun onBaseCreate(savedInstanceState: Bundle?) {
//    }
//
//    override fun initOneData() {
//        // 首次进来清空出库数据
//        SimpleCache.clearKey("qrCode")
//
//
//        initToolBar(R.string.send_out_detail, "保存", View.OnClickListener { view ->
//            DialogUtil.deleteItemDialog(view.context, "确认发货？", "确认发货", NormalAlertDialog.onNormalOnclickListener {
//                sendOutSave(initReqBody())
//                it.dismiss()
//            })
////            val size = deliveryList.size
////            for (i in 0 until size){
////                val temp = deliveryList[i].deliveryNum!!.toInt()
////
////                if ( temp != boxSize) {
////                    return@OnClickListener
////                }else{
////                    sendOutSave(initReqBody())
////                    return@OnClickListener
////                }
////            }
//        })
//        // 发货单编号
//        deliveryNo = intent.getStringExtra(CodeConstant.DELIVERY_NO)
//        companyNo = SimpleCache.getUserInfo()?.companyNo
//        if (!companyNo.isNullOrEmpty()) {
//            presenter.getSendOutListInfo(companyNo, deliveryNo)
//        }
//
//    }
//
//    override fun initTwoView() {
//
//
//    }
//
//    private fun sendOutSave(body: RequestBody?) {
//
//        if (body == null) {
//            return
//        }
//
//        presenter.addSendOut(body)
////        isDelete = false
//
//    }
//
//
//    /**
//     * 出库 Body
//     */
//    private fun initReqBody(): RequestBody? {
//
//        val saveBean = AddSendOutData()
//
//        val userInfo = SimpleCache.getUserInfo()
//        val qrCodeList = mutableListOf<AddSendOutData.OutQrCodeDetailInfosBean>()
//        // 箱码数据
//        val qrList = mutableListOf<QrCodeListData>()
//
//        val goodsInfo = AddSendOutData.WareInfoBean()
//
//        // 发货明细 size
//        val itemSize = deliveryList.size
//
//        // 商品数据
//        for (i in 0 until itemSize) {
//            // 仓库（有效值）
//            val qrCode = AddSendOutData.OutQrCodeDetailInfosBean()
//            qrCode.goodsNo = deliveryList[i].goodsNo
//            // 箱码数量
//            qrCode.outBoxNum = boxSize[i].toString()
//            qrCode.outGoodsNum = proSize[i].toString()
//
//            qrCode.unit = deliveryList[i].unit
//
//            val arrayList = hashMap[i]
//            if (!arrayList.isNullOrEmpty()) {
//
//                val qrSize = arrayList.size
//                // 箱码数据
//                for (j in 0 until qrSize) {
//                    val qrInfo = QrCodeListData()
//                    // 坑，同一个对象
//                    val data = CopyQrCodeList.deepCopyList(qrInfo)
////                    qrInfo.qrCodeClass = arrayList[j].qrCodeClass
////                    qrInfo.qrCode = arrayList[j].qrCode
//                    qrList.add(data)
//                }
//                qrCode.qrCodeInfos = qrList
//            }
//
//            qrCodeList.add(qrCode)
//        }
//        val time = DateUtil.getTime(Date())
//
//        goodsInfo.companyNo = userInfo.companyNo
//        goodsInfo.creator = userInfo.userNo
//        goodsInfo.deliveryNo = deliveryNo
//        goodsInfo.outTime = time
//        goodsInfo.provinceLevel = provinceLevel
//        goodsInfo.cityLevel = cityLevel
//        goodsInfo.countyLevel = countyLevel
//        goodsInfo.deliveryAddress = deliveryAddress
//
//        saveBean.outWareInfo = goodsInfo
//        saveBean.outQrCodeDetailInfos = qrCodeList
////        saveBean.outQrCodeDetailInfos = addList
//
//
//        val json = Gson().toJson(saveBean)
//        return RequestBodyJson.requestBody(json)
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        // 出库扫码
//        if (requestCode == CodeConstant.REQUEST_CODE_OUT && resultCode == CodeConstant.RESULT_CODE) {
//            // 重置数量
//            var pSize = 0
//            var bSize = 0
//            val sendPosition = data?.getIntExtra(CodeConstant.SEND_POSITION, -1)!!
////            qrCodeList = SimpleCache.getQrCode()
//            hashMap = SparseArrayUtil.getQrCodeSend(this)
//            qrCodeList = hashMap[sendPosition]!!
//
//            // 存 item position 箱码产品码
//
//            val totalSize = qrCodeList.size
//
//            for (i in 0 until totalSize) {
//                if (qrCodeList[i].qrCodeClass.equals("A0701")) {
//                    pSize++
//                } else {
//                    bSize++
//                }
//            }
//
//            proSize.put(sendPosition, pSize)
//            boxSize.put(sendPosition, bSize)
//
//            val view = recycler_view_send_next.getChildAt(sendPosition)
//            view.findViewById<TextView>(R.id.tv_box_amount).text = "$bSize 箱"
//            view.findViewById<TextView>(R.id.tv_product_amount).text = "$pSize 盒"
//
//        }
//    }
//
//
//    override fun showSuccess(data: SendOutListInfo) {
//
//        val info = data.goodsDeliveryInfo!!
//        provinceLevel = info.provinceLevel
//        cityLevel = info.cityLevel
//        countyLevel = info.countyLevel
//        deliveryAddress = info.deliveryAddress
//
//        deliveryList = data.deliveryDetailInfos!!
//        LayoutInit.initLayoutManager(this, recycler_view_send_next)
//        adapter = SendOutNextAdapter(this, R.layout.item_send_out_next, deliveryList!!)
//        recycler_view_send_next.adapter = adapter
//
//    }
//
//    override fun showError(msg: String) {
//    }
//
//    override fun showResponse(response: ResponseInfo) {
//        ToastUtil.showToast(response.msg)
//        finish()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        SparseArrayUtil.clearCode(this)
//    }
//}