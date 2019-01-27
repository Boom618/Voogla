package com.ty.voogla.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.honeywell.aidc.*;
import com.ty.voogla.R;
import com.ty.voogla.adapter.BoxLinkAdapter;
import com.ty.voogla.adapter.LayoutInit;
import com.ty.voogla.base.BaseActivity;
import com.ty.voogla.bean.produce.DecodeCode;
import com.ty.voogla.bean.produce.QrCodeJudge;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.data.SharedP;
import com.ty.voogla.data.SimpleCache;
import com.ty.voogla.data.SparseArrayUtil;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;
import com.ty.voogla.ui.activity.scan.BarcodeProperties;
import com.ty.voogla.util.ToastUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import io.reactivex.disposables.Disposable;
import kotlin.reflect.jvm.internal.impl.resolve.constants.StringValue;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author TY on 2019/1/12.
 * <p>
 * 箱码绑定 --> 扫码(出库)
 */
public class BoxLinkJavaActivity3 extends BaseActivity implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener, VooglaContract.BoxLinkView {

    private BarcodeReader barcodeReader;
    private RecyclerView boxRecycler;
    private boolean triggerState = false;

    private BoxLinkAdapter adapter;
    /**
     * 产品码列表(全局展示类 ： QrCodeListData )
     */
    private ArrayList<QrCodeListData> qrCodeInfos = new ArrayList<>();

    /**
     * 发货出库-发货明细 item
     */
    private int sendPosition;
    /**
     * 企业编号
     */
    private String companyNo;
    /**
     * 商品编号 （出库）
     */
    private String goodsNo;


    /**
     * 套码
     */
    private boolean isPackageCode = true;
    // 套码编号
    private String buApplyNo = "";

    /**
     * 出库箱数、出库产品数
     * 二维码、类别
     */
    private int outBoxNum = 0;
    private int outGoodsNum = 0;
    private String lastCode = "";
    private String lastCodeClass = "";
    /**
     * 重复码
     */
    private ArrayList<String> repeatCodeList = new ArrayList<>();


    private VooglaPresenter presenter = new VooglaPresenter(this);
    private Disposable disposable;

    /**
     * 箱码数量显示
     */
    private TextView numberCode;

    /**
     * 上一次进来的位置
     */
    private int lastPosition = 0;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_box_link_code;
    }

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {

        companyNo = SimpleCache.getUserInfo().getCompanyNo();

        numberCode = findViewById(R.id.tv_code_number);

        sendPosition = getIntent().getIntExtra(CodeConstant.SEND_POSITION, -1);
        lastPosition = SharedP.getKeyPosition(this, "lastPosition");

        goodsNo = getIntent().getStringExtra("goodsNo");

        try {
            HashMap<Integer, ArrayList<QrCodeListData>> qrCodeSend = SparseArrayUtil.getQrCodeSend(this);
            qrCodeInfos = qrCodeSend.get(sendPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (qrCodeInfos == null) {
            qrCodeInfos = new ArrayList();
        }
        int size = qrCodeInfos.size();
        for (int i = 0; i < size; i++) {
            repeatCodeList.add(qrCodeInfos.get(i).getQrCode());
        }
        numberCode.setText(String.valueOf(size));

        initToolBar(R.string.scan_code, "保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrCodeInfos.size() == 0) {
                    ToastUtil.showToast("码数量不能为空");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(CodeConstant.SEND_POSITION, sendPosition);
                HashMap<Integer,ArrayList<QrCodeListData>> array = null;
                try {
                    array = SparseArrayUtil.getQrCodeSend(BoxLinkJavaActivity3.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (array == null) {
                    array = new HashMap<>();
                }

                array.put(sendPosition,qrCodeInfos);
//                SimpleCache.putQrCode(qrCodeInfos);
                SparseArrayUtil.putQrCodeSend(BoxLinkJavaActivity3.this,array);

                SharedP.putKeyPosition(BoxLinkJavaActivity3.this, "lastPosition", sendPosition);

                setResult(CodeConstant.RESULT_CODE, intent);
                finish();
            }
        });

        boxRecycler = findViewById(R.id.box_recycler);
        LayoutInit.initLayoutManager(this, boxRecycler);
        boxRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new BoxLinkAdapter(this, R.layout.item_box_link, qrCodeInfos);
        boxRecycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {

                ImageView deleteView = holder.itemView.findViewById(R.id.image_delete);

                deleteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        numberCode.setText(String.valueOf(qrCodeInfos.size() - 1));

                        qrCodeInfos.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, qrCodeInfos.size() - position);
                    }
                });

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

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

    }

    @Override
    protected void initTwoView() {


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


    /**
     * 检查是否已扫码 code
     *
     * @param code
     */
    private void isContainsCode(String code, String codeClass) {

        if (repeatCodeList.contains(code)) {
            ToastUtil.showToast("重复码号");
            //ScanSoundUtil.showSound(getApplicationContext(), R.raw.scan_already);
        } else {

            // 出库校验
            sendOutjudegCode(companyNo, codeClass, goodsNo, code);
        }
    }


    /**
     * 解码成功
     *
     * @param result
     */
    @Override
    public void decodeCode(DecodeCode.ResultBean result) {

        final String code = result.getCode();
        String applyNo = result.getApplyNo();
        String qrCodeType = result.getQrCodeType();
        // 套码编号
        buApplyNo = result.getBuApplyNo();
        // 箱码 A0702  产品吗 A0701
        final String codeClass = qrCodeType.equals("2") ? CodeConstant.QR_CODE_0702 : CodeConstant.QR_CODE_0701;

        // 不是套码
        if (buApplyNo == null) {
            isPackageCode = false;

            isContainsCode(code, codeClass);

            // TODO  Thread.sleep 需改进
            try {
                Thread.sleep(500);
                // 继续扫码
                continuousScanning(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // 套码
            isPackageCode = true;
            isContainsCode(code, codeClass);
            //sendOutjudegCode(companyNo, codeClass, goodsNo, code);
        }

    }

    /**
     * 出库校验
     *
     * @param companyNo   企业编号
     * @param qrCodeClass 商品编号
     * @param goodsNo     产品码A0701、箱码A0702
     * @param qrCode      二维码
     */
    private void sendOutjudegCode(String companyNo, String qrCodeClass, String goodsNo, String qrCode) {

        presenter.sendOutjudegCode(companyNo, qrCodeClass, goodsNo, qrCode);
        lastCodeClass = qrCodeClass;
        lastCode = qrCode;
    }

    /**
     * 入库码校验
     *
     * @param response
     */
    @Override
    public void produceJudegCode(String response) {

    }

    /**
     * 出库码校验
     *
     * @param response
     */
    @Override
    public void sendJudegCode(String response) {

        if (lastCodeClass.equals(CodeConstant.QR_CODE_0702)) {
            outBoxNum++;
        } else {
            outGoodsNum++;
        }
        repeatCodeList.add(lastCode);
        // 校验成功直接添加数据
        QrCodeListData data = new QrCodeListData();
        data.setQrCode(lastCode);
        data.setQrCodeClass(lastCodeClass);

        qrCodeInfos.add(data);

        numberCode.setText(String.valueOf(qrCodeInfos.size()));
        adapter.notifyItemInserted(qrCodeInfos.size());
        adapter.notifyItemRangeChanged(qrCodeInfos.size(), qrCodeInfos.size());


    }

    /**
     * 根据套码获取产品码
     *
     * @param codeList
     */
    @Override
    public void getCodeList(ArrayList<String> codeList) {


    }

    @Override
    public void showError(String msg) {
        ToastUtil.showToast(msg);

    }

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
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
