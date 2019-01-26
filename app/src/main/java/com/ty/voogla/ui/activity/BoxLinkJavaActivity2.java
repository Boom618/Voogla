package com.ty.voogla.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
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
import com.ty.voogla.bean.produce.InBoxCodeDetailInfosBean;
import com.ty.voogla.bean.produce.QrCodeJudge;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.data.SimpleCache;
import com.ty.voogla.data.SparseArrayUtil;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;
import com.ty.voogla.ui.activity.scan.BarcodeProperties;
import com.ty.voogla.util.ToastUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TY on 2019/1/12.
 * <p>
 * 箱码绑定 --> 扫码
 */
public class BoxLinkJavaActivity2 extends BaseActivity implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener, VooglaContract.BoxLinkView<QrCodeJudge> {

    private BarcodeReader barcodeReader;
    private RecyclerView boxRecycler;
    private boolean triggerState = false;

    private BoxLinkAdapter adapter;
    /**
     * 产品码列表(全局展示类 ： QrCodeListData )
     */
    private ArrayList<QrCodeListData> qrCodeInfos = new ArrayList<>();
    /**
     * 箱码
     */
    private String lastCode;
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
     * 套码
     */
    private boolean isPackageCode = true;
    // 套码编号
    private String buApplyNo = "";

    /**
     * 当前码号、类别
     */
    private String currentCode = "";
    private String currentCodeClass = "";


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

    /**
     *  检测 重复码
     */
    ArrayList<String> repeatCodeList = new ArrayList<String>();


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

            List<QrCodeListData> infos = qrCodeList.get(sendPosition).getQrCodeInfos();
            // 获取 列表中的箱码号
            for (int i = 0; i < infos.size(); i++) {
                if (infos.get(i).getQrCodeClass().equals(CodeConstant.QR_CODE_0702)) {
                    lastCode = infos.get(i).getQrCode();
                }
            }
            // 套码有值
            buApplyNo = qrCodeList.get(sendPosition).getBuApplyNo();
            qrCodeInfos.addAll(infos);

            numberCode.setText(String.valueOf(this.qrCodeInfos.size()));
            initToolBar(R.string.box_link, "保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnActivity("productChange");
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

                // 套码 【默认 true 】
                if (buApplyNo == null) {

                    deleteView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            numberCode.setText(String.valueOf(qrCodeInfos.size() - 1));

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

        int temp = 0;
        // 产品码 + 箱码 = 规格大小 + 1
        if (size == specNumber + 1) {
            for (int i = 0; i < qrCodeInfos.size(); i++) {
                String codeClass = qrCodeInfos.get(i).getQrCodeClass();
                if (codeClass.equals(CodeConstant.QR_CODE_0702)) {
                    lastCode = qrCodeInfos.get(i).getQrCode();
                    temp ++;
                }
            }
            if (temp != 1) {
                ToastUtil.showToast("有且只能有一个箱码");
            }else {
                Intent intent = new Intent();
                intent.putExtra(CodeConstant.BOX_CODE, lastCode);
                intent.putExtra(CodeConstant.RESULT_TYPE, type);
                intent.putExtra(CodeConstant.SEND_POSITION, sendPosition);
                intent.putExtra("buApplyNo", buApplyNo);
                SimpleCache.putQrCode(qrCodeInfos);
                setResult(CodeConstant.RESULT_CODE, intent);
                finish();
            }
        } else {
            ToastUtil.showToast("产品数量和指定规格不一致");
        }


    }

    /*--------------- 扫码  start  ------------**/

    @Override
    public void onBarcodeEvent(final BarcodeReadEvent event) {

        final String dataString = event.getBarcodeData();
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
     * 解码成功 【 01 】
     *
     * @param result
     */
    @Override
    public void decodeCode(DecodeCode.ResultBean result) {

//        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
//            ToastUtil.showToast("==== MainLooper " + Thread.currentThread().getName());
//        }else{
//            ToastUtil.showToast("==== 不是 MainLooper " + Thread.currentThread().getName());
//        }

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
//            try {
//                Thread.sleep(500);
//                // 继续扫码
//                continuousScanning(true);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        } else {
            // 套码
            isPackageCode = true;
            httpJudegCode(code, codeClass);
        }

    }


    /**
     * 检查是否已扫码 code 【 02 】
     *
     * @param code
     */
    private void isContainsCode(String code, String codeClass) {
        repeatCodeList.clear();

        int tempClass = 0;
        int size = qrCodeInfos.size();
        if (size == 0) {
            //第一次
            httpJudegCode(code, codeClass);
        } else {
            for (int i = 0; i < size; i++) {
                String qrCode = qrCodeInfos.get(i).getQrCode();
                repeatCodeList.add(qrCode);
            }
            if (repeatCodeList.contains(code)) {
                ToastUtil.showToast("重复码号");
                //ScanSoundUtil.showSound(getApplicationContext(), R.raw.scan_already);
            } else {
                // 入库校验
                httpJudegCode(code, codeClass);
            }
        }

    }


    /**
     * 入库校验 【 03 】
     */
    private void httpJudegCode(String code, String codeClass) {

        presenter.judegCode(companyNo, code, codeClass);

        // boxCode 套码
        lastCode = code;
        currentCode = code;
        currentCodeClass = codeClass;

    }


    /**
     * 入库码校验 【 04 】
     *
     * @param response
     */
    @Override
    public void produceJudegCode(QrCodeJudge response) {

        // QrCodeJudge 主要是收取 【"generateNo": "二维码生成编号"】
        QrCodeJudge.QrCodeInfoBean qrCodeInfo = response.getQrCodeInfo();

        if (isPackageCode) {
            // 拉取套码 下的产品码
            // getCodeList(code);
            presenter.getQrCodeList(currentCode);
        } else {
            // 二维码生成编号( 新增要用 )
            String generateNo = qrCodeInfo.getGenerateNo();

            QrCodeListData qrCode = new QrCodeListData();
            qrCode.setQrCode(currentCode);
            qrCode.setQrCodeClass(currentCodeClass);
            qrCode.setGenerateNo(generateNo);

            qrCodeInfos.add(qrCode);

//            qrCodeString.add(currentCode);
//            qrClassString.add(currentCodeClass);

            numberCode.setText(String.valueOf(qrCodeInfos.size()));
//            adapter.notifyItemInserted(qrCodeInfos.size());
//            adapter.notifyItemRangeChanged(qrCodeInfos.size(), qrCodeInfos.size());
            adapter.notifyDataSetChanged();

            // 继续扫码
            continuousScanning(true);

        }

    }

    /**
     * 出库码校验
     *
     * @param response
     */
    @Override
    public void sendJudegCode(QrCodeJudge response) {
        // 校验成功

    }

    /**
     * 根据套码获取产品码
     *
     * @param codeList
     */
    @Override
    public void getCodeList(ArrayList<String> codeList) {

        qrCodeInfos.clear();
        ArrayList<String> data = codeList;
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
            code.setQrCode(currentCode);

            qrCodeInfos.add(code);

            numberCode.setText(String.valueOf(qrCodeSize + 1));
            adapter.notifyDataSetChanged();
        }

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
