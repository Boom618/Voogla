package com.ty.voogla.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
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

    // item 箱码
    private HashMap<Integer, ArrayList<QrCodeListData>> hashMapCode;
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
        SimpleCache.clearKey("qrCode");
    }

    @Override
    protected void initOneData() {

        recyclerView = findViewById(R.id.recycler_view_send_next);
        initToolBar(R.string.send_out_detail, "保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtil.deleteItemDialog(v.getContext(), "温馨提示", "确认发货", new NormalAlertDialog.onNormalOnclickListener() {
                    @Override
                    public void onNormalClick(NormalAlertDialog dialog) {
                        sendOutSave(initReqBody());
                        dialog.dismiss();
                    }
                });
            }
        });

        companyNo = SimpleCache.getUserInfo().getCompanyNo();
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
                if (listData.get(i).getQrCodeClass().equals("A0701")) {
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
     * @return
     */
    private RequestBody initReqBody() {

        UserInfo userInfo = SimpleCache.getUserInfo();
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
            ToastUtil.showToast("请先扫码");
            return null ;
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
        ToastUtil.showToast("成功");
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SparseArrayUtil.clearCode(this);
        SparseArrayUtil.clearOwnProCode(this);
    }
}
