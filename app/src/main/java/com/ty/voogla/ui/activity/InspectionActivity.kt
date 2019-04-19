package com.ty.voogla.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.ty.voogla.R
import com.ty.voogla.adapter.InspectionAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.BaseResponse
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.CheckInfoList
import com.ty.voogla.bean.produce.DecodeCode
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.constant.TipString
import com.ty.voogla.mvp.contract.VooglaContract
import com.ty.voogla.mvp.presenter.VooglaPresenter
import com.ty.voogla.net.HttpMethods
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.DialogUtil
import com.ty.voogla.widght.NormalAlertDialog
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_inspection.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


/**
 * @author TY on 2019/1/7.
 * 稽查主页(手机端)
 */
class InspectionActivity : BaseActivity(), EasyPermissions.PermissionCallbacks,
    VooglaContract.View<DecodeCode.ResultBean> {

    lateinit var adapter: InspectionAdapter

    // 请求 CAMERA 权限码
    private val REQUEST_CAMERA_PERM: Int = 101

    private var list = mutableListOf<CheckInfoList.ListBean>()

    private val presenter = VooglaPresenter(this)

    private lateinit var currentCode:String
    private lateinit var currentCodeClass:String

    override val activityLayout: Int
        get() = R.layout.activity_inspection

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
//        adapter = InspectionAdapter(this, R.layout.item_inspection, list)
//        recycler_view.adapter = adapter

    }

    override fun initTwoView() {

        initToolBar(R.string.inspection_system)


        ed_search.setOnTouchListener(View.OnTouchListener { _, event ->
            val drawable = ed_search.compoundDrawables[2]

            if (event.actionMasked == MotionEvent.ACTION_UP) {
                if (event.x > (ed_search.width - ed_search.paddingRight - drawable.intrinsicWidth)) {
                    // 打开相机扫码
                    cameraTask()
                }
                return@OnTouchListener false
            }
            false
        })

        tv_search.setOnClickListener {
            ToastUtil.showToast("搜索内容：" + ed_search.text.toString())
        }

    }

    var disposable: Disposable? = null

    override fun showSuccess(data: DecodeCode.ResultBean) {
        // 二维码解析成功
        val qrCodeType = data.qrCodeType!!
        currentCode = data.code!!
        // 单据号
        ed_search.setText(currentCode)

        currentCodeClass = if (qrCodeType == "2") "A0702" else "A0701"

        httpCheckList(currentCodeClass, currentCode)

    }

    /**
     * 获取稽查列表数据
     */
    private fun httpCheckList(qrCodeClass: String, code: String) {
        HttpMethods.getInstance().checkInfoList(object : SingleObserver<BaseResponse<CheckInfoList>> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onSuccess(response: BaseResponse<CheckInfoList>) {
                if (CodeConstant.SERVICE_SUCCESS == response.msg) {
                    list = response.data?.list!!

                    adapter = InspectionAdapter(this@InspectionActivity, R.layout.item_inspection, list)
                    recycler_view.adapter = adapter
                    //                    adapter.notifyDataSetChanged()

                    if (list.size == 0) {
                        ToastUtil.showToast("无发货信息")
                    } else {
                        adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {

                            override fun onItemLongClick(
                                view: View,
                                holder: RecyclerView.ViewHolder,
                                position: Int
                            ): Boolean {
                                return false
                            }

                            override fun onItemClick(view: View, holder: RecyclerView.ViewHolder, position: Int) {
                                val confirmView = holder.itemView.findViewById<TextView>(R.id.tv_confirm)
                                val deliveryNo = list[position].deliveryNo!!
                                val companyNo = list[position].companyNo!!
                                val fleeFlag = list[position].fleeFlag!!
                                ImageViewSetOnClick(confirmView, companyNo, deliveryNo, fleeFlag)
                            }
                        })
                    }

                } else {

                }
            }

            override fun onError(e: Throwable) {
                ToastUtil.showError(e.message)
            }
        }, qrCodeClass, code)
    }

    override fun showError(msg: String) {
        ToastUtil.showToast(msg)
    }

    /**
     * 确认窜货
     */
    override fun showResponse(response: ResponseInfo) {
        // list 数据没更新  不能 notify, 方案 1：重新获取数据
        httpCheckList(currentCodeClass, currentCode)
    }

    /**
     * 确认/取消 窜货 Dialog
     * fleeFlag 是否窜货(01否,02是)
     */
    fun ImageViewSetOnClick(confirmView: TextView, companyNo: String, deliveryNo: String, fleeFlag: String) {
        val pointContent = if (fleeFlag == "01") "确认窜货" else "取消窜货"
        // 确认操作 取反
        val flag = if (fleeFlag == "01") "02" else "01"
        confirmView.setOnClickListener { view ->
            DialogUtil.deleteItemDialog(view.context, TipString.tips, pointContent, NormalAlertDialog.onNormalOnclickListener {
                checkInfoConfirm(companyNo, deliveryNo, flag)
                it.dismiss()
            })
        }

    }


    /**
     * 确认串货 Http
     * companyNo 该订单的企业编号
     * deliveryNo 发货单编号
     * 是否窜货(01否,02是)
     */
    private fun checkInfoConfirm(companyNo: String, deliveryNo: String, fleeFlag: String) {

        presenter.checkInfoConfirm(companyNo, deliveryNo, fleeFlag)

    }

    @AfterPermissionGranted(101)
    fun cameraTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent, CodeConstant.RESULT_CODE)
        } else {
            // Ask for one permission
            // 申请权限 Dialog
            EasyPermissions.requestPermissions(
                this,
                "需要请求相机权限",
                REQUEST_CAMERA_PERM,
                Manifest.permission.CAMERA
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CodeConstant.RESULT_CODE) {
            //处理扫描结果（在界面上显示）
            val bundle = data?.extras
            if (CodeUtils.RESULT_SUCCESS == bundle?.getInt(CodeUtils.RESULT_TYPE)) {
                val result = bundle.getString(CodeUtils.RESULT_STRING)

                presenter.decodeUrlCode(result)

            } else {
                ToastUtil.showError("解析二维码失败")
            }
        } else if (requestCode == REQUEST_CAMERA_PERM) {
            ToastUtil.showWarning("从设置页面返回...")
        }
    }

    /**
     * EsayPermissions 接管权限处理逻辑
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    /**
     * 拒绝
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        ToastUtil.showToast("拒绝权限")
    }

    /**
     * 授予权限
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

        // 权限申请 Dialog ，可以自定义
//        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)) {
//            AppSettingsDialog.Builder(this)
//                .setTitle("权限申请")
//                .setPositiveButton("确认")
//                .setNegativeButton("取消")
//                .setRequestCode(REQUEST_CAMERA_PERM)
//                .build()
//                .show()
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}