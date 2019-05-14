package com.ty.voogla.data

import android.content.Context
import cc.fussen.cache.Cache
import com.ty.voogla.bean.sendout.SendOutListInfo
import com.ty.voogla.util.ResourceUtil

/**
 * @author  TY
 * @Date:   2019/5/13 9:57
 * @Description: 依赖 DiskLruCache 缓存数据
 */
object LibraryCache {


    /**
     * 缓存发货出库数据
     */
    @JvmStatic
    fun SendOutCache(
        context: Context,
        path: String,
        deliveryNoKey: String,
        list: MutableList<SendOutListInfo.DeliveryDetailInfosBean>
    ) {

        Cache.with(context)
            .saveCache(deliveryNoKey, list)

    }

    @JvmStatic
    fun getOutCache(
        context: Context,
        path: String,
        deliveryNoKey: String
    ): MutableList<SendOutListInfo.DeliveryDetailInfosBean> {
        return Cache.with(context)
            .getCacheList(deliveryNoKey, SendOutListInfo.DeliveryDetailInfosBean::class.java)
    }

    @JvmStatic
    fun clearCache(context: Context, keyRemove: String) {
        Cache.with(context)
            .remove(keyRemove)
    }
}