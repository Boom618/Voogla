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
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.bean.sendout.AddSendOutData;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.bean.sendout.SendOutListInfo;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.constant.TipString;
import com.ty.voogla.data.*;
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
        initToolBar(R.string.send_out_detail, TipString.save, new View.OnClickListener() {
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
        SharedP.putKeyString(this, deliveryNo);

        List<SendOutListInfo.DeliveryDetailInfosBean> outCache = null;
        try {
            outCache = LibraryCache.getOutCache(this, getCacheDir().getName(), deliveryNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (outCache == null || outCache.size() == 0) {
            presenter.getSendOutListInfo(companyNo, deliveryNo);
        } else {
            // 有暂存数据
            deliveryList = outCache;
            LayoutInit.initLayoutManager(this, recyclerView);
            adapter = new SendOutNextAdapter(this, R.layout.item_send_out_next, deliveryList);
            recyclerView.setAdapter(adapter);
            LibraryCache.clearCache(this, deliveryNo);
        }
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CodeConstant.REQUEST_CODE_OUT && resultCode == CodeConstant.RESULT_CODE) {
            // 重置数量
            int pSize = 0;
            int bSize = 0;

            int position = data.getIntExtra(CodeConstant.SEND_POSITION, -1);
//            hashMapCode = SparseArrayUtil.getQrCodeSend(this);
            List<SendOutListInfo.DeliveryDetailInfosBean> outCache = LibraryCache.getOutCache(this, "", deliveryNo);
            SendOutListInfo.DeliveryDetailInfosBean infosBean = outCache.get(position);
            List<QrCodeListData> listCode = infosBean.getListCode();

//            ArrayList<QrCodeListData> listData = hashMapCode.get(position);

            for (int i = 0; i < listCode.size(); i++) {
                if (listCode.get(i).getQrCodeClass().equals(CodeConstant.QR_CODE_0701)) {
                    pSize++;
                } else {
                    bSize++;
                }
            }
            infosBean.setOutBoxNum(bSize);
            infosBean.setOutGoodsNum(pSize);

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
            bean.setOutBoxNum(deliveryList.get(i).getOutBoxNum() + "");
            bean.setOutGoodsNum(deliveryList.get(i).getOutGoodsNum() + "");
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

        for (int i = 0; i < deliveryList.size(); i++) {
            List<QrCodeListData> listCode = deliveryList.get(i).getListCode();
            if (listCode.size() == 0) {
                finish();
                return;
            }
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
        String path = getCacheDir().getName();
        LibraryCache.SendOutCache(this, path, deliveryNo, deliveryList);

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
        SharedP.clearKeyString(this);
        SparseArrayUtil.clearOwnProCode(this);
    }
}
