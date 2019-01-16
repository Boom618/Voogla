package com.ty.voogla.util.scan;


import android.content.Context;
import android.media.MediaPlayer;
import com.honeywell.aidc.BarcodeReadEvent;
import com.ty.voogla.R;
import com.ty.voogla.bean.DecodeCode;
import com.ty.voogla.constant.CodeConstant;
import io.reactivex.*;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
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
     * 运用 flatMap 解决嵌套请求
     * map 和 flatMap
     * <p>
     * flatMap:第一个函数是待操作的上一个数据流中的数据类型
     * 第二个是这个flatMap操作完成后返回的数据类型的被封装的Observable
     * <p>
     * flatMap 的作用就是对传入的对象进行处理，返回下一级所要的对象的 Observable 包装。
     */
    public static void showBarcodeEvent(final Context context, final int raw, final BarcodeReadEvent event, final ArrayList<String> list) {

        Observable.create(new ObservableOnSubscribe<BarcodeReadEvent>() {
            @Override
            public void subscribe(ObservableEmitter<BarcodeReadEvent> emitter) throws Exception {
                emitter.onNext(event);
            }
        })
                .map(new Function<BarcodeReadEvent, String>() {
                    @Override
                    public String apply(BarcodeReadEvent barcodeReadEvent) throws Exception {
                        return barcodeReadEvent.getBarcodeData();
                    }
                })
                .flatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Exception {
                        return new Observable<String>() {
                            @Override
                            protected void subscribeActual(Observer<? super String> observer) {
                            }
                        };
                    }
                })
                // 被观察者，设置为子线程
                .subscribeOn(Schedulers.io())
                .throttleFirst(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String barcodeData) throws Exception {

                        if (list.contains(barcodeData)) {
                            rawAlready(context, R.raw.scan_already).start();
                        } else {
                            list.add(barcodeData);
//                            adapter.notifyDataSetChanged();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

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
