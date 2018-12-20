package com.ty.voogla.base

import java.io.Serializable

/**
 * @author TY on 2018/10/26.
 */
class BaseResponse<T> : Serializable {

    var tag: String? = null
    var message: String? = null
    var data: T? = null
}
