package com.ty.voogla.net;

import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.base.ResponseInfo;
import com.ty.voogla.bean.DecodeCode;
import com.ty.voogla.bean.ProductIntoData;
import com.ty.voogla.bean.ProductListInfoData;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.constant.ApiNameConstant;
import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.*;

/**
 * @author TY
 */
public interface ApiService {


    /**
     * --------------------------------- 系统登录 ----------------------------------------
     */

    /**
     * 用户登录
     *
     * @param username 用户手机
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.USER_LOGIN)
    Single<BaseResponse<UserInfo>> userLogin(@Field("username") String username,
                                             @Field("password") String password);

    /**
     * 生产入库列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.PRODUCE_LIST)
    Single<BaseResponse<ProductIntoData>> getProductList(@Field("companyNo") String companyNo);


    @FormUrlEncoded
    @POST(ApiNameConstant.PRODUCE_ADD)
    Single<BaseResponse<ResponseInfo>> addProduct(@Body RequestBody body);

    @FormUrlEncoded
    @POST(ApiNameConstant.PRODUCE_DELETE)
    Single<BaseResponse<ResponseInfo>> deleteProduct(@Field("companyNo") String companyNo,
                                                     @Field("inBatchNo") String inBatchNo);

    /**
     * 产品列表信息
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.PRODUCE_LIST_INFO)
    Single<BaseResponse<ProductListInfoData>> getProductListInfo(@Field("companyNo") String companyNo);

    /**
     * 二维码检验
     *
     * @param secret
     * @return
     */
    @GET(ApiNameConstant.DECODE_URL)
    Single<DecodeCode> decodeUrlCode(@Query("secret") String secret);
//
//    /**
//     * 用户登出
//     *
//     * @return
//     */
//    @POST(ApiNameConstant.USER_LOGOUT)
//    Single<ResponseInfo> userLogout();


}
