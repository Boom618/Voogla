package com.ty.voogla.net;

import com.ty.voogla.constant.CodeConstant;
import com.ty.voogla.util.SimpleCache;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;


/**
 * @author TY on 2018/12/14.
 * <p>
 * sessionId 头部 拦截器
 */
public class SessionInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

//        String sessionId = "";
//        try {
//            sessionId = SimpleCache.getString(CodeConstant.SESSION_ID_KEY);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Request authorised = originalRequest.newBuilder()
                .header(CodeConstant.SESSION_ID_KEY, "27DPMUT_YKcGemZW5glwaDpaAxRPBfhZ")
                .header(CodeConstant.SYSTEM_KEY, CodeConstant.SYSTEM_VALUE)
                .build();
        return chain.proceed(authorised);
    }
}
