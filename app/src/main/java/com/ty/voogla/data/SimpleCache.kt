package com.ty.voogla.data


import com.ty.voogla.bean.ProStorageData
import com.ty.voogla.bean.UserInfo
import com.ty.voogla.bean.produce.ProductListInfoData
import com.ty.voogla.bean.sendout.QrCodeListData
import com.ty.voogla.util.ResourceUtil

import java.util.ArrayList

/**
 * @author PVer on 2018/12/15.
 *
 *
 * 对 ACache 缓存 简单处理
 */
class SimpleCache {
    init {
        aCache = ACache.get(ResourceUtil.getContext())
    }

    companion object {

        private var aCache = ACache.get(ResourceUtil.getContext())


        fun putString(key: String, value: String) {
            aCache.put(key, value)
        }

        fun putUserInfo(userInfo: UserInfo) {
            aCache.put("userInfo", userInfo)
        }

        // 生产入库 暂存数据
        fun putStorage(data: ProStorageData) {
            aCache.put("storage", data)
        }

        // 产品列表数据
        fun putProductList(data: ProductListInfoData) {
            aCache.put("product", data)
        }

        fun putQrCode(qrCode: ArrayList<QrCodeListData>) {
            aCache.put("qrCode", qrCode)
        }

        fun getString(key: String): String? {
            return aCache.getAsString(key)
        }

        val userInfo: UserInfo
            get() = aCache.getAsObject("userInfo") as UserInfo

        val qrCode: ArrayList<QrCodeListData>
            get() = aCache.getAsObject("qrCode") as ArrayList<QrCodeListData>

        val productList: ProductListInfoData
            get() = aCache.getAsObject("product") as ProductListInfoData

        /**
         * 移除某个key
         *
         * @param key
         * @return 是否移除成功
         */
        fun clearKey(key: String): Boolean {
            return aCache.remove(key)
        }

        /**
         * 清除所有数据
         */
        fun clearAll() {
            aCache.clear()
        }
    }
}
