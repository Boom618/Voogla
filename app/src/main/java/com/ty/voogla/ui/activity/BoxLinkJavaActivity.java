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
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.ui.activity.scan.BarcodeProperties;
import com.ty.voogla.util.ToastUtil;
import com.ty.voogla.util.scan.ScanSoundUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TY on 2019/1/12.
 * <p>
 * 箱码绑定 --> 扫码
 */
public class BoxLinkJavaActivity extends BaseActivity implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener {

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


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_box_link_code;
    }

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {

        String type = getIntent().getStringExtra(CodeConstant.PAGE_STATE);

        if (CodeConstant.PAGE_BOX_LINK.equals(type)) {
            initToolBar(R.string.box_link, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast("保存数据");
                    returnActivity();
                }
            });
        }else if(CodeConstant.PAGE_SCAN_OUT.equals(type)){
            initToolBar(R.string.scan_code, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast("保存数据");
                    returnActivity();
                }
            });
        }

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
//        listCode.add("a");
//        listCode.add("b");
//        listCode.add("c");
        boxRecycler = findViewById(R.id.box_recycler);
        LayoutInit.INSTANCE.initLayoutManager(this, boxRecycler);
        boxRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new BoxLinkAdapter(this, R.layout.item_box_link, qrCodeInfos);
        boxRecycler.setAdapter(adapter);

    }

    @Override
    protected void initTwoView() {


    }

    /**
     * 返回上级 Activity
     */
    private void returnActivity() {
        Intent intent = new Intent();
        intent.putExtra("boxCode",boxCode);
        intent.putStringArrayListExtra("qrCodeInfos",qrCodeInfos);
        setResult(100,intent);
        finish();

    }

    /*--------------- 扫码  start  ------------**/

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // update UI to reflect the data
                List<String> list = new ArrayList<String>();
                list.add("Barcode data: " + event.getBarcodeData());
                list.add("Character Set: " + event.getCharset());
                list.add("Code ID: " + event.getCodeId());
                list.add("AIM ID: " + event.getAimId());
                list.add("Timestamp: " + event.getTimestamp());

                String dataString = event.getBarcodeData();
                if (qrCodeInfos.size() < 3) {
                    boolean contains = qrCodeInfos.contains(dataString);
                    if (contains) {
                        ToastUtil.showToast("该数据扫码过");
                        ScanSoundUtil.showSound(getApplicationContext(), R.raw.scan_already);
                    } else {
                        qrCodeInfos.add(dataString);
                        adapter.notifyDataSetChanged();
                    }
                    ToastUtil.showToast("已扫码 2 盒,请扫箱码");
                } else {
                    boxCode = dataString;
                }


                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continuousScanning(true);
            }
        });
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
}
