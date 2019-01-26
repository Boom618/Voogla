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
import com.ty.voogla.data.SharedP;
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
    private ArrayList<QrCodeListData> qrCodeInfos = new ArrayList<>();
    /**
     * 方便对比集合中是否有该二维码,不做数据传递
     * qrCodeString   重复码
     * qrClassString  重复类（箱码只能一个）
     */
    private ArrayList<String> qrCodeString = new ArrayList<>();
    private ArrayList<String> qrClassString = new ArrayList<>();
    /**
     * 箱码
     */
    private String boxCode;
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
     * 产品规格数量
     */
    private int specNumber = 0;

    /**
     * 套码
     */
    private boolean isPackageCode = true;
    // 套码编号
    private String buApplyNo = "";

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
            List<QrCodeListData> infos = qrCodeList.get(0).getQrCodeInfos();
            qrCodeInfos.addAll(infos);
            for (int i = 0; i < qrCodeInfos.size(); i++) {
                // 数量控制
                qrCodeString.add(qrCodeInfos.get(i).getQrCode());
            }

            boxCode = getIntent().getStringExtra(CodeConstant.BOX_CODE);

            numberCode.setText(String.valueOf(this.qrCodeInfos.size()));
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
            lastPosition = SharedP.getKeyPosition(this, "lastPosition");

            goodsNo = getIntent().getStringExtra("goodsNo");

            // 是同一列
            if (sendPosition == lastPosition) {
                qrCodeInfos = SimpleCache.getQrCode();
                if (qrCodeInfos == null) {
                    qrCodeInfos = new ArrayList();
                }
                for (int i = 0; i < qrCodeInfos.size(); i++) {
                    qrCodeString.add(qrCodeInfos.get(i).getQrCode());
                }
            }
            numberCode.setText(String.valueOf(this.qrCodeInfos.size()));
            initToolBar(R.string.scan_code, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (qrCodeInfos.size() == 0) {
                        ToastUtil.showToast("码数量不能为空");
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra(CodeConstant.SEND_POSITION, sendPosition);
                    SimpleCache.putQrCode(qrCodeInfos);
                    SharedP.putKeyPosition(BoxLinkJavaActivity.this, "lastPosition", sendPosition);

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

                // 套码禁用产品删除 【默认 true 】
                if (!isPackageCode) {
                    // 先不显示
                    deleteView.setVisibility(View.GONE);
                    deleteView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            numberCode.setText(String.valueOf(qrCodeInfos.size() - 1));

                            if (qrCodeInfos.get(position).getQrCodeClass().equals(CodeConstant.QR_CODE_0702)) {
                                qrClassString.clear();
                            }

                            // TODO　数量控制 （删除产品码有问题）
                            qrCodeString.remove(position);

                            qrCodeInfos.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, qrCodeInfos.size() - position);
                        }
                    });
                } else {
                    // 套码一删全删
                    deleteView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            numberCode.setText("0");
                            qrCodeInfos.clear();
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

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

        // 产品码 + 箱码 = 规格大小 + 1
        if (size == specNumber + 1) {

            InBoxCodeDetailInfosBean bean = new InBoxCodeDetailInfosBean();
            bean.setQrCode(boxCode);
            bean.setQrCodeInfos(qrCodeInfos);

            Intent intent = new Intent();
            intent.putExtra(CodeConstant.BOX_CODE, boxCode);
            intent.putExtra(CodeConstant.RESULT_TYPE, type);
            intent.putExtra("buApplyNo", buApplyNo);
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
        // 套码编号
        buApplyNo = result.getBuApplyNo();
        // 箱码 A0702  产品吗 A0701
        final String codeClass = qrCodeType.equals("2") ? CodeConstant.QR_CODE_0702 : CodeConstant.QR_CODE_0701;

        // 不是套码
        if (buApplyNo == null) {
            isPackageCode = false;
            // 发货出库
            if (isSendItem) {
                // 是否重复 code
                isContainsCode(code, codeClass);

            } else {
                // 生产入库
                int size = qrCodeInfos.size();
                // 产品码数量由规格控制、符合规格数量停止扫码
                if (size == specNumber + 1) {
                    return;
                }
                isContainsCode(code, codeClass);
            }

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
            if (isSendItem) {
                sendOutjudegCode(companyNo, codeClass, goodsNo, code);
            } else {
                httpJudegCode(code, codeClass);
            }
        }


    }

    /**
     * 检查是否已扫码 code
     *
     * @param code
     */
    private void isContainsCode(String code, String codeClass) {
        if (qrCodeString.contains(code) || qrClassString.size() > 1) {
            ToastUtil.showToast("重复码号或多个箱码");
            //ScanSoundUtil.showSound(getApplicationContext(), R.raw.scan_already);
        } else {
            if (isSendItem) {
                // 出库校验
                sendOutjudegCode(companyNo, codeClass, goodsNo, code);
            } else {
                // 入库校验
                httpJudegCode(code, codeClass);
            }
        }
    }

    /**
     * 入库校验
     */
    private void httpJudegCode(final String code, final String codeClass) {
        HttpMethods.getInstance().judegCode(new SingleObserver<BaseResponse<QrCodeJudge>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<QrCodeJudge> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    QrCodeJudge.QrCodeInfoBean qrCodeInfo = response.getData().getQrCodeInfo();

                    if (isPackageCode) {
                        // 拉取套码 下的产品码
                        getCodeList(code);
                        boxCode = codeClass;
                    }else{
                        // 二维码生成编号( 新增要用 )
                        String generateNo = qrCodeInfo.getGenerateNo();
                        QrCodeListData codeQr = new QrCodeListData();
                        codeQr.setQrCode(code);
                        codeQr.setQrCodeClass(codeClass);
                        codeQr.setGenerateNo(generateNo);

                        qrCodeInfos.add(codeQr);


                        if (codeClass.equals(CodeConstant.QR_CODE_0702) && qrClassString.size() == 1) {
                            ToastUtil.showToast("只能有一个箱码");
                        } else {
                            // 显示扫码数量
                            qrCodeString.add(code);
                            qrClassString.add(codeClass);
                            numberCode.setText(String.valueOf(qrCodeString.size()));
                            adapter.notifyItemInserted(qrCodeInfos.size());
                            adapter.notifyItemRangeChanged(qrCodeInfos.size(), qrCodeInfos.size());
                        }
                    }
                } else {
                    ToastUtil.showToast(response.getMsg());
                }

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(e.getMessage());

            }
        }, companyNo, code, codeClass);

    }


    /**
     * 出库校验
     *
     * @param companyNo   企业编号
     * @param qrCodeClass 商品编号
     * @param goodsNo     产品码A0701、箱码A0702
     * @param qrCode      二维码
     */
    private void sendOutjudegCode(String companyNo, final String qrCodeClass, String goodsNo, final String qrCode) {
        HttpMethods.getInstance().sendOutjudegCode(new SingleObserver<BaseResponse<QrCodeJudge>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<QrCodeJudge> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {

                    QrCodeJudge.QrCodeInfoBean qrCodeInfo = response.getData().getQrCodeInfo();
                    QrCodeListData codeData = new QrCodeListData();
                    codeData.setQrCode(qrCode);
                    codeData.setQrCodeClass(qrCodeClass);
                    codeData.setWareName(qrCodeInfo.getWareName());
                    codeData.setInBatchNo(qrCodeInfo.getInBatchNo());

                    qrCodeInfos.add(codeData);

                    if (qrCodeClass.equals(CodeConstant.QR_CODE_0702) && qrClassString.size() == 1) {
                        ToastUtil.showToast("只能有一个箱码");
                    } else {
                        // 显示扫码数量
                        qrCodeString.add(qrCode);
                        qrClassString.add(qrCodeClass);
                        numberCode.setText(String.valueOf(qrCodeString.size()));
                        adapter.notifyItemInserted(qrCodeInfos.size());
                        adapter.notifyItemRangeChanged(qrCodeInfos.size(), qrCodeInfos.size());
                    }
                } else {
                    ToastUtil.showToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(e.getMessage());

            }
        }, companyNo, qrCodeClass, goodsNo, qrCode);
    }

    /**
     * Http 根据套码箱码获取产品码
     */
    private void getCodeList(final String qrCode) {
        HttpMethods.getInstance().getQrCodeList(new SingleObserver<BaseResponse<ArrayList<String>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<ArrayList<String>> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {

                    qrCodeInfos.clear();
                    ArrayList<String> data = response.getData();
                    int qrCodeSize = data.size();
                    if (qrCodeSize != specNumber) {
                        ToastUtil.showToast("请扫指定规格的套码");
                    } else {
                        for (int i = 0; i < data.size(); i++) {
                            QrCodeListData qrCodeData = new QrCodeListData();
                            qrCodeData.setQrCodeClass("A0701");
                            qrCodeData.setQrCode(data.get(i));
                            qrCodeInfos.add(qrCodeData);
                        }
                        QrCodeListData code = new QrCodeListData();
                        code.setQrCodeClass("A0702");
                        code.setQrCode(qrCode);
                        qrCodeInfos.add(code);

                        numberCode.setText(String.valueOf(qrCodeSize + 1));
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    ToastUtil.showToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(e.getMessage());
            }
        }, qrCode);
    }

    @Override
    public void showResponse(ResponseInfo response) {

    }

    @Override
    public void showError(String msg) {
        ToastUtil.showToast(msg);

    }
}
