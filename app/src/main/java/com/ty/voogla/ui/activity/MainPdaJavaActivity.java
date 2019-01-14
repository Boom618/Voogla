package com.ty.voogla.ui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeReader;
import com.ty.voogla.R;
import com.ty.voogla.base.BaseActivity;
import org.jetbrains.annotations.Nullable;

/**
 * @author TY on 2019/1/12.
 */
public class MainPdaJavaActivity extends BaseActivity implements View.OnClickListener {

    private static BarcodeReader barcodeReader;
    private AidcManager manager;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_main_pda;
    }

    @Override
    protected void onBaseCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initOneData() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        AidcManager.create(this, new AidcManager.CreatedCallback() {

            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;
                try {
                    barcodeReader = manager.createBarcodeReader();
                } catch (Exception e) {
                    Toast.makeText(MainPdaJavaActivity.this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void initTwoView() {

        ImageView product = findViewById(R.id.image_product);
        ImageView send = findViewById(R.id.image_send);
        ImageView user = findViewById(R.id.image_user);
        setViewOnClickListener(this, product, send, user);

    }

    static BarcodeReader getBarcodeObject() {
        return barcodeReader;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_product:
                gotoActivity(ProduceIntoActivity.class);
                break;
            case R.id.image_send:
                gotoActivity(SendOutActivity.class);
                break;
            case R.id.image_user:
                gotoActivity(UserContentActivity.class);
            default:
                break;

        }
    }
}
