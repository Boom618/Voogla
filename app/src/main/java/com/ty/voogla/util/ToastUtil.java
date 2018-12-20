package com.ty.voogla.util;

import android.widget.Toast;

/**
 * @author TY on 2018/12/20.
 */
public class ToastUtil {

    public static void showToast(String msg) {
        Toast.makeText(ResourceUtil.getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
