package com.ty.voogla.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import com.ty.voogla.bean.sendout.CacheAddress;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.bean.sendout.SendOutListInfo;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.constant.TipString;
import com.ty.voogla.data.*;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;
import com.ty.voogla.net.RequestBodyJson;
import com.ty.voogla.util.FullDialog;
import com.ty.voogla.util.ToastUtil;
import com.ty.voogla.widght.DialogUtil;
import com.ty.voogla.widght.LoadingDialog;
import com.ty.voogla.widght.NormalAlertDialog;
import okhttp3.RequestBody;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
    // 缓存
    private String goodsNo; // SP20190513034347
    private String unit;
    private String goodsName;
    private String unitNum;
    private String deliveryNum;

    // http list 数据
    private List<SendOutListInfo.DeliveryDetailInfosBean> deliveryList;


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
        initToolBar(R.string.send_out_detail, TipString.sendOut, new View.OnClickListener() {
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
        CacheAddress addr = new CacheAddress();
        try {
            outCache = SparseArrayUtil.getQrCodeSend(this, deliveryNo);
            addr = SimpleCache.Companion.getAddr(deliveryNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (outCache == null || outCache.size() == 0) {
            presenter.getSendOutListInfo(companyNo, deliveryNo);
        } else {
            // 有暂存数据
            deliveryList = outCache;
            provinceLevel = addr.getProvinceLevel();
            cityLevel = addr.getCityLevel();
            countyLevel = addr.getCountyLevel();
            deliveryAddress = addr.getDeliveryAddress();

            goodsNo = addr.getGoodsNo();
            unit = addr.getUnit();

            LayoutInit.initLayoutManager(this, recyclerView);
            adapter = new SendOutNextAdapter(this, R.layout.item_send_out_next, deliveryList);
            recyclerView.setAdapter(adapter);
            // TODO SB 了,清除了 ，在扫码页面中不能取值
            //SparseArrayUtil.clearCode(this, deliveryNo);
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

        goodsNo = deliveryList.get(0).getGoodsNo();
        unit = deliveryList.get(0).getUnit();
        goodsName = deliveryList.get(0).getGoodsName();
        deliveryNum = deliveryList.get(0).getDeliveryNum();
        unitNum = deliveryList.get(0).getUnitNum();

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
            SendOutListInfo.DeliveryDetailInfosBean infosBean = new SendOutListInfo.DeliveryDetailInfosBean();
            List<QrCodeListData> listCode = new ArrayList<>();
            try {
                deliveryList = SparseArrayUtil.getQrCodeSend(this, deliveryNo);
                infosBean = deliveryList.get(position);
                listCode = infosBean.getListCode();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < listCode.size(); i++) {
                if (listCode.get(i).getQrCodeClass().equals(CodeConstant.QR_CODE_0701)) {
                    pSize++;
                } else {
                    bSize++;
                }
            }
            infosBean.setUnit(unit);
            infosBean.setGoodsNo(goodsNo);
            infosBean.setOutBoxNum(bSize);
            infosBean.setOutGoodsNum(pSize);
            infosBean.setGoodsName(goodsName);
            infosBean.setDeliveryNum(deliveryNum);
            infosBean.setUnitNum(unitNum);
            infosBean.setListCode(listCode);

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
            bean.setGoodsNo(goodsNo);
            bean.setOutBoxNum(deliveryList.get(i).getOutBoxNum() + "");
            bean.setOutGoodsNum(deliveryList.get(i).getOutGoodsNum() + "");
            bean.setUnit(unit);
            List<QrCodeListData> listData = deliveryList.get(i).getListCode();
            bean.setQrCodeInfos(listData);
            if (listData != null) {
                list.add(bean);
            }
        }

        if (list.size() == 0) {
            ToastUtil.showWarning(TipString.scanPlease);
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
        ToastUtil.showError(msg);

    }

    @Override
    public void showResponse(ResponseInfo response) {
        ToastUtil.showSuccess(response.getMsg());
        SparseArrayUtil.clearCode(this, deliveryNo);
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

        new NormalAlertDialog.Builder(this)
                .setTitleVisible(true)
                .setTitleText(TipString.tips)
                .setRightButtonText("保存")
                .setLeftButtonText("不保存")
                .setContentText(TipString.saveData)
                .setRightListener(new NormalAlertDialog.onNormalOnclickListener() {
                    @Override
                    public void onNormalClick(NormalAlertDialog dialog) {
                        dialog.dismiss();
                        finish();
                        // 暂时数据
                        storage();
                    }
                })
                .setLeftListener(new NormalAlertDialog.onNormalOnclickListener() {
                    @Override
                    public void onNormalClick(NormalAlertDialog dialog) {
                        dialog.dismiss();
                        // 清除缓存数据
                        SparseArrayUtil.clearCode(SendOutNextActivity2.this, deliveryNo);
                        Set<String> set = SparseArrayUtil.getDeliveryNo();
                        set.remove(deliveryNo);
                        SparseArrayUtil.putDeliveryNo(set);
                        finish();
                    }
                })
                .build()
                .show();
    }

    /**
     * 暂存数据
     */
    private void storage() {
        // 暂存码列表
        SparseArrayUtil.putQrCodeSend(this, deliveryNo, deliveryList);
        // 暂存数据 可以继续发货 状态
        Set<String> set = SparseArrayUtil.getDeliveryNo();
        set.add(deliveryNo);
        SparseArrayUtil.putDeliveryNo(set);
        // 缓存地址
        CacheAddress data = new CacheAddress();
        data.setCityLevel(cityLevel);
        data.setCountyLevel(countyLevel);
        data.setProvinceLevel(provinceLevel);
        data.setDeliveryAddress(deliveryAddress);

        data.setGoodsNo(goodsNo);
        data.setUnit(unit);
        SimpleCache.Companion.putAddr(this.deliveryNo, data);

    }

    private LoadingDialog dialog;

    @Override
    public void showLoading() {
        dialog = FullDialog.showLoading(this, TipString.loading);
    }

    @Override
    public void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedP.clearKeyString(this);
        SparseArrayUtil.clearOwnProCode(this);
    }
}
