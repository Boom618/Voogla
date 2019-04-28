package com.ty.voogla.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.mexxen.barcode.BarcodeConfig;
import com.mexxen.barcode.BarcodeEvent;
import com.mexxen.barcode.BarcodeListener;
import com.mexxen.barcode.BarcodeManager;
import com.ty.voogla.R;
import com.ty.voogla.adapter.BoxLinkAdapter;
import com.ty.voogla.adapter.LayoutInit;
import com.ty.voogla.base.BaseActivity;
import com.ty.voogla.bean.produce.DecodeCode;
import com.ty.voogla.bean.produce.InBoxCodeDetailInfosBean;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.constant.TipString;
import com.ty.voogla.data.*;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;
import com.ty.voogla.util.ToastUtil;
import com.ty.voogla.util.ZBLog;
import com.ty.voogla.util.scan.PDAUtil;
import com.ty.voogla.widght.DialogUtil;
import com.ty.voogla.widght.NormalAlertDialog;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author TY on 2019/1/12.
 * <p>
 * 箱码绑定 --> 扫码（入库）
 */
public class BoxLinkJavaActivity2 extends BaseActivity implements VooglaContract.BoxLinkView {

    private BarcodeManager mBarcodeManager;
    private BarcodeConfig mBarcodeConfig;
    private BarcodeListener listener;

    private RecyclerView boxRecycler;
    private EditText companyView;
    private TextView tvBox;
    private ImageView deleteView;
    // 修改接收的企业箱号
    private String comBoxCode;

    private BoxLinkAdapter adapter;
    /**
     * 产品码列表(全局展示类 ： QrCodeListData )
     */
    private ArrayList<QrCodeListData> qrCodeInfos = new ArrayList<>();
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
     * 产品规格数量
     */
    private int specNumber = 20;

    /**
     * 套码
     */
    private boolean isPackageCode = false;
    /**
     * 更换类型（套码和卷码）
     */
    private boolean typeSwitch = false;
    // 套码编号
    private String buApplyNo = "";

    /**
     * 当前码号、类别
     */
    private String currentCode = "";
    private String currentCodeClass = "";


    private VooglaPresenter presenter = new VooglaPresenter(this);
    private Disposable subscribe;

    private int timeScan = 1000;

    /**
     * 箱码数量显示
     */
    private TextView numberCode;

    /**
     * 检测 重复码
     */
//    ArrayList<String> repeatCodeList = new ArrayList<String>();

    /**
     * 所有已扫过的码,校验
     */
    private List<InBoxCodeDetailInfosBean> allCode;

    // 检测是否在扫码中 默认 false
    private boolean isScanIng = false;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_box_link_code_in;
    }

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {

        String type = getIntent().getStringExtra(CodeConstant.PAGE_STATE_KEY);
        companyNo = SimpleCache.getUserInfo().getCompanyNo();

        tvBox = findViewById(R.id.box_code);
        deleteView = findViewById(R.id.img_delete);
        companyView = findViewById(R.id.et_code);
        numberCode = findViewById(R.id.tv_code_number);
        if (CodeConstant.PAGE_BOX_LINK.equals(type)) {
            // 入库扫码
            initToolBar(R.string.box_link, TipString.save, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnActivity("productIn");
                }
            });
            String spec = SimpleCache.getString(CodeConstant.GOODS_SPEC);
            specNumber = Integer.parseInt(spec);
            // 全部码
            allCode = SparseArrayUtil.getQrCodeList(this);
        } else if (CodeConstant.PAGE_BOX_LINK_EDIT.equals(type)) {
            // 修改编辑
            String spec = SimpleCache.getString(CodeConstant.GOODS_SPEC);
            specNumber = Integer.parseInt(spec);
            sendPosition = getIntent().getIntExtra("position", 0);
            comBoxCode = getIntent().getStringExtra("comBoxCode");
            boxCode = getIntent().getStringExtra("boxCode");
            companyView.setText(comBoxCode);
            allCode = SparseArrayUtil.getQrCodeList(this);

            List<QrCodeListData> infos = allCode.get(sendPosition).getQrCodeInfos();
            tvBox.setText(boxCode);
            deleteView.setVisibility(View.VISIBLE);
            // 套码有值
            buApplyNo = allCode.get(sendPosition).getBuApplyNo();
            qrCodeInfos.addAll(infos);
            // 修改 -- 初始化重复码（存放产品码）
//            for (int i = 0; i < infos.size(); i++) {
//                String qrCode = infos.get(i).getQrCode();
//                repeatCodeList.add(qrCode);
//            }

            numberCode.setText(String.valueOf(this.qrCodeInfos.size()));
            initToolBar(R.string.box_link, TipString.save, new View.OnClickListener() {
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
            public void onItemClick(final View view, RecyclerView.ViewHolder holder, final int position) {

                final ImageView deleteView = holder.itemView.findViewById(R.id.image_delete);

                // 非套码
                if (buApplyNo == null) {

                    deleteView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            qrCodeInfos.remove(position);
                            numberCode.setText(String.valueOf(qrCodeInfos.size()));
                            // 重复码集合
//                            repeatCodeList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, qrCodeInfos.size() - position);
                            ToastUtil.showSuccess(TipString.deleteSuccess);
                        }
                    });
                } else {
                    // 套码一删全删
                    deleteView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            numberCode.setText("0");
                            tvBox.setText("");
                            qrCodeInfos.clear();
                            deleteView.setVisibility(View.GONE);
//                            repeatCodeList.clear();
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
        mBarcodeConfig = new BarcodeConfig(this);
        mBarcodeManager = new BarcodeManager(this);
        // 扫码监听
        if (listener == null) {
            listener = new BarcodeListener() {
                @Override
                public void barcodeEvent(BarcodeEvent event) {
                    // TODO Auto-generated method stub
                    Log.d("TAG", "---->>heww barcodeEvent() event =" + event.getOrder());
                    // 当条码事件的命令为“SCANNER_READ”时，进行操作
                    if (event.getOrder().equals(CodeConstant.SCANNER_READ)) {
                        // 扫码结果
                        handleResult();
                    }
                }
            };
        }

        mBarcodeManager.addListener(listener);
        // 扫码速度 补光灯
        boolean velocity = SharedP.getKeyBoolean(this, CodeConstant.SP_VELOCITY);
        boolean light = SharedP.getKeyBoolean(this, CodeConstant.SP_LIGHT);
        if (velocity) {
            timeScan = 700;
        } else {
            timeScan = 1000;
        }
        // 初始化 聚光，补光
        PDAUtil.initBarcodeConfig(mBarcodeConfig, light);
    }

    private void handleResult() {
        String barcode = mBarcodeManager.getBarcode();
        if (barcode != null) {
            int size = barcode.length();
            Log.d("TAG", "---->>heww handleResult() barcode =" + barcode + ",size=" + size);
            // 【开始解析扫码结果 00】
            presenter.decodeUrlCode(barcode);
        } else {
            ToastUtil.showError(TipString.scanError);
        }
    }

    @Override
    protected void initTwoView() {
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPackageCode) {
                    numberCode.setText("0");
                    qrCodeInfos.clear();
//                    repeatCodeList.clear();
                    adapter.notifyDataSetChanged();
                }
                tvBox.setText("");
                deleteView.setVisibility(View.GONE);
            }
        });

        companyView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    companyView.clearFocus();
                    DialogUtil.hideInputWindow(v.getContext(), v);
                }
                return false;
            }
        });

    }

    /**
     * 返回上级 Activity
     */
    private void returnActivity(final String type) {
        stopScaner();
        int size = qrCodeInfos.size();
        // 箱码
        final String boxString = tvBox.getText().toString().trim();
        // 企业箱号
        final String companyString = companyView.getText().toString().trim();
        if (boxString.isEmpty() || companyString.isEmpty()) {
            ToastUtil.showWarning(TipString.boxCodeNotNull);
            return;
        }
        if (size > specNumber) {
            ToastUtil.showWarning(TipString.boxCodeNotThanSpec);
            return;
        }

        //  规格大小
        if (size == specNumber) {
            gotoProductActivity(type, boxString, companyString);
            finish();
        } else {
            // 该产品规格为24，实际为10，是否继续绑定
            String tip = "该产品规格" + specNumber + ",实际为 " + size + " 是否继续绑定";
            DialogUtil.deleteItemDialog(this, TipString.tips, tip, new NormalAlertDialog.onNormalOnclickListener() {
                @Override
                public void onNormalClick(NormalAlertDialog dialog) {
                    gotoProductActivity(type, boxString, companyString);
                    dialog.dismiss();
                    finish();
                }
            });
        }
    }

    private void gotoProductActivity(String type, String boxString, String companyString) {
        Intent intent = new Intent();
        intent.putExtra(CodeConstant.BOX_CODE, boxString);
        intent.putExtra(CodeConstant.RESULT_TYPE, type);
        intent.putExtra(CodeConstant.SEND_POSITION, sendPosition);
        intent.putExtra("buApplyNo", buApplyNo);
        intent.putExtra("comBoxCode", companyString);
        SimpleCache.putQrCode(qrCodeInfos);
        setResult(CodeConstant.RESULT_CODE, intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 扫码按键
        if (keyCode == CodeConstant.KEY_CODE_223 || keyCode == CodeConstant.KEY_CODE_224) {
            companyView.clearFocus();
            // 在扫码中
            if (isScanIng) {
                stopScaner();
            } else {
                handleStartScaner();
            }
        }
        isScanIng = !isScanIng;

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 继续扫码
     */
    private void handleStartScaner() {
//        mBarcodeManager.startScanner();
        subscribe = Observable.interval(timeScan, TimeUnit.MILLISECONDS, Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i("TAG", "accept aLong = " + aLong);
                        mBarcodeManager.startScanner();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showError(throwable.getMessage());
                    }
                });
    }

    /**
     * 停止扫码
     */
    private void stopScaner() {
        if (subscribe != null) {
            subscribe.dispose();
        }
        mBarcodeManager.stopScanner();
    }

    /**
     * 解码成功 【 01 】
     *
     * @param result 解码结果
     */
    @Override
    public void decodeCode(DecodeCode.ResultBean result) {

        final String code = result.getCode();
        String applyNo = result.getApplyNo();
        final String qrCodeType = result.getQrCodeType();
        // 套码编号
        buApplyNo = result.getBuApplyNo();
        boolean currentPackage = buApplyNo != null;
        // 箱码 A0702  产品码 A0701
        final String codeClass = qrCodeType.equals("2") ? CodeConstant.QR_CODE_0702 : CodeConstant.QR_CODE_0701;

        //int size = repeatCodeList.size();
        int size = qrCodeInfos.size();

        // 有数据的时候判断套码 和 卷码
        if (size != 0) {
            if (isPackageCode != currentPackage) {
                // 码类型不一致提示( 套码和卷码更换 )
                typeSwitch = true;
            }
        }
        isPackage(currentPackage, code, qrCodeType, codeClass);

    }

    /**
     * 是否是 套码【 01 】
     *
     * @param currentPackage 是否套码
     * @param code           当前码号
     * @param qrCodeType     2 箱码/ 1 产品码 ( 齐超给 )
     * @param codeClass      箱码 A0702  产品吗 A0701 ( 少杰用 )
     */
    private void isPackage(boolean currentPackage, String code, String qrCodeType, String codeClass) {
        isPackageCode = currentPackage;

        if (!currentPackage) {

            isContainsCode(code, codeClass);

        } else {
            // 套码
            if (qrCodeType.equals("2")) {
                isContainsCode(code, codeClass);
            } else {
                ToastUtil.showWarning(TipString.gotoScanBoxCode);
            }
        }
    }


    /**
     * 检查是否已扫码 code 【 02 】
     *
     * @param code 码号
     */
    private void isContainsCode(String code, String codeClass) {

        // 扫过的所有码 校验是否有重复码
        boolean repeatCode = RepeatCode.isRepeatCode(code, allCode);

        if (repeatCode) {
            ToastUtil.showWarning(TipString.repeatCode);
        } else {

            httpJudegCode(code, codeClass);
//            int size = qrCodeInfos.size();
//            if (size == 0) {
//                //第一次
//              httpJudegCode(code, codeClass);
//            } else {
//                httpJudegCode(code, codeClass);
//            }
        }

    }


    /**
     * 入库校验 【 03 】
     */
    private void httpJudegCode(String code, String codeClass) {

        presenter.judegCode(companyNo, code, codeClass);

        // boxCode 套码
        currentCode = code;
        currentCodeClass = codeClass;

    }


    /**
     * 入库码校验 【 04 】
     *
     * @param response response
     */
    @Override
    public void produceJudegCode(String response) {

        if (isPackageCode) {
            // 拉取套码 下的产品码
            presenter.getQrCodeList(currentCode, CodeConstant.QR_CODE_0702);
        } else {

            QrCodeListData qrCode = new QrCodeListData();
            qrCode.setQrCode(currentCode);
            qrCode.setQrCodeClass(currentCodeClass);

            if (typeSwitch) {
                // 更换了码类型
                deleteSwitchDialog(qrCode);

            } else {
                if (currentCodeClass.equals(CodeConstant.QR_CODE_0702)) {
                    //箱码
                    tvBox.setText(currentCode);
                    deleteView.setVisibility(View.VISIBLE);
                } else{
                    // 产品码
                    qrCodeInfos.add(qrCode);
//                    repeatCodeList.add(currentCode);
                    List<QrCodeListData> listData = RemoveDupData.removeDupQrCode(qrCodeInfos);
                    qrCodeInfos.clear();
                    qrCodeInfos.addAll(listData);

                    int size = qrCodeInfos.size();
                    numberCode.setText(String.valueOf(size));
                    adapter.notifyItemInserted(size);
                    adapter.notifyItemRangeChanged(size, size);
                }
            }
        }

    }

    /**
     * 切换了码类型 套码 -> 切换成卷码
     */
    private void deleteSwitchDialog(final QrCodeListData qrCode) {
        DialogUtil.deleteItemDialog(this, TipString.tips, TipString.resetData, new NormalAlertDialog.onNormalOnclickListener() {
            @Override
            public void onNormalClick(NormalAlertDialog dialog) {
                // 更换了码类型
                qrCodeInfos.clear();
//                repeatCodeList.clear();
                if (Objects.equals(qrCode.getQrCodeClass(), CodeConstant.QR_CODE_0702)) {
                    //箱码
                    tvBox.setText(qrCode.getQrCode());
                    deleteView.setVisibility(View.VISIBLE);
                } else {
                    //产品码
                    qrCodeInfos.add(qrCode);
                    List<QrCodeListData> listData = RemoveDupData.removeDupQrCode(qrCodeInfos);
                    qrCodeInfos.clear();
                    qrCodeInfos.addAll(listData);

//                    repeatCodeList.add(currentCode);
                    numberCode.setText(String.valueOf(qrCodeInfos.size()));
                    adapter.notifyDataSetChanged();
                }

                dialog.dismiss();
            }
        });
    }

    /**
     * 出库码校验
     *
     * @param response response
     */
    @Override
    public void sendJudegCode(String response) {
        // 校验成功

    }

    /**
     * 根据套码获取产品码
     *
     * @param codeList 产品码列表
     */
    @Override
    public void getCodeList(ArrayList<String> codeList) {

        qrCodeInfos.clear();
//        repeatCodeList.clear();
//        ArrayList<String> data = codeList;
        int qrCodeSize = codeList.size();
        if (qrCodeSize != specNumber) {
            ToastUtil.showWarning(TipString.scanCompleteSpecCode);
        } else {
            for (int i = 0; i < qrCodeSize; i++) {
                QrCodeListData qrCodeList = new QrCodeListData();
                qrCodeList.setQrCodeClass(CodeConstant.QR_CODE_0701);
                qrCodeList.setQrCode(codeList.get(i));
                qrCodeInfos.add(qrCodeList);
                // repeatCodeList.add(codeList.get(i));
            }
            tvBox.setText(currentCode);
            deleteView.setVisibility(View.VISIBLE);

            numberCode.setText(String.valueOf(qrCodeSize));
            adapter.notifyDataSetChanged();
            // 停止扫码
            stopScaner();
        }

    }

    @Override
    public void showError(String msg) {
        ToastUtil.showError(msg);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (subscribe != null) {
            subscribe.dispose();
        }
        mBarcodeManager.dismiss();
        mBarcodeManager.removeListener(listener);
    }
}
