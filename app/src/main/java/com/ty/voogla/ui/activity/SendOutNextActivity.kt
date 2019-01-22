package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.SendOutNextAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.sendout.AddSendOutData
import com.ty.voogla.bean.sendout.QrCodeListData
import com.ty.voogla.bean.sendout.SendOutListInfo
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.data.DateUtil
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.data.SimpleCache
import com.ty.voogla.net.RequestBodyJson
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.NormalAlertDialog
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import kotlinx.android.synthetic.main.activity_send_out_next.*
import okhttp3.RequestBody
import java.util.*

/**
 * @author TY on 2019/1/14.
 *
 * 发货出库 明细
 */
class SendOutNextActivity : BaseActivity(), VooglaContract.View<SendOutListInfo> {

    private val presenter = VooglaPresenter(this)
    lateinit var adapter: SendOutNextAdapter

    // 发货出库的二维码
    private var qrCodeList = ArrayList<QrCodeListData>()

    // 发货单号
    private var deliveryNo: String = ""

    // 回调成功标志
    private var isDelete = false
    private var itemPosition = 0

    // 企业编号
    private var companyNo: String? = null

    // http 获取数据
    private var deliveryList: MutableList<SendOutListInfo.DeliveryDetailInfosBean>? = null

    // position 的箱码产品
    private var itemSparse: SparseArray<ArrayList<QrCodeListData>> = SparseArray()

    override val activityLayout: Int
        get() = R.layout.activity_send_out_next

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {


        initToolBar(R.string.send_out_detail, "保存", View.OnClickListener {
            sendOutSave(initReqBody())
        })
        // 发货单编号
        deliveryNo = intent.getStringExtra(CodeConstant.DELIVERY_NO)
        companyNo = SimpleCache.getUserInfo()?.companyNo
        if (!companyNo.isNullOrEmpty()) {
            presenter.getSendOutListInfo(companyNo, deliveryNo)
        }

    }

    override fun initTwoView() {


    }

    private fun sendOutSave(body: RequestBody?) {

        if (body == null) {
            return
        }

        presenter.addSendOut(body)
        isDelete = false

    }


    /**
     * 出库 Body
     */
    private fun initReqBody(): RequestBody? {

        val saveBean = AddSendOutData()

        val userInfo = SimpleCache.getUserInfo()
        val qrCodeList = mutableListOf<AddSendOutData.OutQrCodeDetailInfosBean>()
        // 箱码数据
        val qrList = mutableListOf<QrCodeListData>()

        val goodsInfo = AddSendOutData.GoodsDeliveryInfoBean()

        val size = deliveryList!!.size
        // 商品数据
        for (i in 0 until size) {
            val qrCode = AddSendOutData.OutQrCodeDetailInfosBean()
            qrCode.wareName = ""
            qrCode.inBatchNo = ""
            qrCode.goodsNo = deliveryList!![i].goodsNo
            qrCode.outBoxNum = ""
            qrCode.outGoodsNum = ""
            qrCode.unit = ""

            val arrayList = itemSparse[i]
            val qrSize = arrayList.size
            // 箱码数据
            for (j in 0 until qrSize) {
                val qrInfo = QrCodeListData()
                qrInfo.qrCodeClass = arrayList[j].qrCodeClass
                qrInfo.qrCode = arrayList[j].qrCode

                qrList.add(qrInfo)
            }

            qrCode.qrCodeInfos = qrList
            qrCodeList.add(qrCode)
            // 清除箱码数据集合
            qrList.clear()
        }
        val time = DateUtil.getTime(Date())

        goodsInfo.companyNo = userInfo.companyNo
        goodsInfo.creator = userInfo.userNo
        goodsInfo.deliveryNo = deliveryNo
        goodsInfo.outTime = time

        saveBean.goodsDeliveryInfo = goodsInfo
        saveBean.outQrCodeDetailInfos = qrCodeList


        val json = Gson().toJson(saveBean)
        return RequestBodyJson.requestBody(json)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // 出库扫码
        if (requestCode == CodeConstant.REQUEST_CODE_OUT && resultCode == CodeConstant.RESULT_CODE) {

            val sendPosition = data?.getIntExtra(CodeConstant.SEND_POSITION, -1)!!
            qrCodeList = SimpleCache.getQrCode()
            // 存 item position 箱码产品码
            itemSparse.put(sendPosition, qrCodeList)

            val totalSize = qrCodeList.size
            var boxSize = 0
            var proSize = 0
            for (i in 0 until totalSize) {
                if (qrCodeList[i].qrCodeClass.equals("产品码")) {
                    proSize++
                } else {
                    boxSize++
                }
            }
            val view = recycler_view_send_next.getChildAt(sendPosition)
            view.findViewById<TextView>(R.id.tv_box_amount).text = "$boxSize 箱"
            view.findViewById<TextView>(R.id.tv_product_amount).text = "$proSize 盒"


//            val data = QrCodeListData()
//            data.qrCode = qrCode
//            //data.qrCodeClass = qrCodeClass
//            qrCodeList.add(sendPosition!!,data)
        }
    }


    override fun showSuccess(data: SendOutListInfo) {

        deliveryList = data.deliveryDetailInfos
        LayoutInit.initLayoutManager(this, recycler_view_send_next)
        adapter = SendOutNextAdapter(this, R.layout.item_send_out_next, deliveryList!!)
        recycler_view_send_next.adapter = adapter

        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {
            override fun onItemLongClick(view: View, holder: RecyclerView.ViewHolder, position: Int): Boolean {

                DialogUtil.deleteItemDialog(view.context, "确认删除", NormalAlertDialog.onNormalOnclickListener {

                    presenter.deleteSendOut(companyNo, deliveryNo)
                    isDelete = true
                    itemPosition = position
                    it.dismiss()
                })
                return true
            }

            override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder?, position: Int) {

            }

        })

    }

    override fun showError(msg: String) {
    }

    override fun showResponse(response: ResponseInfo) {
        if (CodeConstant.SERVICE_SUCCESS == response.msg) {

            if (isDelete) {
                deliveryList!!.removeAt(itemPosition)
                adapter.notifyItemRemoved(itemPosition)
                adapter.notifyItemRangeChanged(itemPosition, deliveryList!!.size - itemPosition)
            } else {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}