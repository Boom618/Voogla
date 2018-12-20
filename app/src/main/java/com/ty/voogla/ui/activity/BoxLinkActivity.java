package com.ty.voogla.ui.activity;

import android.os.Bundle;
import com.ty.voogla.base.BaseActivity;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;

/**
 * @author TY on 2018/12/20.
 * <p>
 * 箱码绑定
 */
public class BoxLinkActivity extends BaseActivity implements VooglaContract.View {


    private VooglaPresenter presenter = new VooglaPresenter(this);


    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {

    }

    @Override
    protected int getActivityLayout() {
        return 0;
    }

    @Override
    protected void initOneData() {

    }

    @Override
    protected void initTwoView() {

    }

    @Override
    public void showSuccess() {

    }

    @Override
    public void showError(String msg) {

    }
}
