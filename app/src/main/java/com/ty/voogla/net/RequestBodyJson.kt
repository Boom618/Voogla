package com.ty.voogla.net

import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * @author TY on 2018/12/27.
 * POST 请求
 */
object RequestBodyJson {

    /**
     * RequestBody--json 数据提交
     * Kotlin 没有静态变量与静态方法，采用 @JvmStatic 修饰的方法主要是兼容 Java 代码的调用方式和 Kotlin 一致
     * @param json
     * @return
     */
    @JvmStatic
    fun requestBody(json: String): RequestBody {

        return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json)
    }

    // FromBody---表单提交
    // MultipartBody---文件上传
}
