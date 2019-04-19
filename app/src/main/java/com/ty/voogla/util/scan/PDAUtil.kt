package com.ty.voogla.util.scan

import android.content.Context
import android.media.MediaPlayer
import com.mexxen.barcode.BarcodeConfig

/**
 * @author TY on 2019/1/15.
 * 扫码语音
 */
object PDAUtil {


    /**
     * 初始化 聚光，补光
     */
    @JvmStatic
    fun initBarcodeConfig(mBarcodeConfig: BarcodeConfig, openLight: Boolean) {
        val isDecodeLight = mBarcodeConfig.isDecodeingIlluminiation
        // isDecodeLight = false 开启聚光，其他情况不管
        when (mBarcodeConfig.isDecodeAimIlluminiation) {
            false -> {
                mBarcodeConfig.isDecodeAimIlluminiation = true
                mBarcodeConfig.setScannerParamsChange()
            }
            else -> {
            }
        }
        when (openLight) {
            false -> {
                if (isDecodeLight) {
                    mBarcodeConfig.isDecodeingIlluminiation = false
                    mBarcodeConfig.setScannerParamsChange()
                }
            }
            true -> {
                if (!isDecodeLight) {
                    mBarcodeConfig.isDecodeingIlluminiation = true
                    mBarcodeConfig.setScannerParamsChange()
                }
            }
        }
    }

    /**
     * 该箱码已扫码
     *
     * @param context
     * @param raw
     * @return
     */
    private fun rawAlready(context: Context, raw: Int): MediaPlayer {
        val mediaPlayer = MediaPlayer.create(context, raw)
        mediaPlayer.setVolume(0.05f, 0.05f)
        return mediaPlayer
    }
}
