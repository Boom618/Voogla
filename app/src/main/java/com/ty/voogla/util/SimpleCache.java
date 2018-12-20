package com.ty.voogla.util;


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



    public static String getString(String key){
        return aCache.getAsString(key);
    }



    /**
     * 移除某个key
     *
     * @param key
     * @return 是否移除成功
     */
    public static boolean clearKey(String key){
        return aCache.remove(key);
    }

    /**
     * 清除所有数据
     */
    public static void clearAll(){
        aCache.clear();
    }
}
