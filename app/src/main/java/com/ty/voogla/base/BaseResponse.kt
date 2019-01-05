package com.ty.voogla.base

import java.io.Serializable

/**
 * @author TY on 2018/10/26.
 */
class BaseResponse<T> : Serializable {

    var status: Int? = null
    var msg: String? = null
    var data: T? = null
}
