package com.ty.voogla.mvp.presenter;

import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.bean.produce.DecodeCode;
import com.ty.voogla.bean.produce.ProductIntoData;
import com.ty.voogla.bean.produce.ProductListInfoData;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.bean.sendout.SendOutListData;
import com.ty.voogla.constant.ApiNameConstant;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.net.HttpMethods;
import com.ty.voogla.util.SimpleCache;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * @author TY on 2018/12/20.
 */
public class VooglaPresenter implements VooglaContract.Presenter {

    private HttpMethods httpMethods;

    private VooglaContract.View iView;

    private VooglaContract.ListView iListView;

    private Disposable disposable;

    public VooglaPresenter(VooglaContract.View view) {
        httpMethods = HttpMethods.getInstance();
        this.iView = view;
    }

    public VooglaPresenter(VooglaContract.ListView view) {
        httpMethods = HttpMethods.getInstance();
        this.iListView = view;
    }

    public void disposable() {
        if (disposable != null) {
            disposable.dispose();
        }
    }


    @Override
    public void getData(String username, String password) {

        httpMethods.userLogin(new SingleObserver<BaseResponse<UserInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<UserInfo> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    UserInfo userInfo = response.getData();
                    SimpleCache.putString(CodeConstant.SESSION_ID_KEY, userInfo.getSessionID());
                    SimpleCache.putUserInfo(userInfo);
                    iView.showSuccess(userInfo);
                } else {
                    iView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage());
            }
        }, username, password);
    }

    /**
     * 获取生产入库列表
     */
    public void getProduceList(String companyNo) {
        httpMethods.getProductList(new SingleObserver<BaseResponse<ProductIntoData>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(BaseResponse<ProductIntoData> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    iView.showSuccess(response.getData());
                } else {
                    iView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage());
            }
        }, companyNo);
    }

    /**
     * 获取产品列表信息
     *
     * @param companyNo
     */
    public void getProductListInfo(String companyNo) {
        httpMethods.getProductListInfo(new SingleObserver<BaseResponse<ProductListInfoData>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<ProductListInfoData> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    iView.showSuccess(response.getData());
                } else {
                    iView.showError(response.getMsg());
                }


            }

            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage());
            }
        }, companyNo);
    }

    /**
     * 二维码解码
     */
    public void decodeUrlCode(String secret) {

        // 齐超 地址
        HttpMethods http = new HttpMethods(ApiNameConstant.BASE_URL3);

        http.decodeUrlCode(new SingleObserver<DecodeCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(DecodeCode decodeCode) {
                if (CodeConstant.SERVICE_STATUS == decodeCode.getStatus()) {
                    iView.showSuccess(decodeCode);
                } else {
                    iView.showError(decodeCode.getMsg());
                }

            }

            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage());
            }
        }, secret);

    }

    /**
     * 二维码解码  flatMap 操作
     */
    public void decodeUrlCodeMap(SingleObserver<DecodeCode> observer, String secret) {

        // 齐超 地址
        HttpMethods http = new HttpMethods(ApiNameConstant.BASE_URL3);


    }

    /** ------------------------------------  发货出库 ------------------------------------*/

    /**
     * 获取发货单信息
     *
     * @param companyNo
     */
    public void getSendOutList(String companyNo) {
        httpMethods.getSendOutList(new SingleObserver<BaseResponse<SendOutListData>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<SendOutListData> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    SendOutListData data = response.getData();

                    iListView.showSuccess(data.getList());
                } else {
                    iListView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                iListView.showError(e.getMessage());

            }
        }, companyNo);
    }

}
