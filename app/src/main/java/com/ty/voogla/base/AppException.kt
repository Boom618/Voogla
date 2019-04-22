package com.ty.voogla.base

/**
 * @author TY on 2019/4/22.
 * 自定义异常
 */
class AppException(private val msg: String) : Exception() {

    fun getMsg(): String {
        return msg
    }

}