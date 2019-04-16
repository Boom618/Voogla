package com.ty.voogla.util.scan;


import android.content.Context;
import android.media.MediaPlayer;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * @author TY on 2019/1/15.
 * 扫码语音
 */
public class ScanSoundUtil {

    /**
     * 第一步：初始化 Observable
     * 第二步：初始化 Observer
     * 第三步：建立订阅关系
     *
     * @param raw
     */

    public static void showSound(final Context context, final int raw) {


        Observable.just(true)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                        rawAlready(context, raw).start();
                    }
                });
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
