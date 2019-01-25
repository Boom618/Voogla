package com.ty.voogla.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.ty.voogla.R
import com.ty.voogla.adapter.InspectionAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.base.BaseResponse
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.bean.CheckInfoList
import com.ty.voogla.bean.produce.DecodeCode
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.data.SimpleCache
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
import io.reactivex.internal.operators.single.SingleObserveOn
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

    // 扫描跳转Activity RequestCode
    private val REQUEST_CODE: Int = 100
    // 请求 CAMERA 权限码
    private val REQUEST_CAMERA_PERM: Int = 101

    // 企业编号
    private var companyNo: String? = null
    val list = mutableListOf<CheckInfoList.ListBean>()

    private val presenter = VooglaPresenter(this)

    override val activityLayout: Int
        get() = R.layout.activity_inspection

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        companyNo = SimpleCache.getUserInfo().companyNo
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

        // XML　设置   android:imeOptions="actionSearch"
        //            android:singleLine="true"
//        ed_search.setOnEditorActionListener { textView, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//
//                val tempString = textView.text.toString().trim { it <= ' ' }
//                ToastUtil.showToast(tempString)
//            }
//            true
//        }

        tv_search.setOnClickListener {
            ToastUtil.showToast("搜索内容：" + ed_search.text.toString())
        }

    }

    var disposable: Disposable? = null

    override fun showSuccess(data: DecodeCode.ResultBean) {
        // 二维码解析成功
        val qrCodeType = data.qrCodeType!!
        val code = data.code!!

        val qrCodeClass = if (qrCodeType == "2") "A0702" else "A0701"

        HttpMethods.getInstance().checkInfoList(object : SingleObserver<BaseResponse<CheckInfoList>> {
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onSuccess(response: BaseResponse<CheckInfoList>) {
                if (CodeConstant.SERVICE_SUCCESS == response.msg) {
                    val list = response.data?.list!!

                    val layoutManager =
                        LinearLayoutManager(this@InspectionActivity, LinearLayoutManager.VERTICAL, false)
                    recycler_view.layoutManager = layoutManager
                    adapter = InspectionAdapter(this@InspectionActivity, R.layout.item_inspection, list)

                    recycler_view.adapter = adapter

                    adapter.setOnItemClickListener(object : MultiItemTypeAdapter.OnItemClickListener {

                        override fun onItemLongClick(
                            view: View?,
                            holder: RecyclerView.ViewHolder?,
                            position: Int
                        ): Boolean {
                            return false
                        }

                        override fun onItemClick(view: View?, holder: RecyclerView.ViewHolder, position: Int) {
                            val imageView = holder.itemView.findViewById<ImageView>(R.id.image_confirm)
                            val deliveryNo = ""
                            ImageViewSetOnClick(imageView, deliveryNo)
                        }

                    })


                } else {

                }
            }

            override fun onError(e: Throwable) {
                ToastUtil.showToast(e.message)
            }
        }, qrCodeClass, code)

    }

    override fun showError(msg: String) {
        ToastUtil.showToast(msg)
    }

    override fun showResponse(response: ResponseInfo) {
    }

    /**
     * 确认串货 Dialog
     */
    fun ImageViewSetOnClick(imageView: ImageView, deliveryNo: String) {
        imageView.setOnClickListener { view ->
            DialogUtil.deleteItemDialog(view.context, "温馨提示", "确认窜货", NormalAlertDialog.onNormalOnclickListener {

                checkInfoConfirm(deliveryNo)
                it.dismiss()
//                datas.removeAt(position)
//                notifyItemRemoved(position)
//                notifyItemRangeChanged(position, datas.size - position)
            })
        }

    }


    /**
     * 确认串货 Http
     * deliveryNo 发货单编号
     */
    fun checkInfoConfirm(deliveryNo: String) {

        HttpMethods.getInstance().checkInfoConfirm(object : SingleObserver<ResponseInfo> {
            override fun onSuccess(response: ResponseInfo) {
                if (CodeConstant.SERVICE_SUCCESS == response.msg) {

//                    adapter.notifyItemRemoved()
//                    adapter.notifyItemRangeChanged()

                }
                ToastUtil.showToast(response.msg)
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                ToastUtil.showToast(e.message)
            }

        }, companyNo, deliveryNo)

    }

    @AfterPermissionGranted(101)
    fun cameraTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val intent = Intent(this, CaptureActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
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
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            val bundle = data?.extras
            if (CodeUtils.RESULT_SUCCESS == bundle?.getInt(CodeUtils.RESULT_TYPE)) {
                val result = bundle.getString(CodeUtils.RESULT_STRING)

                presenter.decodeUrlCode(result)

                ToastUtil.showToast(result)
                //ed_search.setText(result)
            } else {
                ToastUtil.showToast("解析二维码失败")
            }
        } else if (requestCode == REQUEST_CAMERA_PERM) {
            ToastUtil.showToast("从设置页面返回...")
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