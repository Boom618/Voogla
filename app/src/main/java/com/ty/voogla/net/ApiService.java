package com.ty.voogla.net;

import com.ty.voogla.base.BaseResponse;
import com.ty.voogla.base.ResponseInfo;
import com.ty.voogla.bean.produce.*;
import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.bean.sendout.OutPutInfoData;
import com.ty.voogla.bean.sendout.SendOutListData;
import com.ty.voogla.bean.sendout.SendOutListInfo;
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


    @POST(ApiNameConstant.PRODUCE_ADD)
    Single<ResponseInfo> addProduct(@Body RequestBody body);

    /**
     * 删除入库
     *
     * @param companyNo
     * @param inBatchNo
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.PRODUCE_DELETE)
    Single<ResponseInfo> deleteProduct(@Field("companyNo") String companyNo,
                                       @Field("inBatchNo") String inBatchNo,
                                       @Field("companyAttr") String companyAttr);

    /**
     * 产品列表信息
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.PRODUCE_LIST_INFO)
    Single<BaseResponse<ProductListInfoData>> getProductListInfo(@Field("companyNo") String companyNo);

    /**
     * 入库详情信息
     *
     * @param companyNo 企业编号
     * @param inBatchNo 入库批次号
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.PRODUCE_LIST_INFO_INPUT)
    Single<BaseResponse<ProductInputInfo>> getInputProductInfo(@Field("companyNo") String companyNo,
                                                               @Field("inBatchNo") String inBatchNo);

    /**
     * 二维码检验
     *
     * @param secret
     * @return
     */
    @GET(ApiNameConstant.DECODE_URL)
    Single<DecodeCode> decodeUrlCode(@Query("secret") String secret);

    /**
     * 箱码校验
     *
     * @param companyNo
     * @param qrCode
     * @return
     */
    @POST(ApiNameConstant.CODE_JUDGE_PRODUCE)
    Single<BaseResponse<QrCodeJudge>> judegCode(@Field("companyNo") String companyNo,
                                                @Field("qrCode") String qrCode);

    /**
     * --------------------------------- 发货出库 ----------------------------------------
     */

    /**
     * 获取发货单列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.SEND_OUT_LIST)
    Single<BaseResponse<SendOutListData>> getSendOutList(@Field("companyNo") String companyNo,
                                                         @Field("flag") String flag);

    /**
     * 发货单详情信息
     * <p>
     * deliveryNo:发货单号
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.SEND_OUT_LIST_INFO)
    Single<BaseResponse<SendOutListInfo>> getSendOutListInfo(@Field("companyNo") String companyNo,
                                                             @Field("deliveryNo") String deliveryNo);

    /**
     * 获取出库信息(已发货 - 查看)
     * <p>
     * deliveryNo:发货单号
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.SEND_OUTPUT_INFO)
    Single<BaseResponse<OutPutInfoData>> getSendOutPutInfo(@Field("companyNo") String companyNo,
                                                           @Field("deliveryNo") String deliveryNo);

    /**
     * 新增出库
     *
     * @return
     */
    @POST(ApiNameConstant.SEND_OUT_ADD)
    Single<ResponseInfo> addSendOut(@Body RequestBody body);

    /**
     * 删除入库
     *
     * @return
     */
    @FormUrlEncoded
    @POST(ApiNameConstant.SEND_OUT_DELETE)
    Single<ResponseInfo> deleteSendOut(@Field("companyNo") String companyNo,
                                       @Field("deliveryNo") String deliveryNo);


//
//    /**
//     * 用户登出
//     *
//     * @return
//     */
//    @POST(ApiNameConstant.USER_LOGOUT)
//    Single<ResponseInfo> userLogout();


}
