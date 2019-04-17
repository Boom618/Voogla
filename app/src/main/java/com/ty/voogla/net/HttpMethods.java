package com.ty.voogla.net;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.base.ResponseInfo;
import com.ty.voogla.bean.CheckInfoList;
import com.ty.voogla.bean.produce.*;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.bean.sendout.OutPutInfoData;
import com.ty.voogla.bean.sendout.SendOutListData;
import com.ty.voogla.bean.sendout.SendOutListInfo;
import com.ty.voogla.constant.ApiNameConstant;
import com.ty.voogla.net.gson.DoubleDefault0Adapter;
import com.ty.voogla.net.gson.IntegerDefault0Adapter;
import com.ty.voogla.net.gson.LongDefault0Adapter;
import com.ty.voogla.net.gson.StringDefault0Adapter;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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
    private ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

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
        final HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 创建OKHttpClient
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        // 保存 cookie
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        // 加载新的 cookie
                        List<Cookie> cookies = cookieStore.get(url.host());
                        if (cookies != null) {
                            Log.e("cookies",cookies.toString());
                        }else{
                            Log.e("cookies","cookies is null");
                        }

                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                //.addInterceptor(new SessionInterceptor()) // 用 cookies 不用 session
                // 日志拦截器
//                .addInterceptor(new LogInterceptor())
                .addInterceptor(log);

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
    public void getProductList(SingleObserver<BaseResponse<ProductIntoData>> observer, String companyNo, String batchNo) {
        mService.getProductList(companyNo, batchNo)
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
    public void deleteProduct(SingleObserver<ResponseInfo> observer, String companyNo, String inBatchNo, String companyAttr) {
        mService.deleteProduct(companyNo, inBatchNo, companyAttr)
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
     * 入库详情信息(查看)
     *
     * @param observer
     * @param companyNo
     * @param inBatchNo
     */
    public void getInputProductInfo(SingleObserver<BaseResponse<ProductInputInfo>> observer, String companyNo, String inBatchNo) {
        mService.getInputProductInfo(companyNo, inBatchNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 二维码解码
     *
     * @param observer
     * @param encodeUrl
     */
    public void decodeUrlCode(SingleObserver<DecodeCode> observer, String encodeUrl) {
        mService.decodeUrlCode(encodeUrl)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 入库 二维码校验
     *
     * @param observer
     * @param companyNo
     * @param qrCode
     */
    public void judegCode(SingleObserver<BaseResponse<QrCodeJudge>> observer, String companyNo, String qrCode,String qrCodeClass) {
        mService.judegCode(companyNo, qrCode,qrCodeClass)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 根据箱码获取产品码
     *
     * @param qrCode
     * @return
     */
    public void getQrCodeList(SingleObserver<BaseResponse<ArrayList<String>>> observer, String qrCode,String qrCodeType) {
        mService.getQrCodeList(qrCode,qrCodeType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }


    /**
     * --------------------------------- 产品出库  ----------------------------------------
     */

    /**
     * 发货单信息
     *
     * @param observer
     * @param companyNo
     */
    public void getSendOutList(SingleObserver<BaseResponse<SendOutListData>> observer, String companyNo) {
        mService.getSendOutList(companyNo, "02")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    /**
     * 发货单信息
     *
     * @param observer
     * @param companyNo
     */
    public void getSendOutList2(SingleObserver<BaseResponse<SendOutListData>> observer, String companyNo,String deliveryState) {
        mService.getSendOutList2(companyNo, deliveryState)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 发货单详情信息
     * <p>
     * deliveryNo:发货单号
     */
    public void getSendOutListInfo(SingleObserver<BaseResponse<SendOutListInfo>> observer, String companyNo, String deliveryNo) {
        mService.getSendOutListInfo(companyNo, deliveryNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 获取出库信息(已发货 - 查看)
     * <p>
     * deliveryNo:发货单号
     */
    public void getSendOutPutInfo(SingleObserver<BaseResponse<OutPutInfoData>> observer, String companyNo, String deliveryNo) {
        mService.getSendOutPutInfo(companyNo, deliveryNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 新增出库
     *
     * @param observer
     * @param body
     */
    public void addSendOut(SingleObserver<ResponseInfo> observer, RequestBody body) {
        mService.addSendOut(body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 删除出库
     */
    public void deleteSendOut(SingleObserver<ResponseInfo> observer, String companyNo, String deliveryNo) {
        mService.deleteSendOut(companyNo, deliveryNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 出库二维码校验
     *
     * @param observer
     * @param companyNo
     * @param qrCodeClass
     * @param goodsNo
     * @param qrCode
     */
    public void sendOutjudegCode(SingleObserver<BaseResponse<QrCodeJudge>> observer, String companyNo,
                                 String qrCodeClass, String goodsNo, String qrCode) {
        mService.sendOutjudegCode(companyNo, qrCodeClass, goodsNo, qrCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * --------------------------------- 稽查 ----------------------------------------
     */

    public void checkInfoList(SingleObserver<BaseResponse<CheckInfoList>> observer, String qrCodeClass, String qrCode) {
        mService.checkInfoList(qrCodeClass, qrCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 稽查确认
     *
     * @param observer
     * @param companyNo
     * @param deliveryNo
     * @param fleeFlag 01 正常 02 窜货
     */
    public void checkInfoConfirm(SingleObserver<ResponseInfo> observer, String companyNo, String deliveryNo,String fleeFlag) {
        mService.checkInfoConfirm(companyNo, deliveryNo, fleeFlag)
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
