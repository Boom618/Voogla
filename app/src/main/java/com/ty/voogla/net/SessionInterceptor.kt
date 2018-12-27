package com.ty.voogla.net

import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.util.SimpleCache
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

import java.io.IOException


/**
 * @author TY on 2018/12/14.
 *
 *
 * sessionId 头部 拦截器
 */
class SessionInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        var sessionId = ""
        try {
            sessionId = SimpleCache.getString(CodeConstant.SESSION_ID_KEY)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val authorised = originalRequest.newBuilder()
            .header(CodeConstant.SESSION_ID_KEY, "27DPMUT_YKcGemZW5glwaDpaAxRPBfhZ")
            .header(CodeConstant.SYSTEM_KEY, CodeConstant.SYSTEM_VALUE)
            .build()
        return chain.proceed(authorised)
    }
}
