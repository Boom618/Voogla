//package com.ty.voogla.ui.activity.boxlink;
//
//import android.content.pm.ActivityInfo;
//import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
//import android.widget.Toast;
//import com.honeywell.aidc.*;
//import com.ty.voogla.R;
//import com.ty.voogla.base.BaseActivity;
//import com.ty.voogla.base.ResponseInfo;
//import com.ty.voogla.bean.produce.DecodeCode;
//import com.ty.voogla.mvp.contract.VooglaContract;
//import com.ty.voogla.mvp.presenter.VooglaPresenter;
//import com.ty.voogla.ui.activity.MainPdaJavaActivity;
//import com.ty.voogla.ui.activity.scan.BarcodeProperties;
//import org.jetbrains.annotations.Nullable;
//
//
///**
// * @author TY on 2019/1/26.
// * 产品入库扫码
// */
//public class BoxLinkSendOut extends BaseActivity implements BarcodeReader.BarcodeListener,
//        BarcodeReader.TriggerListener, VooglaContract.View<DecodeCode.ResultBean> {
//
//
//    private BarcodeReader barcodeReader;
//    private RecyclerView boxRecycler;
//    private boolean triggerState = false;
//
//    private VooglaPresenter presenter = new VooglaPresenter(this);
//
//
//    @Override
//    public void onBarcodeEvent(final BarcodeReadEvent event) {
//
//        String dataString = event.getBarcodeData();
//
//        presenter.decodeUrlCode(dataString);
//
//        try {
//            //sleep 1s waiting for another barcode
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        continuousScanning(true);
//    }
//
//    public void continuousScanning(boolean bState){
//        try {
//            //turn on/off backlight
//            barcodeReader.light(bState);
//            //开关瞄准线
//            barcodeReader.aim(bState);
//            //开关解码功能
//            barcodeReader.decode(bState);
//        } catch (ScannerUnavailableException e) {
//            e.printStackTrace();
//        } catch (ScannerNotClaimedException e) {
//            e.printStackTrace();
//        }
//        triggerState = bState;
//    }
//
//    // When using Automatic Trigger control do not need to implement the
//    // onTriggerEvent function
//    @Override
//    public void onTriggerEvent(TriggerStateChangeEvent event) {
//        // TODO Auto-generated method stub
//
//        if (event.getState()) {
//            //turn on/off aimer, illumination and decoding
//            try {
//                barcodeReader.aim(!triggerState);
//                barcodeReader.light(!triggerState);
//                barcodeReader.decode(!triggerState);
//
//                triggerState = !triggerState;
//            } catch (ScannerNotClaimedException e) {
//                e.printStackTrace();
//            } catch (ScannerUnavailableException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onFailureEvent(BarcodeFailureEvent arg0) {
//        // TODO Auto-generated method stub
//        //continuousScanning(true);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (barcodeReader != null) {
//            try {
//                barcodeReader.claim();
//            } catch (ScannerUnavailableException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        if (barcodeReader != null) {
//            // release the scanner claim so we don't get any scanner
//            // notifications while paused.
//            barcodeReader.release();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (barcodeReader != null) {
//            // unregister barcode event listener
//            barcodeReader.removeBarcodeListener(this);
//
//            // unregister trigger state change listener
//            barcodeReader.removeTriggerListener(this);
//        }
//    }
//
//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_box_link_code;
//    }
//
//    @Override
//    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {
//
//    }
//
//    @Override
//    protected void initOneData() {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//        // get bar code instance from MainActivity
//        barcodeReader = MainPdaJavaActivity.getBarcodeObject();
//
//        if (barcodeReader != null) {
//
//            // register bar code event listener
//            barcodeReader.addBarcodeListener(this);
//
//            // set the trigger mode to client control
//            try {
//                barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
//                        BarcodeReader.TRIGGER_CONTROL_MODE_CLIENT_CONTROL);
//            } catch (UnsupportedPropertyException e) {
//                Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT).show();
//            }
//            // register trigger state change listener
//            barcodeReader.addTriggerListener(this);
//
//            // 设置 50k 属性
//            BarcodeProperties.setProperties(barcodeReader);
//        }
//
//    }
//
//    @Override
//    protected void initTwoView() {
//
//    }
//
//
//    @Override
//    public void showSuccess(DecodeCode.ResultBean data) {
//
//    }
//
//    @Override
//    public void showError(String msg) {
//
//    }
//
//    @Override
//    public void showResponse(ResponseInfo response) {
//
//    }
//}
