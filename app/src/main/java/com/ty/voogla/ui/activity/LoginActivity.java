package com.ty.voogla.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.ty.voogla.R;
import com.ty.voogla.base.BaseActivity;
import com.ty.voogla.mvp.contract.VooglaContract;
import com.ty.voogla.mvp.presenter.VooglaPresenter;
import com.ty.voogla.util.ToastUtil;

/**
 * @author TY on 2018/12/20.
 */
public class LoginActivity extends BaseActivity implements VooglaContract.View {

    private EditText userName;
    private EditText userPass;
    private Button btLogin;

    private VooglaPresenter presenter = new VooglaPresenter(this);


    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {

    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_login_voogla;
    }

    @Override
    protected void initOneData() {

    }

    @Override
    protected void initTwoView() {

        userName = findViewById(R.id.et_user_name);
        userPass = findViewById(R.id.et_user_pass);
        btLogin = findViewById(R.id.login);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString().trim();
                String pass = userPass.getText().toString().trim();
                gotoActivity(MainActivity.class, true);
                //presenter.getData(name, pass);
            }
        });

    }

    @Override
    public void showSuccess() {
        gotoActivity(MainActivity.class, true);
    }

    @Override
    public void showError(String msg) {

        ToastUtil.showToast(msg);

    }
}
