package com.ty.voogla.mvp.contract;

/**
 * @author TY on 2018/12/20.
 */
public interface VooglaContract {

    interface Model {
    }

    interface View {
        /**
         * Presenter 完成数据加载,该方法展示在 view 上
         */
        void showView();
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
