package com.ty.voogla.data;


import com.ty.voogla.bean.UserInfo;
import com.ty.voogla.bean.sendout.QrCodeListData;
import com.ty.voogla.util.ResourceUtil;

import java.util.ArrayList;

/**
 * @author PVer on 2018/12/15.
 * <p>
 * 对 ACache 缓存 简单处理
 */
public class SimpleCache {

    private static ACache aCache = ACache.get(ResourceUtil.getContext());

    public SimpleCache() {
        aCache = ACache.get(ResourceUtil.getContext());
    }


    public static void putString(String key, String value) {
        aCache.put(key, value);
    }

    public static void putUserInfo(UserInfo userInfo) {
        aCache.put("userInfo", userInfo);
    }

    public static void putQrCode(ArrayList<QrCodeListData> qrCode) {
        aCache.put("qrCode", qrCode);
    }

    public static String getString(String key) {
        return aCache.getAsString(key);
    }

    public static UserInfo getUserInfo() {
        return (UserInfo) aCache.getAsObject("userInfo");
    }

    public static ArrayList<QrCodeListData> getQrCode() {
        return (ArrayList<QrCodeListData>) aCache.getAsObject("qrCode");
    }



    /**
     * 移除某个key
     *
     * @param key
     * @return 是否移除成功
     */
    public static boolean clearKey(String key) {
        return aCache.remove(key);
    }

    /**
     * 清除所有数据
     */
    public static void clearAll() {
        aCache.clear();
    }
}
