package com.ty.voogla.constant;

/**
 * @author TY on 2018/11/6.
 * <p>
 * 代码常量
 */
public final class CodeConstant {

    public static final String DATE_SIMPLE_Y_M_D = "yyyy年MM月dd日";
    public static final String DATE_SIMPLE_Y_M_D_H_M = "yyyy年MM月dd日 HH:mm:ss";
    public static final String DATE_SIMPLE_H_M = "yyyy-MM-dd HH:mm";
    public static final String DATE_SIMPLE_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_START_TIME = "2010-01-01 00:00";
    public static final String DATE_END_TIME = "2999-01-01 00:00";

    /**
     * 服务器返回成功标识
     */
    public static final String SERVICE_SUCCESS = "success";
    public static final int SERVICE_STATUS = 200;


    /**
     * 按键码 131 or 135 : K3A手柄键   139 : G3A F1键
     */
    public static final int KEY_CODE_131 = 131;
    public static final int KEY_CODE_135 = 135;
    public static final int KEY_CODE_139 = 139;


    /**
     * 区分箱码绑定页面
     */
    public static final String PAGE_STATE_KEY = "state";
    public static final String PAGE_BOX_LINK = "boxLink";
    public static final String PAGE_BOX_LINK_EDIT = "boxLinkEdit";
    public static final String PAGE_SCAN_OUT = "scanOut";

    /**
     * 腾讯 bug ID
     */
    public static final String BUGLY_APP_ID = "115fa1897c";

    /**
     * GOODS_NO 商品编号
     * GOODS_SPEC 商品规格
     * LOOK_TYPE  查看类型（入库/出库）
     * RESULT_TYPE 回传类型（入库/修改/出库）
     */
    public static final String GOODS_NO = "goodsNo";
    public static final String GOODS_SPEC = "goodSpec";
    public static final String LOOK_TYPE = "lookType";
    public static final String RESULT_TYPE = "resultType";
    /**
     * 发货单编号
     */
    public static final String DELIVERY_NO = "deliveryNo";

    /**
     * 箱码 boxCode
     * 产品码 qr_CodeInfos
     */
    public static final String BOX_CODE = "boxCode";
    public static final String QR_CODE_INFOS = "qrCodeInfos";
    /**
     * 发货出库 箱码或产品码集合
     */
    public static final String QR_CODE_LIST = "qrCodeList";

    /**
     * 发货明细列表 position
     */
    public static final String SEND_POSITION = "position";


    /**
     * 用户 sessionId 全局
     */

    public static final String SYSTEM_KEY = "system";
    public static final String SYSTEM_VALUE = "android";
    public static final String SESSION_ID_KEY = "sessionId";

    /**
     * SharedPreferences 文件名
     */
    public static final String SP_SHARED = "shared";

    /**
     * 相机 请求、响应 状态码
     * REQUEST_CODE_INTO: 入库
     * REQUEST_CODE_OUT:  出库
     */
    public static final int REQUEST_CODE_INTO = 100;
    public static final int REQUEST_CODE_OUT = 101;
    public static final int RESULT_CODE = 100;

    /**
     * 用户数据
     */
    public static final String USER_DATA = "userData";

}
