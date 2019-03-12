//package com.ty.voogla.ui.activity.boxlink;
//
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.os.Bundle;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.honeywell.aidc.*;
//import com.ty.voogla.R;
//import com.ty.voogla.adapter.BoxLinkAdapter;
//import com.ty.voogla.adapter.LayoutInit;
//import com.ty.voogla.base.BaseActivity;
//import com.ty.voogla.base.BaseResponse;
//import com.ty.voogla.base.ResponseInfo;
//import com.ty.voogla.bean.produce.DecodeCode;
//import com.ty.voogla.bean.produce.QrCodeJudge;
//import com.ty.voogla.bean.sendout.QrCodeListData;
//import com.ty.voogla.constant.CodeConstant;
//import com.ty.voogla.data.SimpleCache;
//import com.ty.voogla.mvp.contract.VooglaContract;
//import com.ty.voogla.mvp.presenter.VooglaPresenter;
//import com.ty.voogla.net.HttpMethods;
//import com.ty.voogla.ui.activity.MainPdaJavaActivity;
//import com.ty.voogla.ui.activity.scan.BarcodeProperties;
//import com.ty.voogla.util.ToastUtil;
//import io.reactivex.SingleObserver;
//import io.reactivex.disposables.Disposable;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * @author TY on 2019/1/26.
// * 产品入库扫码
// */
//public class BoxLinkProduct extends BaseActivity implements BarcodeReader.BarcodeListener,
//        BarcodeReader.TriggerListener, VooglaContract.View<DecodeCode.ResultBean> {
//
//
//    private BarcodeReader barcodeReader;
//    private RecyclerView boxRecycler;
//    private boolean triggerState = false;
//
//    private VooglaPresenter presenter = new VooglaPresenter(this);
//
//    private BoxLinkAdapter adapter;
//
//    private ArrayList<QrCodeListData> qrCodeInfos = new ArrayList<>();
//
//    // 套码编号
//    private String buApplyNo = "";
//
//    /**
//     * 企业编号
//     */
//    private String companyNo;
//
//    /**
//     * 产品规格数量
//     */
//    private int specNumber = 0;
//
//   /**
//     * 箱码数量
//     */
//    private TextView numberCode;
//
//    // 箱码号
//    private String lastCode;
//
//    /**
//     * 套码
//     */
//    private boolean isPackageCode = true;
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
//    public void continuousScanning(boolean bState) {
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
//        companyNo = SimpleCache.getUserInfo().getCompanyNo();
//        numberCode = findViewById(R.id.tv_code_number);
//
//        initToolBar(R.string.box_link, "保存", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                returnActivity();
//            }
//        });
//
//    }
//
//
//    private void returnActivity() {
//
//        if (qrCodeInfos.size() == specNumber + 1) {
//
//            Intent intent = new Intent();
////            intent.putExtra(CodeConstant.BOX_CODE, boxCode);
//            intent.putExtra(CodeConstant.BOX_CODE, lastCode);
//            intent.putExtra(CodeConstant.RESULT_TYPE, "productIn");
////            intent.putExtra(CodeConstant.SEND_POSITION, sendPosition);
//            SimpleCache.putQrCode(qrCodeInfos);
//            setResult(CodeConstant.RESULT_CODE, intent);
//            finish();
//        } else {
//            ToastUtil.showToast("产品数量和指定规格不一致");
//        }
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
//        boxRecycler = findViewById(R.id.box_recycler);
//        LayoutInit.initLayoutManager(this, boxRecycler);
//        boxRecycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        adapter = new BoxLinkAdapter(this, R.layout.item_box_link, qrCodeInfos);
//        boxRecycler.setAdapter(adapter);
//
//    }
//
//
//    @Override
//    public void showSuccess(DecodeCode.ResultBean result) {
//
//        String code = result.getCode();
//        String qrCodeType = result.getQrCodeType();
//
//        // 套码编号
//        buApplyNo = result.getBuApplyNo();
//        // 箱码 A0702  产品吗 A0701
//        String codeClass = qrCodeType.equals("2") ? CodeConstant.QR_CODE_0702 : CodeConstant.QR_CODE_0701;
//
//        if (buApplyNo == null) {
//            isPackageCode = false;
//            isCheckRepeatCode(code,codeClass);
//        }else{
//            // 套码
//            isPackageCode = true;
//
//        }
//
//    }
//
//    /**
//     * 重复码检验
//     * @param code
//     * @param codeClass
//     */
//    private void isCheckRepeatCode(String code, String codeClass) {
//
//        httpJudegCode(code,codeClass);
//
//    }
//
//
//    @Override
//    public void showError(String msg) {
//        ToastUtil.showToast(msg);
//
//    }
//
//    @Override
//    public void showResponse(ResponseInfo response) {
//
//    }
//
//    /** ---   -----------      http ----------------       */
//        /**
//     * 入库校验
//     */
//    private void httpJudegCode(final String code, final String codeClass) {
//        HttpMethods.getInstance().judegCode(new SingleObserver<BaseResponse<QrCodeJudge>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//            }
//
//            @Override
//            public void onSuccess(BaseResponse<QrCodeJudge> response) {
//                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
//                    QrCodeJudge.QrCodeInfoBean qrCodeInfo = response.getData().getQrCodeInfo();
//
//                    if (isPackageCode) {
//                        // 拉取套码 下的产品码
//                        getCodeList(code);
//                        lastCode = codeClass;
//                    }else{
//                        // 二维码生成编号( 新增要用 )
//                        String generateNo = qrCodeInfo.getGenerateNo();
//                        QrCodeListData codeQr = new QrCodeListData();
//                        codeQr.setQrCode(code);
//                        codeQr.setQrCodeClass(codeClass);
//                        codeQr.setGenerateNo(generateNo);
//
//                        qrCodeInfos.add(codeQr);
//
//                        if (codeClass.equals(CodeConstant.QR_CODE_0702)) {
//                            lastCode = code;
//                        }
//
//                        numberCode.setText(String.valueOf(qrCodeInfos.size()));
//                        adapter.notifyItemInserted(qrCodeInfos.size());
//                        adapter.notifyItemRangeChanged(qrCodeInfos.size(), qrCodeInfos.size());
//                    }
//                } else {
//                    ToastUtil.showToast(response.getMsg());
//                }
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                ToastUtil.showToast(e.getMessage());
//
//            }
//        }, companyNo, code, codeClass);
//
//    }
//
//        /**
//     * Http 根据套码箱码获取产品码
//     */
//    private void getCodeList(final String qrCode) {
//        HttpMethods.getInstance().getQrCodeList(new SingleObserver<BaseResponse<ArrayList<String>>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//            }
//
//            @Override
//            public void onSuccess(BaseResponse<ArrayList<String>> response) {
//                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
//
//                    qrCodeInfos.clear();
//                    ArrayList<String> data = response.getData();
//                    int qrCodeSize = data.size();
//                    if (qrCodeSize != specNumber) {
//                        ToastUtil.showToast("请扫指定规格的套码");
//                    } else {
//                        for (int i = 0; i < data.size(); i++) {
//                            QrCodeListData qrCodeData = new QrCodeListData();
//                            qrCodeData.setQrCodeClass("A0701");
//                            qrCodeData.setQrCode(data.get(i));
//                            qrCodeInfos.add(qrCodeData);
//                        }
//                        QrCodeListData code = new QrCodeListData();
//                        code.setQrCodeClass("A0702");
//                        code.setQrCode(qrCode);
//                        qrCodeInfos.add(code);
//
//                        numberCode.setText(String.valueOf(qrCodeSize + 1));
//                        adapter.notifyDataSetChanged();
//                    }
//
//                } else {
//                    ToastUtil.showToast(response.getMsg());
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                ToastUtil.showToast(e.getMessage());
//            }
//        }, qrCode);
//    }
//}
