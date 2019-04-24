package com.ty.voogla.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.ui.ActivitiesHelper;
import com.ty.voogla.data.ACache;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;

/**
 * @author TY
 */
public class MainApp extends MultiDexApplication {

    public static Context context;
    public static ACache mCache;


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        mCache = ACache.get(context);
        // 日志 logger 库
        Logger.addLogAdapter(new AndroidLogAdapter());
        // bugly
        CrashReport.initCrashReport(getApplicationContext(), CodeConstant.BUGLY_APP_ID, false);

        // 内存检测
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        /*
         * 管理 AC 栈
         */
        ActivitiesHelper.init(this);

        // 二维码
        ZXingLibrary.initDisplayOpinion(this);

        // yokeyword 大佬的 Fragment 库
        Fragmentation.builder()
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(true)
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(@NonNull Exception e) {
                        // 处理捕获异常
                    }
                })
                .install();

    }

    public static Context getContext() {
        return context;
    }


}
