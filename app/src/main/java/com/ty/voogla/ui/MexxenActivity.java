package com.ty.voogla.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.mexxen.barcode.BarcodeConfig;
import com.mexxen.barcode.BarcodeEvent;
import com.mexxen.barcode.BarcodeListener;
import com.mexxen.barcode.BarcodeManager;
import com.ty.voogla.R;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author TY
 * <p>
 * 赫盛 手持机 SDK Demo
 */
public class MexxenActivity extends Activity {

    private static String TAG = "ScanerDemo";
    private Button mButtonStart, mButtonStop, mButtonGetBarcode, mButtonQuit;

    private Button mButtonAimLight, mButtonDecodeLight;
    private TextView mTextViewCodeResult, mTextViewCodeSize, mTextKyeCode;
    public Context mContext;


    private static final String SCANNER_READ = "SCANNER_READ";
    private BarcodeManager mBarcodeManager;
    private BarcodeConfig mBarcodeConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Log.d(TAG, "---->>heww onCreate()");
        initUI();
        mContext = this;

        mBarcodeConfig = new BarcodeConfig(this);

        boolean isAim = mBarcodeConfig.isDecodeAimIlluminiation();
        Log.d(TAG, "---->>heww setAimLight()isAim = " + isAim);
        if (isAim) {
            mButtonAimLight.setText("聚焦_close");
        } else {
            mButtonAimLight.setText("聚焦_open");
        }

        boolean isDecodeLight = mBarcodeConfig.isDecodeingIlluminiation();
        Log.d(TAG, "---->>heww setAimLight()isDecodeLight = " + isDecodeLight);
        if (isDecodeLight) {
            mButtonDecodeLight.setText("补光_close");
        } else {
            mButtonDecodeLight.setText("补光_open");
        }

        mBarcodeManager = new BarcodeManager(this);
        // 扫码监听
        if (listener == null) {
            listener = new BarcodeListener() {
                @Override
                public void barcodeEvent(BarcodeEvent event) {
                    // TODO Auto-generated method stub
                    Log.d(TAG, "---->>heww barcodeEvent() event =" + event.getOrder());
                    // 当条码事件的命令为“SCANNER_READ”时，进行操作
                    if (event.getOrder().equals(SCANNER_READ)) {
                        // 扫码结果
                        handleResult();
                    }
                }
            };
        }

        mBarcodeManager.addListener(listener);

    }

    BarcodeListener listener;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        // return super.onKeyDown(arg0, arg1);
        mTextKyeCode.setText("按键值为 " + keyCode);
//        if (keyCode == 223 || keyCode == 224){
//            handleStartScaner();
//        }
        return true;
    }


    public void initUI() {
        mButtonStart = findViewById(R.id.button1);
        mButtonGetBarcode = findViewById(R.id.button2);
        mButtonStop = findViewById(R.id.button3);
        mButtonQuit = findViewById(R.id.button4);

        mButtonGetBarcode.setOnClickListener(mButtonListener);
        mButtonStart.setOnClickListener(mButtonListener);
        mButtonStop.setOnClickListener(mButtonListener);
        mButtonQuit.setOnClickListener(mButtonListener);

        mTextViewCodeResult = findViewById(R.id.textView7);
        mTextViewCodeSize = findViewById(R.id.textView9);
        mTextKyeCode = findViewById(R.id.textView11);

        mButtonAimLight = findViewById(R.id.btn_aim_light);
        mButtonDecodeLight = findViewById(R.id.btn_decode_light);
        mButtonAimLight.setOnClickListener(mButtonListener);
        mButtonDecodeLight.setOnClickListener(mButtonListener);
    }

    OnClickListener mButtonListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.button1:
                    initStart();
                    handleStartScaner();
                    break;
                case R.id.button2:
                    handleGetBarcode();
                    break;
                case R.id.button3:
                    mButtonStart.setEnabled(true);
                    handleStopScaner();
                    break;
                case R.id.button4:
                    quitApp();
                    break;
                case R.id.btn_aim_light:
                    // 聚焦
//                    setAimLight();
                    break;
                case R.id.btn_decode_light:
                    // 补光
//                    setDecodeLight();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 初始化 聚光，补光
     */
    private void initStart(){
        mButtonStart.setEnabled(false);
        boolean isDecodeLight = mBarcodeConfig.isDecodeingIlluminiation();
        boolean isAim = mBarcodeConfig.isDecodeAimIlluminiation();
        if (!isDecodeLight) {
            mBarcodeConfig.setDecodeingIlluminiation(true);
            mBarcodeConfig.setScannerParamsChange();
        }
        if (!isAim) {
            mBarcodeConfig.setDecodeAimIlluminiation(true);
            mBarcodeConfig.setScannerParamsChange();
        }
    }


    private void setAimLight() {
        boolean isAim = mBarcodeConfig.isDecodeAimIlluminiation();
        Log.d(TAG, "---->>heww setAimLight()isAim = " + isAim);
        if (isAim) {
            mBarcodeConfig.setDecodeAimIlluminiation(false);
            mButtonDecodeLight.setText("aim_light_open");
        } else {
            mBarcodeConfig.setDecodeAimIlluminiation(true);
            mButtonDecodeLight.setText("aim_light_close");
        }
        mBarcodeConfig.setScannerParamsChange();
    }

    private void setDecodeLight() {
        boolean isDecodeLight = mBarcodeConfig.isDecodeingIlluminiation();
        Log.d(TAG, "---->>heww setAimLight()isDecodeLight = " + isDecodeLight);
        if (isDecodeLight) {
            mBarcodeConfig.setDecodeingIlluminiation(false);
            mButtonDecodeLight.setText("aim_light_open");
        } else {
            mBarcodeConfig.setDecodeingIlluminiation(true);
            mButtonDecodeLight.setText("aim_light_close");
        }
        mBarcodeConfig.setScannerParamsChange();
    }

    Disposable subscribe;
    private void handleStartScaner() {
        subscribe = Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i("TAG",  "accept aLong = " + aLong);
                        startScaner();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("TAG",  "Throwable");
                    }
                });
    }

    private void handleGetBarcode() {
        handleResult();
    }

    private void handleStopScaner() {
        stopScaner();
    }

    private void handleResult() {
        Log.d(TAG, "---->>heww handleResult()");
        // 调用 getBarcode()方法读取条码信息

        String barcode = mBarcodeManager.getBarcode();
        if (barcode != null) {
            int size = barcode.length();
            Log.d(TAG, "---->>heww handleResult() barcode =" + barcode + ",size=" + size);
            mTextViewCodeResult.setText(barcode);
            mTextViewCodeSize.setText("" + size);
        } else {
            mTextViewCodeResult.setText(" ");
            mTextViewCodeSize.setText(" ");
        }
    }

    private void quitApp() {
        finish();
        stopScaner();
    }


    private void startScaner() {
        mBarcodeManager.startScanner();
    }

    private void stopScaner() {
        subscribe.dispose();
        mBarcodeManager.stopScanner();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mBarcodeManager.dismiss();
        mBarcodeManager.removeListener(listener);
    }
}
