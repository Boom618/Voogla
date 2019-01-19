package com.ty.voogla.constant;

/**
 * @author TY
 */
public class ApiNameConstant {

    /**
     * 徐坤
     */
    public static final String BASE_URL = "http://192.168.11.2:3099/";

    /**
     * 顾炎
     */
    public static final String BASE_URL1 = "http://192.168.11.24:3000/";

    /**
     * 少杰
     */
    public static final String BASE_URL2 = "http://192.168.11.6:3060/";

    /**
     * 齐超
     */
    public static final String BASE_URL3 = "http://192.168.11.4:10000/";

    /**
     * --------------------------------- 系统登录 ----------------------------------------
     */

    /**
     * 用户登录
     */
    public static final String USER_LOGIN = "login";

    /**
     * 用户登出
     */
    public static final String USER_LOGOUT = "sys/user/logout";


    /**
     * --------------------------------- 生产入库 ----------------------------------------
     */

    /**
     * 生产入库列表
     */
    public static final String PRODUCE_LIST = "corp/im/commodity-input-list";


    /**
     * 新增入库
     */
    public static final String PRODUCE_ADD = "corp/im/commodity-input-add";

    /**
     * 删除入库
     */
    public static final String PRODUCE_DELETE = "corp/im/commodity-input-del";

    /**
     * 产品列表信息
     */
    public static final String PRODUCE_LIST_INFO = "corp/im/commodity-info";

    /**
     * 入库详情信息
     */
    public static final String PRODUCE_LIST_INFO_INPUT = "corp/im/commodity-input-info";

    /**
     * 二维码解码
     */
    public static final String DECODE_URL = "deCodeUrl";

    /**
     * 二维码校验
     */
    public static final String CODE_JUDGE_PRODUCE = "corp/im/commodity-input-judge";

    /**
     * --------------------------------- 发货出库 ----------------------------------------
     */

    /**
     * 发货单信息
     */
    public static final String SEND_OUT_LIST = "corp/im/commodity-output-list";

    /**
     * 发货单详情信息
     */
    public static final String SEND_OUT_LIST_INFO = "corp/im/delivery-info";


    /**
     * 获取出库信息
     */
    public static final String SEND_OUTPUT_INFO = "corp/im/commodity-output-info";

    /**
     * 删除入库
     */
    public static final String SEND_OUT_DELETE = "corp/im/commodity-output-del";

    /**
     * 新增出库
     */
    public static final String SEND_OUT_ADD = "corp/im/commodity-output-add";

    /**
     * 二维码校验
     */
    public static final String CODE_JUDGE_SEND = "corp/im/commodity-output-judge";


}
