package com.ty.voogla.net

import android.content.Intent
import com.google.gson.Gson
import com.socks.library.KLog
import com.ty.voogla.base.ResponseInfo
import com.ty.voogla.constant.CodeConstant
import com.ty.voogla.constant.TipString
import com.ty.voogla.ui.ActivitiesHelper
import com.ty.voogla.ui.activity.LoginMobActivity
import okhttp3.Interceptor
import java.io.IOException

/**
 * @author TY
 *
 *
 * okhttp3 日志拦截器
 * 网络连接失败 : 401 状态 拦截
 */
class LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        val response = chain.proceed(chain.request())

        val mediaType = response.body()!!.contentType()
        // 响应数据
        val content = response.body()!!.string()

        val fromJson = Gson().fromJson(content, ResponseInfo::class.java)
        if (fromJson.status == CodeConstant.SERVICE_401) {// || fromJson.status == "500"
            val activity = ActivitiesHelper.get().lastActivity
            val intent = Intent(activity, LoginMobActivity::class.java)
            activity.startActivity(intent)
            ActivitiesHelper.get().finishAll()
            throw Throwable(TipString.loginRetry)
        }
        KLog.json(TAG, content)
        return response.newBuilder()
            .body(okhttp3.ResponseBody.create(mediaType, content))
            .build()
    }

    companion object {

        var TAG = "Http = "
    }
}
