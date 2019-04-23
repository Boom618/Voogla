package com.ty.voogla.net

import android.content.Intent
import com.google.gson.Gson
import com.socks.library.KLog
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.constant.TipString
import com.ty.voogla.ui.ActivitiesHelper
import com.ty.voogla.ui.activity.LoginMobActivity
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * @author TY on 2018/12/14.
 *
 *
 * sessionId 头部 拦截器
 */
class SessionInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val startTime = System.nanoTime()
        val response = chain.proceed(chain.request())
        val endTime = System.nanoTime()
        val duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime)

        val mediaType = response.body()!!.contentType()
        // 响应数据
        val content = response.body()!!.string()
        val fromJson = Gson().fromJson(content, ResponseInfo::class.java)
        if (fromJson.status == CodeConstant.SERVICE_401) {// || fromJson.status == "500"
            val activity = ActivitiesHelper.get().lastActivity
            val intent = Intent(activity, LoginMobActivity::class.java)
            activity.startActivity(intent)
            throw Throwable(TipString.loginRetry)
        }
        KLog.e(TAG, "----------【Start】----------------")
        val method = request.method()
        if ("POST" == method) {
            // 请求参数
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
        KLog.d(TAG, "| Response:$content")
        KLog.e(TAG, "----------【End:" + duration + "毫秒】----------")
        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(mediaType, content))
            .build()
    }

    companion object {

        var TAG = "Http = "
    }
}
