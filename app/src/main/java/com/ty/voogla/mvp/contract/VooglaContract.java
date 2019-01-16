package com.ty.voogla.mvp.contract;

import com.ty.voogla.bean.ProductListInfoData;

/**
 * @author TY on 2018/12/20.
 * <p>
 * MVP  Contract 契约类
 */
public interface VooglaContract {

    interface Model {
    }

    interface View<T> {
        /**
         * Presenter 完成数据加载,该方法展示在 view 上
         */
        void showSuccess(T data);

        /**
         * 错误信息
         *
         * @param msg
         */
        void showError(String msg);
    }

    interface Presenter {
        /**
         * 从服务器拉取数据
         *
         * @param userName
         * @param password
         */
        void getData(String userName, String password);
    }
}
