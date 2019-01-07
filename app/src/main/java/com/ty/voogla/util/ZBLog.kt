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

    fun d(msg: String) {
        if (DEBUG) {
            Logger.d(TAG, msg)
        }
    }

    fun d(obj: Any) {
        if (DEBUG) {
            Logger.d(TAG, obj)
        }
    }

    fun d(tag: String, msg: String) {
        if (DEBUG) {
            Logger.d(tag, msg)
        }
    }

    fun d(tag: String, obj: Any) {
        if (DEBUG) {
            Logger.d(tag, obj)
        }
    }


    fun e(msg: String) {
        if (DEBUG) {
            Logger.e(TAG, msg)
        }
    }

    fun e(obj: Any) {
        if (DEBUG) {
            Logger.e(TAG, obj)
        }
    }

    fun e(tag: String, msg: String) {
        if (DEBUG) {
            Logger.e(tag, msg)
        }
    }

    fun e(tag: String, obj: Any) {
        if (DEBUG) {
            Logger.e(tag, obj)
        }
    }

}
