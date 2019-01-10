package com.ty.voogla.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import com.ty.voogla.R
import com.ty.voogla.adapter.InspectionAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.util.ToastUtil
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import kotlinx.android.synthetic.main.activity_inspection.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


/**
 * @author TY on 2019/1/7.
 * 稽查主页(手机端)
 */
class InspectionActivity : BaseActivity() ,EasyPermissions.PermissionCallbacks{

    lateinit var adapter: InspectionAdapter

//    val list: MutableList<InspectionData.ListBean>? = null

    // 扫描跳转Activity RequestCode
    private val REQUEST_CODE: Int = 100
    // 请求 CAMERA 权限码
    private val REQUEST_CAMERA_PERM: Int = 101

    val list: ArrayList<String> = ArrayList(10)

    override val activityLayout: Int
        get() = R.layout.activity_inspection

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {
        list.add("12345")
        list.add("12345")
    }

    override fun initTwoView() {


        initToolBar(R.string.inspection_system)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.layoutManager = layoutManager
        adapter = InspectionAdapter(this, R.layout.item_inspection, list)

        recycler_view.adapter = adapter

        ed_search.setOnTouchListener(View.OnTouchListener { v, event ->
            val drawable = ed_search.compoundDrawables[2]

            if (event.actionMasked == MotionEvent.ACTION_UP) {
                if (event.x > (ed_search.width - ed_search.paddingRight - drawable.intrinsicWidth)){
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
//            EasyPermissions.requestPermissions(Manifest.permission.CAMERA)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            val bundle = data?.extras
            if (CodeUtils.RESULT_SUCCESS == bundle?.getInt(CodeUtils.RESULT_TYPE)) {
                val result = bundle.getString(CodeUtils.RESULT_STRING)
                ToastUtil.showToast(result)
                ed_search.setText(result)
            } else {
                ToastUtil.showToast("解析二维码失败")
            }
        }else if(requestCode == REQUEST_CAMERA_PERM){
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
}