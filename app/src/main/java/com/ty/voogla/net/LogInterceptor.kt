package com.ty.voogla.net

import android.util.Log
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request

import java.io.IOException

/**
 * @author TY
 *
 *
 * okhttp3 日志拦截器
 */
class LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        Log.e(TAG, request.url().toString())
        val startTime = System.currentTimeMillis()

        val response = chain.proceed(chain.request())
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        Log.e(TAG, "\n")
        Log.e(TAG, "----------【Start】----------------")
        val method = request.method()
        if ("POST" == method) {
            // Get 请求 body 为空，报错
            Log.e(TAG, "request Length : " + request.body()!!.contentLength())
            val sb = StringBuilder()
            if (request.body() is FormBody) {
                val body = request.body() as FormBody?
                for (i in 0 until body!!.size()) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",")
                }
                sb.delete(sb.length - 1, sb.length)
                Log.e(TAG, "| RequestParams:{" + sb.toString() + "}")
            }
        }
        Log.e(TAG, "| Response:$content")
        Log.e(TAG, "----------【End:" + duration + "毫秒】----------")
        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(mediaType, content))
            .build()
    }

    companion object {

        var TAG = "Http = "
    }
}
