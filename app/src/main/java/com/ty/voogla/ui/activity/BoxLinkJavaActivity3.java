package com.ty.voogla.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.data.SharedP;
import com.ty.voogla.data.SimpleCache;
import com.ty.voogla.data.SparseArrayUtil;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;
import com.ty.voogla.util.ToastUtil;
import com.ty.voogla.util.scan.PDAUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author TY on 2019/1/12.
 * <p>
 * 箱码绑定 --> 扫码(出库)
 */
public class BoxLinkJavaActivity3 extends BaseActivity implements VooglaContract.BoxLinkView {

    // SDK 相关
    private BarcodeManager mBarcodeManager;
    private BarcodeConfig mBarcodeConfig;
    private BarcodeListener listener;

    private RecyclerView boxRecycler;
    private boolean triggerState = false;

    private BoxLinkAdapter adapter;
    /**
     * 产品码列表(全局展示类 ： QrCodeListData )
     */
    private ArrayList<QrCodeListData> qrCodeInfos = new ArrayList<>();
    /**
     * 当前出库的所有码
     */
    private HashMap<Integer, ArrayList<QrCodeListData>> allCode = new HashMap<>();

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
     * 二维码、类别
     */
    private String lastCode = "";
    private String lastCodeClass = "";
    /**
     * 重复码
     */
    private ArrayList<String> repeatCodeList = new ArrayList<>();

    /**
     * support.v4.util.ArrayMap ( 兼容 aip19 以下)
     * ArrayMap 没有 Serializable ，不能采用文件保存 改用 HashMap
     * 保存所有 产品码 及对应的箱码
     */
    private HashMap<String, String> ownProCode = new HashMap();
    /**
     * 产品码转箱码
     */
    private String pro2BoxCode;


    private VooglaPresenter presenter = new VooglaPresenter(this);
    private Disposable subscribe;
    // 检测是否在扫码中 默认 false
    private boolean isScanIng = false;

    private int timeScan = 1000;
    /**
     * 箱码数量显示
     */
    private TextView numberCode;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_box_link_code;
    }

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {

        companyNo = SimpleCache.getUserInfo().getCompanyNo();

        numberCode = findViewById(R.id.tv_code_number);

        sendPosition = getIntent().getIntExtra(CodeConstant.SEND_POSITION, -1);
        goodsNo = getIntent().getStringExtra("goodsNo");

        try {
            allCode = SparseArrayUtil.getQrCodeSend(this);
            ownProCode = SparseArrayUtil.getOwnProCode(this);
            qrCodeInfos = allCode.get(sendPosition);
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
                    ToastUtil.showWarning("码数量不能为空");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(CodeConstant.SEND_POSITION, sendPosition);

                allCode.put(sendPosition, qrCodeInfos);
                // 保存出库中所有码（不能互查）
                SparseArrayUtil.putQrCodeSend(BoxLinkJavaActivity3.this, allCode);
                // 保存所有产品码及对应的箱码
                SparseArrayUtil.putOwnProCode(BoxLinkJavaActivity3.this, ownProCode);

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

                        QrCodeListData data = qrCodeInfos.get(position);
                        String qrCodeClass = data.getQrCodeClass();
                        if (CodeConstant.QR_CODE_0701.equals(qrCodeClass)) {
                            String qrCode = data.getQrCode();
                            ownProCode.remove(qrCode);
                        }
                        numberCode.setText(String.valueOf(qrCodeInfos.size() - 1));

                        qrCodeInfos.remove(position);
                        // 重复码集合
                        repeatCodeList.remove(position);
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
    }

    @Override
    protected void initTwoView() {
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
            presenter.decodeUrlCode(barcode);
        } else {
            ToastUtil.showError("扫码失败");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 扫码按键
        if (keyCode == CodeConstant.KEY_CODE_223 || keyCode == CodeConstant.KEY_CODE_224) {
            // 在扫码中
            if (isScanIng) {
                stopScaner();
            } else {
                handleStartScaner();
            }
        }
        isScanIng = !isScanIng;

        return true;
    }

    /**
     * 继续扫码
     */
    private void handleStartScaner() {
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
     * 解码成功【01】
     *
     * @param result
     */
    @Override
    public void decodeCode(DecodeCode.ResultBean result) {

        final String code = result.getCode();
        String qrCodeType = result.getQrCodeType();
        // 套码编号
        // 箱码 A0702  产品吗 A0701
        final String codeClass = qrCodeType.equals("2") ? CodeConstant.QR_CODE_0702 : CodeConstant.QR_CODE_0701;

        isContainsCode(code, codeClass);

    }

    /**
     * 检查是否已扫码 code【02】
     *
     * @param code
     */
    private void isContainsCode(String code, String codeClass) {

        // 方式一：使用 keySet()遍历 Map ( 10 W 数据 31 ms )
        /*for (Integer key : allCode.keySet()) {
            ArrayList<QrCodeListData> data = allCode.get(key);
            for (int j = 0; j < data.size(); j++) {
                String qrCode = data.get(j).getQrCode();
                if (qrCode.equals(code)) {
                    ToastUtil.showToast("重复码请重试");
                    return;
                }
            }
        }*/

        // 方式二：迭代器，keySet 迭代 （10 W 数据 17 ms）
        Iterator<Integer> iterator = allCode.keySet().iterator();
        Integer key;
        while (iterator.hasNext()) {
            key = iterator.next();
            ArrayList<QrCodeListData> listData = allCode.get(key);
            for (int i = 0; i < listData.size(); i++) {
                String qrCode = listData.get(i).getQrCode();
                if (qrCode.equals(code)) {
                    ToastUtil.showWarning("重复码请重试");
                    return;
                }
            }
        }

        if (repeatCodeList.contains(code)) {
            ToastUtil.showWarning("重复码请重试");
            //ScanSoundUtil.showSound(getApplicationContext(), R.raw.scan_already);
        } else {
            if (CodeConstant.QR_CODE_0701.equals(codeClass)) {
                // 产品码
                presenter.getQrCodeList(code, CodeConstant.QR_CODE_0701);
                lastCode = code;
            } else {
                // 箱码,去比较所有产品码对应的箱码
                Iterator<String> iteratorPro = ownProCode.keySet().iterator();
                String keyPro;
                while (iteratorPro.hasNext()) {
                    keyPro = iteratorPro.next();
                    String boxCode = ownProCode.get(keyPro);
                    if (code.equals(boxCode)) {
                        ToastUtil.showWarning("该箱码中有对应的产品码已出库");
                        return;
                    }
                }
                // 出库校验
                sendOutjudegCode(companyNo, codeClass, goodsNo, code);
            }
        }
    }

    /**
     * 出库校验【03】
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
     * 出库码校验【04】
     *
     * @param response
     */
    @Override
    public void sendJudegCode(String response) {

        repeatCodeList.add(lastCode);
        ownProCode.put(lastCode, pro2BoxCode);
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
     * 产品码去判断箱码，箱码根据保存的 ownProCode 去遍历
     * 根据产品码获取箱码号(单个)
     *
     * @param codeList
     */
    @Override
    public void getCodeList(ArrayList<String> codeList) {
        pro2BoxCode = codeList.get(0);
        if (repeatCodeList.contains(pro2BoxCode)) {
            ToastUtil.showWarning("该产品码所关联箱码已在列表中");
            return;
        }
        sendOutjudegCode(companyNo, CodeConstant.QR_CODE_0701, goodsNo, lastCode);

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
