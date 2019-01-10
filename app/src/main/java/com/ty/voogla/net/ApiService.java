package com.ty.voogla.net;

import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.bean.ProductIntoData;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.constant.ApiNameConstant;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

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
    @POST(ApiNameConstant.PRODUCE_LIST)
    Single<BaseResponse<ProductIntoData>> getProductList();
//
//    /**
//     * 用户登出
//     *
//     * @return
//     */
//    @POST(ApiNameConstant.USER_LOGOUT)
//    Single<ResponseInfo> userLogout();


}
