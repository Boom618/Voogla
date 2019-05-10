package com.ty.voogla.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.ty.voogla.R;
import com.ty.voogla.adapter.LayoutInit;
import com.ty.voogla.adapter.SendOutNextAdapter;
import com.ty.voogla.base.BaseActivity;
import com.ty.voogla.base.ResponseInfo;
import com.ty.voogla.bean.SendStorageData;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.bean.sendout.AddSendOutData;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.bean.sendout.SendOutListInfo;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.constant.TipString;
import com.ty.voogla.data.DateUtil;
import com.ty.voogla.data.SimpleCache;
import com.ty.voogla.data.SparseArrayUtil;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;
import com.ty.voogla.net.RequestBodyJson;
import com.ty.voogla.util.ToastUtil;
import com.ty.voogla.widght.DialogUtil;
import com.ty.voogla.widght.NormalAlertDialog;
import okhttp3.RequestBody;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author TY on 2019/1/27.
 */
public class SendOutNextActivity2 extends BaseActivity implements VooglaContract.View<SendOutListInfo> {


    // 发货单号
    // 企业编号
    private String deliveryNo;
    private String companyNo;

    // 地址信息
    private String provinceLevel;
    private String cityLevel;
    private String countyLevel;
    private String deliveryAddress;

    // http list 数据
    private List<SendOutListInfo.DeliveryDetailInfosBean> deliveryList;

    /**
     * item 箱码
     */
    private HashMap<Integer, ArrayList<QrCodeListData>> hashMapCode = new HashMap<>();
    private VooglaPresenter presenter = new VooglaPresenter(this);

    private RecyclerView recyclerView;
    private SendOutNextAdapter adapter;

    private SparseArray<Integer> boxSize = new SparseArray<>();
    private SparseArray<Integer> proSize = new SparseArray<>();

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_send_out_next;
    }

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {
        // 首次进来清空出库数据
        SimpleCache.Companion.clearKey("qrCode");
    }

    @Override
    protected void initOneData() {

        recyclerView = findViewById(R.id.recycler_view_send_next);
        initToolBar(R.string.send_out_detail, "保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtil.leftRightDialog(SendOutNextActivity2.this, TipString.tips, TipString.ConfirmDelivery, new NormalAlertDialog.onNormalOnclickListener() {
                    @Override
                    public void onNormalClick(NormalAlertDialog dialog) {
                        sendOutSave(initReqBody());
                        dialog.dismiss();
                    }
                }, false);
            }
        });
        ImageView back = findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backIntercept();
            }
        });

        companyNo = SimpleCache.Companion.getUserInfo().getCompanyNo();
        // deliveryNo 发货单号
        deliveryNo = getIntent().getStringExtra(CodeConstant.DELIVERY_NO);

        presenter.getSendOutListInfo(companyNo, deliveryNo);
    }

    private void sendOutSave(RequestBody body) {
        if (body == null) {
            return;
        }
        presenter.addSendOut(body);
    }

    @Override
    protected void initTwoView() {

    }

    @Override
    public void showSuccess(SendOutListInfo data) {


        SendOutListInfo.GoodsDeliveryInfoBean info = data.getGoodsDeliveryInfo();
        deliveryList = data.getDeliveryDetailInfos();

        provinceLevel = info.getProvinceLevel();
        cityLevel = info.getCityLevel();
        countyLevel = info.getCountyLevel();
        deliveryAddress = info.getDeliveryAddress();

        LayoutInit.initLayoutManager(this, recyclerView);
        adapter = new SendOutNextAdapter(this, R.layout.item_send_out_next, deliveryList);
        recyclerView.setAdapter(adapter);

        // 有暂存数据
        // 上次暂存的数据
        SendStorageData storageData = null;
        boolean storage = false;
        try {
            storageData = SimpleCache.Companion.sendOutNumb(deliveryNo);
            storage = storageData.isStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 设置了暂存
        if (storage) {
            SparseArray<Integer> boxSizeList = storageData.getBoxSizeList();
            SparseArray<Integer> proSizeList = storageData.getProSizeList();
            HashMap<Integer, ArrayList<QrCodeListData>> hashMap = storageData.getHashMap();

            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View child = recyclerView.getChildAt(i);
                TextView box = child.findViewById(R.id.tv_box_amount);
                TextView pro = child.findViewById(R.id.tv_product_amount);

                box.setText(boxSizeList.get(i) + "箱");
                pro.setText(proSizeList.get(i) + "盒");
            }
            hashMapCode = hashMap;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CodeConstant.REQUEST_CODE_OUT && resultCode == CodeConstant.RESULT_CODE) {
            // 重置数量
            int pSize = 0;
            int bSize = 0;

            int position = data.getIntExtra(CodeConstant.SEND_POSITION, -1);
            hashMapCode = SparseArrayUtil.getQrCodeSend(this);

            ArrayList<QrCodeListData> listData = hashMapCode.get(position);

            for (int i = 0; i < listData.size(); i++) {
                if (listData.get(i).getQrCodeClass().equals(CodeConstant.QR_CODE_0701)) {
                    pSize++;
                } else {
                    bSize++;
                }
            }
            boxSize.put(position, bSize);
            proSize.put(position, pSize);

            View child = recyclerView.getChildAt(position);
            TextView box = child.findViewById(R.id.tv_box_amount);
            TextView pro = child.findViewById(R.id.tv_product_amount);

            box.setText(bSize + "箱");
            pro.setText(pSize + "盒");
        }

    }

    /**
     * 构建 Body
     *
     * @return RequestBody
     */
    private RequestBody initReqBody() {

        UserInfo userInfo = SimpleCache.Companion.getUserInfo();
        AddSendOutData send = new AddSendOutData();
        AddSendOutData.WareInfoBean info = new AddSendOutData.WareInfoBean();

        ArrayList<AddSendOutData.OutQrCodeDetailInfosBean> list = new ArrayList<>();

        int size = deliveryList.size();

        for (int i = 0; i < size; i++) {
            // 商品列
            AddSendOutData.OutQrCodeDetailInfosBean bean = new AddSendOutData.OutQrCodeDetailInfosBean();
            bean.setGoodsNo(deliveryList.get(i).getGoodsNo());
            bean.setOutBoxNum(boxSize.get(i) + "");
            bean.setOutGoodsNum(proSize.get(i) + "");
            bean.setUnit(deliveryList.get(i).getUnit());
            // 箱码集合
            ArrayList<QrCodeListData> listData = hashMapCode.get(i);
            bean.setQrCodeInfos(listData);
            if (listData != null) {
                list.add(bean);
            }
        }

        if (list.size() == 0) {
            ToastUtil.showToast(TipString.scanPlease);
            return null;
        }

        String time = DateUtil.getTime(new Date());

        info.setCompanyNo(companyNo);
        info.setCreator(userInfo.getUserNo());
        info.setDeliveryNo(deliveryNo);
        info.setProvinceLevel(provinceLevel);
        info.setCityLevel(cityLevel);
        info.setCountyLevel(countyLevel);
        info.setDeliveryAddress(deliveryAddress);
        info.setOutTime(time);

        send.setOutWareInfo(info);
        send.setOutQrCodeDetailInfos(list);

        String json = new Gson().toJson(send);

        return RequestBodyJson.requestBody(json);

    }

    @Override
    public void showError(String msg) {
        ToastUtil.showToast(msg);

    }

    @Override
    public void showResponse(ResponseInfo response) {
        ToastUtil.showToast(response.getMsg());
        finish();

    }

    /**
     * 监听拦截 back 键 TODO == 保存扫码
     */
    @Override
    public void onBackPressed() {

        backIntercept();
    }

    /**
     * back  拦截
     */
    private void backIntercept() {

        if (boxSize.size() == 0 && proSize.size() == 0) {
            finish();
            return;
        }

        DialogUtil.leftRightDialog(this, TipString.tips, TipString.saveData, new NormalAlertDialog.onNormalOnclickListener() {
            @Override
            public void onNormalClick(NormalAlertDialog dialog) {
                dialog.dismiss();
                finish();
                // 暂时数据
                storage();
            }
        }, true);
    }

    /**
     * 暂存数据
     */
    private void storage() {

        SendStorageData data = new SendStorageData();
        data.setStorage(true);
        data.setBoxSizeList(boxSize);
        data.setProSizeList(proSize);
        data.setHashMap(hashMapCode);

        SimpleCache.Companion.putSendOutNumb(deliveryNo, data);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SparseArrayUtil.clearCode(this);
        SparseArrayUtil.clearOwnProCode(this);
    }
}
