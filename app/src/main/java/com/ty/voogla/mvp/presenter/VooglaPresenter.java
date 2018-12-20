package com.ty.voogla.mvp.presenter;

import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.net.HttpMethods;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * @author TY on 2018/12/20.
 */
public class VooglaPresenter implements VooglaContract.Presenter {

    private HttpMethods httpMethods;

    private VooglaContract.View iView;

    private Disposable disposable;

    public VooglaPresenter(VooglaContract.View view) {
        httpMethods = HttpMethods.getInstance();
        this.iView = view;
    }


    @Override
    public void getData(String userName, String password) {

        httpMethods.userLogin(new SingleObserver<BaseResponse<UserInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onSuccess(BaseResponse<UserInfo> response) {
                if (CodeConstant.SERVICE_SUCCESS.equals(response.getTag())) {
                    iView.showView();
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        }, userName, password);


    }
}
