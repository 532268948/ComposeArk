package com.nsyw.nsyw_base.http

import android.os.Build
import com.nsyw.nsyw_base.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author qianjiang
 * @date   2022/12/20
 */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newBuilder = originalRequest.newBuilder()
//        newBuilder.addHeader("aifocus-cookie", TokenCacheUtil.getToken())
        newBuilder.addHeader("aifocus-cookie", "323_d0ce944fd19268c9d4199d172c961aa1")
        newBuilder.addHeader("User-Agent", getUA())
        newBuilder.addHeader("version", BuildConfig.VERSION_NAME)
        newBuilder.addHeader("platformVersion", Build.VERSION.RELEASE)
        newBuilder.addHeader("code-named", "noah")
        newBuilder.addHeader("channel", "official")
        return chain.proceed(newBuilder.build())
    }

    companion object {
        private const val NAME_SPACE = "ARK"
        private const val OS = "Android"

        fun getUA(): String {
            return "$NAME_SPACE/${BuildConfig.VERSION_NAME} ($OS${Build.VERSION.RELEASE})"
        }
    }

}