package com.ty.voogla.util.scan;

import android.content.Context;
import android.media.MediaPlayer;
import com.mexxen.barcode.BarcodeConfig;

/**
 * @author TY on 2019/1/15.
 * 扫码语音
 */
public class PDAUtil {


    /**
     * 初始化 聚光，补光
     */
    public static void initBarcodeConfig(BarcodeConfig mBarcodeConfig) {
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

    /**
     * 该箱码已扫码
     *
     * @param context
     * @param raw
     * @return
     */
    private static MediaPlayer rawAlready(Context context, int raw) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, raw);
        mediaPlayer.setVolume(0.05f, 0.05f);
        return mediaPlayer;
    }
}
