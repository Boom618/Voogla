package com.ty.voogla.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.honeywell.aidc.AidcManager
import com.honeywell.aidc.BarcodeReader
import com.ty.voogla.R
import com.ty.voogla.adapter.LayoutInit
import com.ty.voogla.adapter.ProIntoDetailAdapter
import com.ty.voogla.base.BaseActivity
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.util.ToastUtil
import com.ty.voogla.widght.TimeWidght
import kotlinx.android.synthetic.main.activity_product_into_detail.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author TY on 2019/1/11.
 * 生产入库 详情
 */
class ProduceIntoDetailActivity : BaseActivity() {

    private lateinit var selectTime: String

    private lateinit var adapter: ProIntoDetailAdapter

    lateinit var manager: AidcManager
    private var barcodeReader: BarcodeReader? = null

    override val activityLayout: Int
        get() = R.layout.activity_product_into_detail

    override fun onBaseCreate(savedInstanceState: Bundle?) {
    }

    override fun initOneData() {

        AidcManager.create(this){aidcManager ->
            manager = aidcManager
            barcodeReader = manager.createBarcodeReader()

        }
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

        // 时间选择
        tv_select_time.setOnClickListener { v ->
            TimeWidght.showPickDate(v.context) { date, _ ->
                selectTime = TimeWidght.getTime(date)
                tv_select_time.text = selectTime

                ToastUtil.showToast(selectTime)
            }
        }

        tv_to_box_link.setOnClickListener {
//            gotoActivity(BoxLinkActivity::class.java)
            val intent = Intent("android.intent.action.AUTOMATICBARCODEACTIVITY")
            startActivity(intent)
        }

        val list = mutableListOf("a", "b", "c")
        LayoutInit.initLayoutManager(this, house_recycler)

        adapter = ProIntoDetailAdapter(this, R.layout.item_house_detail, list)
        house_recycler.adapter = adapter
    }


    /**
     * 构建请求参数
     */
    fun initReqBody() {


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
}