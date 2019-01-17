package com.ty.voogla.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.base.ResponseInfo;
import com.ty.voogla.bean.produce.DecodeCode;
import com.ty.voogla.bean.produce.ProductIntoData;
import com.ty.voogla.bean.produce.ProductListInfoData;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.bean.sendout.SendOutListData;
import com.ty.voogla.constant.ApiNameConstant;
import com.ty.voogla.net.gson.DoubleDefault0Adapter;
import com.ty.voogla.net.gson.IntegerDefault0Adapter;
import com.ty.voogla.net.gson.LongDefault0Adapter;
import com.ty.voogla.net.gson.StringDefault0Adapter;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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
    private static HttpMethods mInstance;
    private ApiService mService;
    private static Gson gson;

    /**
     * cookie
     */
//    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
    private HttpMethods() {
        init(ApiNameConstant.BASE_URL2);
    }

    /**
     * URL 变更入口
     *
     * @param url
     */
    public HttpMethods(String url) {
        init(url);
    }

    private void init(String url) {
        // 创建OKHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .cookieJar(new CookieJar() {
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                        // 保存 cookie
//                        cookieStore.put(url.host(), cookies);
//                    }
//
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        // 加载新的 cookie
//                        List<Cookie> cookies = cookieStore.get(url.host());
//                        return cookies != null ? cookies : new ArrayList<Cookie>();
//                    }
//                })
                .addInterceptor(new SessionInterceptor())
                // 日志拦截器
                .addInterceptor(new LogInterceptor());

        Retrofit mRetrofit = new Retrofit.Builder()
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(url)
                .build();
        mService = mRetrofit.create(ApiService.class);
    }

    public static HttpMethods getInstance() {

        if (mInstance == null) {
            synchronized (HttpMethods.class) {
                if (mInstance == null) {
                    mInstance = new HttpMethods();
                }
            }
        }
        return mInstance;
    }


    /**
     * --------------------------------- 系统登录 ----------------------------------------
     */

    /**
     * 用户登录
     *
     * @param observer
     * @param username
     * @param password
     */
    public void userLogin(SingleObserver<BaseResponse<UserInfo>> observer, String username, String password) {
        mService.userLogin(username, password)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * --------------------------------- 产品入库  ----------------------------------------
     */

    /**
     * 入库列表
     *
     * @param observer
     * @param companyNo
     */
    public void getProductList(SingleObserver<BaseResponse<ProductIntoData>> observer, String companyNo) {
        mService.getProductList(companyNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 新增入库
     *
     * @param observer
     * @param body
     */
    public void addProduct(SingleObserver<ResponseInfo> observer, RequestBody body) {
        mService.addProduct(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 删除入库
     *
     * @param observer
     * @param companyNo
     * @param inBatchNo
     */
    public void deleteProduct(SingleObserver<BaseResponse<ResponseInfo>> observer, String companyNo, String inBatchNo) {
        mService.deleteProduct(companyNo, inBatchNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 产品列表信息
     */
    public void getProductListInfo(SingleObserver<BaseResponse<ProductListInfoData>> observer, String companyNo) {
        mService.getProductListInfo(companyNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 二维码解码
     *
     * @param observer
     * @param secret
     */
    public void decodeUrlCode(SingleObserver<DecodeCode> observer, String secret) {
        mService.decodeUrlCode(secret)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void decodeUrlCodeMap(String secret) {
        mService.decodeUrlCode(secret)
                .map(new Function<DecodeCode, DecodeCode>() {
                    @Override
                    public DecodeCode apply(DecodeCode decodeCode) throws Exception {
                        return decodeCode;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }


    /**
     * --------------------------------- 产品出库  ----------------------------------------
     */

    /**
     * 发货单信息
     * @param observer
     * @param companyNo
     */
    public void getSendOutList(SingleObserver<BaseResponse<SendOutListData>> observer, String companyNo){
        mService.getSendOutList(companyNo)
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
