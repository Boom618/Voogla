package com.ty.voogla.net

import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.socks.library.KLog
import com.ty.voogla.base.AppException
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.ui.ActivitiesHelper
import com.ty.voogla.ui.activity.LoginMobActivity
import com.ty.voogla.util.ZBLog
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request

import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.log

/**
 * @author TY
 *
 *
 * okhttp3 日志拦截器
 * 网络连接失败， 没有响应信息
 */
class LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
        KLog.d(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()))

        val startTime = System.nanoTime()
        val response = chain.proceed(chain.request())
        val endTime = System.nanoTime()
        val duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime)

        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
        val fromJson = Gson().fromJson(content, ResponseInfo::class.java)
        if (fromJson.status == "401") {// || fromJson.status == "500"
            val activity = ActivitiesHelper.get().lastActivity
            val intent = Intent(activity, LoginMobActivity::class.java)
            activity.startActivity(intent)
            throw Exception(AppException("cookies 失效,请重新登录"))
        }
        KLog.e(TAG, "----------【Start】----------------")
        val method = request.method()
        if ("POST" == method) {
            // Get 请求 body 为空，报错
            KLog.d(TAG, "request Length : " + request.body()!!.contentLength())
            val sb = StringBuilder()
            if (request.body() is FormBody) {
                val body = request.body() as FormBody?
                for (i in 0 until body!!.size()) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",")
                }
                sb.delete(sb.length - 1, sb.length)
                KLog.d(TAG, "| RequestParams:{$sb}")
            }
        }
        KLog.json(TAG, "| Response:$content")
        KLog.e(TAG, "----------【End:" + duration + "毫秒】----------")
        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(mediaType, content))
            .build()
    }

    companion object {

        var TAG = "Http = "
    }
}
