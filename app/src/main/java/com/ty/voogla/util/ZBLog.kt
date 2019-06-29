package com.ty.voogla.util

import com.orhanobut.logger.Logger
import com.ty.voogla.BuildConfig

/**
 * @author TY on 2018/11/6.
 *
 * Ty Logger
 */
object ZBLog {


    private val TAG = "ZBLog"

    /**
     * 是否开启 Logger
     */
    private val DEBUG = BuildConfig.DEBUG

    @JvmStatic
    fun d(msg: String) {
        if (DEBUG) {
            Logger.d(TAG, msg)
        }
    }

    @JvmStatic
    fun d(obj: Any) {
        if (DEBUG) {
            Logger.d(TAG, obj)
        }
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (DEBUG) {
            Logger.d(tag, msg)
        }
    }

    @JvmStatic
    fun d(tag: String, obj: Any) {
        if (DEBUG) {
            Logger.d(tag, obj)
        }
    }

    @JvmStatic
    fun e(msg: String) {
        if (DEBUG) {
            Logger.e(TAG, msg)
        }
    }

    @JvmStatic
    fun e(obj: Any) {
        if (DEBUG) {
            Logger.e(TAG, obj)
        }
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (DEBUG) {
            Logger.e(tag, msg)
        }
    }

    @JvmStatic
    fun e(tag: String, obj: Any) {
        if (DEBUG) {
            Logger.e(tag, obj)
        }
    }

}
