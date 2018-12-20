package com.ty.voogla.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.constant.ApiNameConstant;
import com.ty.voogla.net.gson.DoubleDefault0Adapter;
import com.ty.voogla.net.gson.IntegerDefault0Adapter;
import com.ty.voogla.net.gson.LongDefault0Adapter;
import com.ty.voogla.net.gson.StringDefault0Adapter;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * description:
 * author: XingZheng
 * date: 2016/11/22.
 *
 * @author TY
 */
public class HttpMethods {

    /**
     * 默认超时时间
     */
    private static final int DEFAULT_TIMEOUT = 20;
    private Retrofit mRetrofit;
    private ApiService mService;
    private static Gson gson;
    private String baseUrl = ApiNameConstant.BASE_URL;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private HttpMethods() {
        init(baseUrl);
    }

    public HttpMethods(String url) {
        init(url);
    }

    private void init(String url) {
        //创建OKHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new SessionInterceptor())
                // 日志拦截器
                .addInterceptor(new LogInterceptor());

        mRetrofit = new Retrofit.Builder()
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(url)
                .build();
        mService = mRetrofit.create(ApiService.class);
    }

    //构建单例
    public static class SingletonHolder {
        static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * --------------------------------- 系统登录 ----------------------------------------
     */

    /**
     * 用户登录
     *
     * @param observer
     * @param userName
     * @param password
     */
    public void userLogin(SingleObserver<BaseResponse<UserInfo>> observer, String userName, String password) {
        mService.userLogin(userName, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }



    /**
     * --------------------------------- Gson  ----------------------------------------
     */

    private static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(String.class, new StringDefault0Adapter())
                    .create();
        }
        return gson;
    }

}
