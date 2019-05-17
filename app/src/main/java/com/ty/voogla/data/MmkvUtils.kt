package com.ty.voogla.data

import com.tencent.mmkv.MMKV

/**
 * @author  TY
 * @Date:   2019/5/16 16:43
 * @Description: tencent mmkv 替换 SP
 */
object MmkvUtils {

    private var mmkv = MMKV.defaultMMKV()

    fun putInt(key: String, int: Int) {
        mmkv.encode(key, int)
    }

    fun getInt(key: String) {
        mmkv.decodeInt(key, 0)
    }

    fun clearKey(key: String) {
//        val reKey = mmkv.reKey(key)
//        val remove = mmkv.remove(key)
        mmkv.removeValueForKey(key)
    }

    fun putSet(key: String, set: MutableSet<String>) {
        mmkv.decodeStringSet(key, set)
    }

}