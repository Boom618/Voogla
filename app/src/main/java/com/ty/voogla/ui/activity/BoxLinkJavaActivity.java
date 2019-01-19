package com.ty.voogla.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.honeywell.aidc.*;
import com.ty.voogla.R;
import com.ty.voogla.adapter.BoxLinkAdapter;
import com.ty.voogla.adapter.LayoutInit;
import com.ty.voogla.base.BaseActivity;
import com.ty.voogla.base.ResponseInfo;
import com.ty.voogla.bean.produce.DecodeCode;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.data.SimpleCache;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;
import com.ty.voogla.ui.activity.scan.BarcodeProperties;
import com.ty.voogla.util.ToastUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * @author TY on 2019/1/12.
 * <p>
 * 箱码绑定 --> 扫码
 */
public class BoxLinkJavaActivity extends BaseActivity implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener, VooglaContract.View<DecodeCode> {

    private com.honeywell.aidc.BarcodeReader barcodeReader;
    private RecyclerView boxRecycler;
    private boolean triggerState = false;

    private BoxLinkAdapter adapter;
    /**
     * 产品码
     */
    private ArrayList<String> qrCodeInfos = new ArrayList<>();
    /**
     * 箱码
     */
    private String boxCode;
    /**
     * 发货出库 箱码产品码
     */
    private ArrayList<QrCodeListData> qrCodeList = new ArrayList<>();

    /**
     * 发货出库-发货明细 item
     */
    private int sendPosition;

    /**
     * 是发货出库
     */
    private boolean isSendItem = false;

    private VooglaPresenter presenter = new VooglaPresenter(this);


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_box_link_code;
    }

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {

        String type = getIntent().getStringExtra(CodeConstant.PAGE_STATE_KEY);

        if (CodeConstant.PAGE_BOX_LINK.equals(type)) {
            // 入库扫码
            initToolBar(R.string.box_link, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnActivity();
                }
            });
        } else if (CodeConstant.PAGE_BOX_LINK_EDIT.equals(type)){
            // 修改编辑
            qrCodeInfos = getIntent().getStringArrayListExtra(CodeConstant.QR_CODE_INFOS);
            boxCode = getIntent().getStringExtra(CodeConstant.BOX_CODE);

            initToolBar(R.string.box_link, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnActivity();
                }
            });
        }else if (CodeConstant.PAGE_SCAN_OUT.equals(type)) {
            // 出库扫码
            isSendItem = true;
            try {
                sendPosition = getIntent().getIntExtra(CodeConstant.SEND_POSITION,-1);
                qrCodeList = SimpleCache.getQrCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (qrCodeList == null) {
                qrCodeList = new ArrayList();
            }
            initToolBar(R.string.scan_code, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(CodeConstant.SEND_POSITION, sendPosition);
                    SimpleCache.putQrCode(qrCodeList);

                    setResult(CodeConstant.RESULT_CODE, intent);
                    finish();
                }
            });
        }

        boxRecycler = findViewById(R.id.box_recycler);
        LayoutInit.INSTANCE.initLayoutManager(this, boxRecycler);
        boxRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new BoxLinkAdapter(this, R.layout.item_box_link, qrCodeInfos);
        boxRecycler.setAdapter(adapter);

    }

    @Override
    protected void initOneData() {

        // set lock the orientation
        // otherwise, the onDestory will trigger when orientation changes
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // get bar code instance from MainActivity
        barcodeReader = MainPdaJavaActivity.getBarcodeObject();

        if (barcodeReader != null) {

            // register bar code event listener
            barcodeReader.addBarcodeListener(this);

            // set the trigger mode to client control
            try {
                barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_CLIENT_CONTROL);
            } catch (UnsupportedPropertyException e) {
                Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT).show();
            }
            // register trigger state change listener
            barcodeReader.addTriggerListener(this);

            // 设置 50k 属性
            BarcodeProperties.setProperties(barcodeReader);
        }
//        boxRecycler = findViewById(R.id.box_recycler);
//        LayoutInit.INSTANCE.initLayoutManager(this, boxRecycler);
//        boxRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        adapter = new BoxLinkAdapter(this, R.layout.item_box_link, qrCodeInfos);
//        boxRecycler.setAdapter(adapter);

    }

    @Override
    protected void initTwoView() {


    }

    /**
     * 返回上级 Activity
     */
    private void returnActivity() {
        Intent intent = new Intent();
        intent.putExtra(CodeConstant.BOX_CODE, boxCode);
        intent.putExtra(CodeConstant.SEND_POSITION,sendPosition);
        intent.putStringArrayListExtra(CodeConstant.QR_CODE_INFOS, qrCodeInfos);
        setResult(CodeConstant.RESULT_CODE, intent);
        finish();

    }

    /*--------------- 扫码  start  ------------**/

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {

        String dataString = event.getBarcodeData();

        presenter.decodeUrlCode(dataString);

    }

    public void continuousScanning(boolean bState) {
        try {
            //turn on/off backlight
            barcodeReader.light(bState);
            //开关瞄准线
            barcodeReader.aim(bState);
            //开关解码功能
            barcodeReader.decode(bState);
        } catch (ScannerUnavailableException e) {
            e.printStackTrace();
        } catch (ScannerNotClaimedException e) {
            e.printStackTrace();
        }
        triggerState = bState;
    }

    // When using Automatic Trigger control do not need to implement the
    // onTriggerEvent function
    @Override
    public void onTriggerEvent(TriggerStateChangeEvent event) {

        if (event.getState()) {
            //turn on/off aimer, illumination and decoding
            try {
                barcodeReader.aim(!triggerState);
                barcodeReader.light(!triggerState);
                barcodeReader.decode(!triggerState);

                triggerState = !triggerState;
            } catch (ScannerNotClaimedException e) {
                e.printStackTrace();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent event) {
    }



    /*--------------- 扫码  end  ------------**/

    @Override
    public void onResume() {
        super.onResume();
        if (barcodeReader != null) {
            try {
                // 这个很关键
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (barcodeReader != null) {
            // release the scanner claim so we don't get any scanner
            // notifications while paused.
            barcodeReader.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (barcodeReader != null) {
            // unregister barcode event listener
            barcodeReader.removeBarcodeListener(this);

            // unregister trigger state change listener
            barcodeReader.removeTriggerListener(this);
        }
    }

    @Override
    public void showSuccess(DecodeCode data) {

        String code = data.getResult().getCode();
        ToastUtil.showToast(code);

        // TODO 方便测试 规定 4 个产品码
        if (qrCodeInfos.size() == 4) {
            boxCode = code;
            return;
        }
        if (qrCodeInfos.contains(code)) {
            ToastUtil.showToast("该数据扫码过");
            //ScanSoundUtil.showSound(getApplicationContext(), R.raw.scan_already);
        } else {
            qrCodeInfos.add(code);
            adapter.notifyItemInserted(qrCodeInfos.size());
            adapter.notifyItemRangeChanged(qrCodeInfos.size(), qrCodeInfos.size());
        }
        // 发货出库
        if(isSendItem){
            QrCodeListData codeData = new QrCodeListData();
            codeData.setQrCode(code);
            codeData.setQrCodeClass("产品码");
            qrCodeList.add(codeData);
        }

        // TODO  Thread.sleep 需改进
        try {
            Thread.sleep(500);
            // 继续扫码
            continuousScanning(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void showResponse(ResponseInfo response) {

    }

    @Override
    public void showError(String msg) {
        ToastUtil.showToast(msg);

    }
}
