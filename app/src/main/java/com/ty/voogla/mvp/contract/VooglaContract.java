package com.ty.voogla.mvp.contract;

import com.ty.voogla.base.ResponseInfo;
import com.ty.voogla.bean.produce.DecodeCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TY on 2018/12/20.
 * <p>
 * MVP  Contract 契约类
 */
public interface VooglaContract {

    interface View<T> {
        /**
         * Presenter 完成数据加载,该方法展示在 view 上
         * 返回回调数据
         *
         * @param data obj 数据
         */
        void showSuccess(T data);

        /**
         * 错误信息
         *
         * @param msg msg
         */
        void showError(String msg);

        /**
         * 返回响应信息（成功 失败）
         *
         * @param response
         */
        void showResponse(ResponseInfo response);

        /**
         * 保存加载 dialog
         */
        void showLoading();

        /**
         * 隐藏 dialog
         */
        void hideLoading();
    }

    /**
     * 发货出库 接收 list
     *
     * @param <T>
     */
    interface ListView<T> {
        /**
         * Presenter 完成数据加载,该方法展示在 view 上
         */
        void showSuccess(List<T> data);

        /**
         * 错误信息
         *
         * @param msg msg
         */
        void showError(String msg);

        /**
         * 返回响应信息（成功 失败）
         *
         * @param response
         */
        void showResponse(ResponseInfo response);

        /**
         * 保存加载 dialog
         */
        void showLoading();

        /**
         * 隐藏 dialog
         */
        void hideLoading();
    }

    /**
     * 扫码页面单独接口
     */
    interface BoxLinkView {


        void decodeCode(DecodeCode.ResultBean resultBean);

        void produceJudegCode(String response);

        void sendJudegCode(String response);

        void getCodeList(ArrayList<String> codeList);

        void showError(String msg);
    }

//    interface Presenter {
//        /**
//         * 从服务器拉取数据
//         *
//         * @param userName
//         * @param password
//         */
//        void getData(String userName, String password);
//    }
}
