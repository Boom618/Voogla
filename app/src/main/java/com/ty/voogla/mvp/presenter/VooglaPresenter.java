package com.ty.voogla.mvp.presenter;

import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.base.ResponseInfo;
import com.ty.voogla.bean.CheckInfoList;
import com.ty.voogla.bean.produce.*;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.bean.sendout.OutPutInfoData;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.bean.sendout.SendOutListData;
import com.ty.voogla.bean.sendout.SendOutListInfo;
import com.ty.voogla.constant.ApiNameConstant;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.constant.TipString;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.net.HttpMethods;
import com.ty.voogla.data.SimpleCache;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * @author TY on 2018/12/20.
 */
public class VooglaPresenter {

    private HttpMethods httpMethods;

    private VooglaContract.View iView;

    private VooglaContract.ListView iListView;

    private VooglaContract.BoxLinkView boxView;

    private Disposable disposable;

    public VooglaPresenter(VooglaContract.View view) {
        httpMethods = HttpMethods.getInstance();
        this.iView = view;
    }

    public VooglaPresenter(VooglaContract.ListView view) {
        httpMethods = HttpMethods.getInstance();
        this.iListView = view;
    }

    /**
     * 扫码页面单独接口（四个请求）
     *
     * @param boxView
     */
    public VooglaPresenter(VooglaContract.BoxLinkView boxView) {
        httpMethods = HttpMethods.getInstance();
        this.boxView = boxView;
    }

    public void disposable() {
        if (disposable != null) {
            disposable.dispose();
        }
    }


    public void login(String username, String password) {
        iView.showLoading();
        httpMethods.userLogin(new SingleObserver<BaseResponse<UserInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<UserInfo> response) {
                iView.hideLoading();
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    UserInfo userInfo = response.getData();
                    SimpleCache.Companion.putUserInfo(userInfo);
                    iView.showSuccess(userInfo);
                } else {
                    iView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                iView.hideLoading();
                iView.showError(e.getMessage());
            }
        }, username, password);
    }

    /**
     * 获取生产入库列表
     * batchNo : 搜索生产批次号
     */
    public void getProduceList(String companyNo, String batchNo) {
        httpMethods.getProductList(new SingleObserver<BaseResponse<ProductIntoData>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<ProductIntoData> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    iListView.showSuccess(response.getData().getList());
                } else {
                    iListView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                iListView.showError(e.getMessage());
            }
        }, companyNo, batchNo);
    }

    /**
     * 删除入库信息
     *
     * @param companyNo
     * @param inBatchNo
     */
    public void deleteProduct(String companyNo, String inBatchNo, String companyAttr) {

        httpMethods.deleteProduct(new SingleObserver<ResponseInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(ResponseInfo response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    iView.showSuccess(response);
                } else {
                    iView.showError(response.getMsg());
                }

            }

            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage());
            }
        }, companyNo, inBatchNo, companyAttr);
    }

    /**
     * 获取产品列表信息
     *
     * @param companyNo 编号
     * @param type 生产入库 or 出库
     */
    public void getProductListInfo(String companyNo, final String type) {
        httpMethods.getProductListInfo(new SingleObserver<BaseResponse<ProductListInfoData>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<ProductListInfoData> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    if (type.equals("proInto")) {
                        iView.showSuccess(response.getData());
                    }else{
                        // 用户可能手动添加了 产品，需要更新数据
                        SimpleCache.Companion.clearKey("product");
                        SimpleCache.Companion.putProductList(response.getData());
                    }
                } else {
                    iView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    iView.showError(TipString.outTimeRetry);
                }else{
                    iView.showError(e.getMessage());
                }
            }
        }, companyNo);
    }

    /**
     * 入库保存
     */
    public void addProduct(RequestBody body){
        iView.showLoading();
        httpMethods.addProduct(new SingleObserver<ResponseInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                iView.hideLoading();
                if (CodeConstant.SERVICE_SUCCESS.equals(responseInfo.getMsg())) {
                    // 入库成功（保存）
                    iView.showResponse(responseInfo);
                } else {
                    iView.showError(responseInfo.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                iView.hideLoading();
                iView.showError(e.getMessage());
            }
        }, body);

    }

    /**
     * 获取入库详情信息
     */
    public void getInputProductInfo(String companyNo, String inBatchNo) {

        httpMethods.getInputProductInfo(new SingleObserver<BaseResponse<ProductInputInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;

            }

            @Override
            public void onSuccess(BaseResponse<ProductInputInfo> response) {

                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    iView.showSuccess(response.getData());
                } else {
                    iView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                iView.showError("获取数据失败");

            }
        }, companyNo, inBatchNo);

    }

    /**
     * 二维码解码
     */
    public void decodeUrlCode(String encodeUrl) {

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
                    DecodeCode.ResultBean result = decodeCode.getResult();
                    if (boxView == null) {
                        iView.showSuccess(result);
                    } else {
                        boxView.decodeCode(result);
                    }
                } else {
                    if (boxView == null) {
                        iView.showError(decodeCode.getMsg());
                    } else {
                        boxView.showError(decodeCode.getMsg());
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                if (boxView == null) {
                    iView.showError(e.getMessage());
                } else {
                    boxView.showError(e.getMessage());
                }
            }
        }, encodeUrl);

    }

    /**
     * 入库校验
     *
     * @param companyNo
     * @param qrCode
     */
    public void judegCode(String companyNo, String qrCode, String qrCodeClass) {
        httpMethods.judegCode(new SingleObserver<BaseResponse<QrCodeJudge>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<QrCodeJudge> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {

                    boxView.produceJudegCode(response.getMsg());
                } else {
                    boxView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                boxView.showError(e.getMessage());
            }
        }, companyNo, qrCode, qrCodeClass);
    }


    /**
     * 箱码和产品码互查
     *
     * @param qrCode
     */
    public void getQrCodeList(String qrCode, String qrCodeType) {
        httpMethods.getQrCodeList(new SingleObserver<BaseResponse<ArrayList<String>>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<ArrayList<String>> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    ArrayList<String> data = response.getData();
                    if (boxView == null) {
                        iListView.showSuccess(data);
                    } else {
                        boxView.getCodeList(data);
                    }
                } else {
                    if (boxView == null) {
                        iListView.showError(response.getMsg());
                    } else {
                        boxView.showError(response.getMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (boxView == null) {
                    iListView.showError(e.getMessage());
                } else {
                    boxView.showError(e.getMessage());
                }
            }
        }, qrCode, qrCodeType);

    }

    /* ------------------------------------  发货出库 ------------------------------------*/

    /**
     * 获取发货单信息(分状态)
     *
     * @param companyNo
     */
    public void getSendOutList2(String companyNo, String deliveryState,String goodsNo) {
        httpMethods.getSendOutList2(new SingleObserver<BaseResponse<SendOutListData>>() {
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
        }, companyNo, deliveryState,goodsNo);
    }

    /**
     * 发货单详情
     */
    public void getSendOutListInfo(String companyNo, String deliveryNo) {
        httpMethods.getSendOutListInfo(new SingleObserver<BaseResponse<SendOutListInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<SendOutListInfo> response) {
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
        }, companyNo, deliveryNo);

    }

    /**
     * 出库校验
     *
     * @param companyNo
     * @param qrCodeClass
     * @param goodsNo
     * @param qrCode
     */
    public void sendOutjudegCode(String companyNo, final String qrCodeClass, String goodsNo, final String qrCode) {
        httpMethods.sendOutjudegCode(new SingleObserver<BaseResponse<QrCodeJudge>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<QrCodeJudge> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {

                    boxView.sendJudegCode(response.getMsg());

                } else {
                    boxView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                boxView.showError(e.getMessage());

            }
        }, companyNo, qrCodeClass, goodsNo, qrCode);
    }


    /**
     * 获取出库信息(已发货 - 查看)
     */
    public void getSendOutPutInfo(String companyNo, String deliveryNo) {
        httpMethods.getSendOutPutInfo(new SingleObserver<BaseResponse<OutPutInfoData>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<OutPutInfoData> response) {
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
        }, companyNo, deliveryNo);

    }

    /**
     * 删除出库
     */
    public void deleteSendOut(String companyNo, String deliveryNo) {
        httpMethods.deleteSendOut(new SingleObserver<ResponseInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(ResponseInfo response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    iListView.showResponse(response);
                } else {
                    iListView.showError(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                iListView.showError(e.getMessage());
            }
        }, companyNo, deliveryNo);

    }

    /**
     * 新增出库
     *
     * @param body body
     */
    public void addSendOut(RequestBody body) {
        iView.showLoading();
        httpMethods.addSendOut(new SingleObserver<ResponseInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(ResponseInfo response) {
                iView.hideLoading();
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getMsg())) {
                    iView.showResponse(response);
                } else {
                    iView.showError(TipString.outFailure);
                }
            }

            @Override
            public void onError(Throwable e) {
                iView.hideLoading();
                iView.showError(e.getMessage());
            }
        }, body);
    }

    /*
     * --------------------------------- 稽查 ----------------------------------------
     */

    /**
     * 稽查确认
     *
     * @param companyNo  companyNo
     * @param deliveryNo deliveryNo
     */
    public void checkInfoConfirm(String companyNo, String deliveryNo, String fleeFlag) {
        httpMethods.checkInfoConfirm(new SingleObserver<ResponseInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                if (CodeConstant.SERVICE_SUCCESS.equals(responseInfo.getMsg())) {
                    iView.showResponse(responseInfo);
                } else {
                    iView.showError(responseInfo.getMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                iView.showError(e.getMessage());
            }
        }, companyNo, deliveryNo, fleeFlag);
    }

}
