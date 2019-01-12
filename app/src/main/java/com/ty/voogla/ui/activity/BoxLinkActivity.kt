//package com.ty.voogla.ui.activity
//
//import android.content.pm.ActivityInfo
//import android.os.Bundle
//import android.support.v7.widget.DividerItemDecoration
//import android.view.View
//import com.honeywell.aidc.*
//import com.ty.voogla.R
//import com.ty.voogla.adapter.BoxLinkAdapter
//import com.ty.voogla.adapter.LayoutInit
//import com.ty.voogla.base.BaseActivity
//import com.ty.voogla.util.ToastUtil
//import kotlinx.android.synthetic.main.activity_box_link_code.*
//import java.util.HashMap
//import com.honeywell.aidc.ScannerNotClaimedException
//import com.honeywell.aidc.ScannerUnavailableException
//
//
///**
// * @author TY on 2019/1/2.
// *
// * 箱码绑定 --> 扫码
// */
//class BoxLinkActivity : BaseActivity(), BarcodeReader.TriggerListener
//    , BarcodeReader.BarcodeListener {
//
//    private var barcodeReader: BarcodeReader? = null
//
//    lateinit var adapter: BoxLinkAdapter
//    // 箱码数据
//    lateinit var listCode: MutableList<String>
//
//    private var triggerState: Boolean = false
//
//    override val activityLayout: Int
//        get() = R.layout.activity_box_link_code
//
//    override fun onBaseCreate(savedInstanceState: Bundle?) {
//
//        // 设置屏幕方向
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//
//    }
//
//    override fun initOneData() {
//
//
//    }
//
//    override fun initTwoView() {
//
//
//        barcodeReader = MainPdaJavaActivity.getBarcodeObject()
//
//        barcodeReader?.addBarcodeListener(this)
//
//        barcodeReader?.setProperty(
//            BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
//            // TRIGGER_CONTROL_MODE_CLIENT_CONTROL
//            BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL
//        )
//
//        barcodeReader?.addTriggerListener(this)
//
//        val properties = HashMap<String, Any>()
//        // Set Symbologies On/Off
//        properties[BarcodeReader.PROPERTY_CODE_128_ENABLED] = true
//        properties[BarcodeReader.PROPERTY_GS1_128_ENABLED] = true
//        properties[BarcodeReader.PROPERTY_QR_CODE_ENABLED] = true
//        properties[BarcodeReader.PROPERTY_CODE_39_ENABLED] = true
//        properties[BarcodeReader.PROPERTY_DATAMATRIX_ENABLED] = true
//        properties[BarcodeReader.PROPERTY_UPC_A_ENABLE] = true
//        properties[BarcodeReader.PROPERTY_EAN_13_ENABLED] = true // false
//        properties[BarcodeReader.PROPERTY_AZTEC_ENABLED] = true // false
//        properties[BarcodeReader.PROPERTY_CODABAR_ENABLED] = true // false
//        properties[BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED] = true // false
//        properties[BarcodeReader.PROPERTY_PDF_417_ENABLED] = true // false
//        // Set Max Code 39 barcode length
//        properties[BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH] = 10
//        // Turn on center decoding
//        properties[BarcodeReader.PROPERTY_CENTER_DECODE] = true
//        // Enable bad read response
//        properties[BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED] = true
//        // Apply the settings
//        barcodeReader?.setProperties(properties)
//
//
//
//        initToolBar(R.string.box_link, "保存", View.OnClickListener {
//
//            ToastUtil.showToast("保存信息")
//        })
//
//        listCode = mutableListOf("a", "b", "c")
//        LayoutInit.initLayoutManager(this, box_recycler)
//
//        box_recycler.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//        adapter = BoxLinkAdapter(this, R.layout.item_box_link, listCode)
//        box_recycler.adapter = adapter
//
//    }
//
//    /**
//     *  PDA SDK 回调 ---- start -------------
//     */
//    override fun onTriggerEvent(event: TriggerStateChangeEvent) {
//        ToastUtil.showToast("onTriggerEvent =====" + event.state)
//
//        if (event.state) {
//            barcodeReader?.aim(!triggerState)
//            barcodeReader?.light(!triggerState)
//            barcodeReader?.decode(!triggerState)
//            triggerState = !triggerState
//        }
//    }
//
//    override fun onFailureEvent(event: BarcodeFailureEvent?) {
//        ToastUtil.showToast("onFailureEvent =====" + event?.timestamp)
//    }
//
//    override fun onBarcodeEvent(event: BarcodeReadEvent) {
//        ToastUtil.showToast("onBarcodeEvent =====" + event.barcodeData)
//
//
//        runOnUiThread {
//            listCode.add(event.barcodeData)
//            adapter.notifyDataSetChanged()
//
//            continuousScanning(true)
//        }
//    }
//    /**  PDA SDK 回调 ---- End ------------- */
//
//
//    /**
//     * 连续扫码
//     */
//    fun continuousScanning(bState: Boolean) {
//        try {
//            barcodeReader?.light(bState)      //turn on/off backlight
//            barcodeReader?.aim(bState)        //开关瞄准线
//            barcodeReader?.decode(bState)        //开关解码功能
//        } catch (e: ScannerUnavailableException) {
//            e.printStackTrace()
//        } catch (e: ScannerNotClaimedException) {
//            e.printStackTrace()
//        }
//
//        triggerState = bState
//    }
//
//    override fun onResume() {
//        super.onResume()
//        barcodeReader?.claim()
//    }
//
//    override fun onPause() {
//        super.onPause()
//
//        barcodeReader?.release()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        // unregister barcode event listener
//        barcodeReader?.removeBarcodeListener(this)
//
//        // unregister trigger state change listener
//        barcodeReader?.removeTriggerListener(this)
//    }
//}