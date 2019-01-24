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
import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.base.ResponseInfo;
import com.ty.voogla.bean.produce.DecodeCode;
import com.ty.voogla.bean.produce.InBoxCodeDetailInfosBean;
import com.ty.voogla.bean.produce.QrCodeJudge;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.data.SimpleCache;
import com.ty.voogla.data.SparseArrayUtil;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;
import com.ty.voogla.net.HttpMethods;
import com.ty.voogla.ui.activity.scan.BarcodeProperties;
import com.ty.voogla.util.ToastUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TY on 2019/1/12.
 * <p>
 * 箱码绑定 --> 扫码
 */
public class BoxLinkJavaActivity extends BaseActivity implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener, VooglaContract.View<DecodeCode.ResultBean> {

    private com.honeywell.aidc.BarcodeReader barcodeReader;
    private RecyclerView boxRecycler;
    private boolean triggerState = false;

    private BoxLinkAdapter adapter;
    /**
     * 产品码列表
     */
    private List<QrCodeListData> qrCodeInfos = new ArrayList<>();
    /**
     * 方便对比集合中是否有该二维码,不做数据传递
     */
    private ArrayList<String> qrCodeString = new ArrayList<>();
    /**
     * 箱码
     */
    private String boxCode;
    /**
     * 发货出库 箱码产品码 (要删)
     */
    private ArrayList<QrCodeListData> qrCodeList = new ArrayList<>();

    /**
     * 发货出库-发货明细 item
     */
    private int sendPosition;
    /**
     * 企业编号
     */
    private String companyNo;

    /**
     * 产品规格数量
     */
    private int specNumber = 0;

    /**
     * 是发货出库
     */
    private boolean isSendItem = false;

    private VooglaPresenter presenter = new VooglaPresenter(this);
    private Disposable disposable;

    /**
     * 箱码数量
     */
    private TextView numberCode;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_box_link_code;
    }

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {

        String type = getIntent().getStringExtra(CodeConstant.PAGE_STATE_KEY);
        companyNo = SimpleCache.getUserInfo().getCompanyNo();

        numberCode = findViewById(R.id.tv_code_number);
        if (CodeConstant.PAGE_BOX_LINK.equals(type)) {
            // 入库扫码
            initToolBar(R.string.box_link, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnActivity("productIn");
                }
            });
            String spec = SimpleCache.getString(CodeConstant.GOODS_SPEC);
            specNumber = Integer.parseInt(spec);
        } else if (CodeConstant.PAGE_BOX_LINK_EDIT.equals(type)) {
            // 修改编辑
            String spec = SimpleCache.getString(CodeConstant.GOODS_SPEC);
            specNumber = Integer.parseInt(spec);
            sendPosition = getIntent().getIntExtra("position", 0);
            List<InBoxCodeDetailInfosBean> qrCodeList = SparseArrayUtil.getQrCodeList(this);
            qrCodeInfos =  qrCodeList.get(0).getQrCodeInfos();
            for (int i = 0; i < qrCodeInfos.size(); i++) {
                // 数量控制
                qrCodeString.add(qrCodeInfos.get(i).getQrCode());
            }

            boxCode = getIntent().getStringExtra(CodeConstant.BOX_CODE);

            numberCode.setText(String.valueOf(qrCodeInfos.size()));
            initToolBar(R.string.box_link, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnActivity("productChange");
                }
            });
        } else if (CodeConstant.PAGE_SCAN_OUT.equals(type)) {
            // 出库扫码
            isSendItem = true;
            sendPosition = getIntent().getIntExtra(CodeConstant.SEND_POSITION, -1);
            qrCodeList = SimpleCache.getQrCode();
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
                        // 数量控制
                        qrCodeString.remove(position);

                        qrCodeInfos.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position,qrCodeInfos.size() - position);
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

    /**
     * 返回上级 Activity
     */
    private void returnActivity(String type) {
        int size = qrCodeInfos.size();
        int temp = 0;

        // 产品码 + 箱码 = 规格大小 + 1
        if (size == specNumber + 1) {
            if ("productChange".equals(type)) {
                // 修改 必须控制最后一位才能是箱码
                for (int i = 0; i < size; i++) {
                    String qrCodeClass = qrCodeInfos.get(i).getQrCodeClass();
                    if (qrCodeClass.equals("A0702")) {
                        temp ++;
                    }
                    if (i == size -1){
                        if (!qrCodeClass.equals("A0702")) {
                            ToastUtil.showToast("箱码必须最后扫码");
                        }
                    }
                }
                if (temp > 1) {
                    ToastUtil.showToast("只能含有一个箱码");
                    return;
                }
            }

            InBoxCodeDetailInfosBean bean = new InBoxCodeDetailInfosBean();
            bean.setQrCode(boxCode);
            bean.setQrCodeInfos(qrCodeInfos);

            Intent intent = new Intent();
            intent.putExtra(CodeConstant.BOX_CODE, boxCode);
            intent.putExtra(CodeConstant.RESULT_TYPE, type);
            intent.putExtra(CodeConstant.SEND_POSITION, sendPosition);
            SimpleCache.putInBoxCode(bean);
            setResult(CodeConstant.RESULT_CODE, intent);
            finish();
        } else {
            ToastUtil.showToast("产品数量和指定规格不一致");
        }


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
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void showSuccess(final DecodeCode.ResultBean result) {

        final String code = result.getCode();
        String applyNo = result.getApplyNo();
        String qrCodeType = result.getQrCodeType();
        // 箱码 A0702  产品吗 A0701
        final String codeClass = qrCodeType.equals("2") ? "A0702" : "A0701";

        int size = qrCodeInfos.size();
        // 产品码数量由规格控制
        if (size == specNumber + 1) {

            return;
        }

        // TODO  逻辑需要改进
        // 符合规格数量
        if (size < specNumber) {
            // 1 为产品码,2 为箱码
            if (qrCodeType.equals("1")) {
                // 是否重复 code
                isContainsCode(code,codeClass);
            } else {
                ToastUtil.showToast("请先扫产品码");
            }
        } else {
            if (qrCodeType.equals("2")) {
                isContainsCode(code,codeClass);
                boxCode = code;
            } else {
                ToastUtil.showToast("请扫箱码");
            }
        }

        // 发货出库
        if (isSendItem) {
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

    /**
     * 检查是否已扫码 code
     * @param code
     */
    private void isContainsCode(String code,String codeClass){
        if (qrCodeString.contains(code)) {
            ToastUtil.showToast("重复码号");
            //ScanSoundUtil.showSound(getApplicationContext(), R.raw.scan_already);
        } else {
            // 服务器校验码
            httpJudegCode(code,codeClass);
        }
    }

    /**
     * 入库校验
     */
    private void httpJudegCode(final String code,final String codeClass) {
        HttpMethods.getInstance().judegCode(new SingleObserver<BaseResponse<QrCodeJudge>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<QrCodeJudge> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    QrCodeJudge.QrCodeInfoBean qrCodeInfo = response.getData().getQrCodeInfo();

                    // 二维码生成编号( 新增要用 )
                    String generateNo = qrCodeInfo.getGenerateNo();
                    QrCodeListData codeQr = new QrCodeListData();
                    codeQr.setQrCode(code);
                    codeQr.setQrCodeClass(codeClass);
                    codeQr.setGenerateNo(generateNo);

                    qrCodeInfos.add(codeQr);

                    // 显示扫码数量
                    qrCodeString.add(code);
                    numberCode.setText(String.valueOf(qrCodeString.size()));
                    adapter.notifyItemInserted(qrCodeInfos.size());
                    adapter.notifyItemRangeChanged(qrCodeInfos.size(), qrCodeInfos.size());
                }else{
                    ToastUtil.showToast(response.getMsg());
                }

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(e.getMessage());

            }
        }, companyNo, code);

    }


    @Override
    public void showResponse(ResponseInfo response) {

    }

    @Override
    public void showError(String msg) {
        ToastUtil.showToast(msg);

    }
}
