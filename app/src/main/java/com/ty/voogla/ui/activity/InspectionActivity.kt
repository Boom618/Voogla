package com.ty.voogla.ui.activity

import android.Manifest
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.ty.voogla.R
import com.ty.voogla.adapter.InspectionAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.bean.InspectionData
import kotlinx.android.synthetic.main.activity_inspection.*
import com.uuzuche.lib_zxing.activity.CaptureActivity
import android.content.Intent
import android.provider.MediaStore
import com.ty.voogla.util.ToastUtil
import com.uuzuche.lib_zxing.activity.CodeUtils
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import android.Manifest.permission
import android.R.attr.onClick
import pub.devrel.easypermissions.AfterPermissionGranted



/**
 * @author TY on 2019/1/7.
 * 稽查主页
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

        tv_search.setOnClickListener {
            cameraTask()
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
            EasyPermissions.requestPermissions(
                this, "需要请求 camera 权限",
                REQUEST_CAMERA_PERM, Manifest.permission.CAMERA
            )
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
        ToastUtil.showToast("拒绝l权限")
    }

    /**
     * 授予权限
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)) {
            AppSettingsDialog.Builder(this)
                .setTitle("权限申请")
                .setPositiveButton("确认")
                .setNegativeButton("取消")
                .setRequestCode(REQUEST_CAMERA_PERM)
                .build()
                .show()
        }

    }
}