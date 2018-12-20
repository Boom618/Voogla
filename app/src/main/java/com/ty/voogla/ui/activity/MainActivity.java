package com.ty.voogla.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ty.voogla.R;
import com.ty.voogla.base.BaseActivity;

/**
 * @author TY on 2018/12/20.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button boxLink;
    private Button houseIn;
    private Button houseOut;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {

    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initOneData() {

    }

    @Override
    protected void initTwoView() {

        boxLink = findViewById(R.id.box_link);
        houseIn = findViewById(R.id.warehousing_into);
        houseOut = findViewById(R.id.warehousing_out);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.box_link:
                gotoActivity(BoxLinkActivity.class);
                break;
            default:
                break;
        }
    }
}
