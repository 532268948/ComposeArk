package com.nsyw.nsyw_base.http

import com.nsyw.nsyw_base.Constant
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author qianjiang
 * @date   2023/1/13
 */
private val LOGIN_API = arrayOf("/member/soul/wx/login", "/member/soul/mobile/login")

class ArkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val url = request.url
        val urlBuilder = url.newBuilder()
        val host = url.host
        val firstSegment = url.pathSegments.getOrNull(0)
        // 判断ark，防止漏修改。
        if (!host.contains("cdn") && (firstSegment == "ark" || firstSegment == "noah")) {
            urlBuilder.setPathSegment(0, "noah")
                .scheme("https")
                .host(Constant.FXJ_DOMAIN)
        }
        if (url.encodedPath in LOGIN_API) {
            urlBuilder.scheme("https")
                .host(Constant.FXJ_DOMAIN)
        }
        return chain.proceed(
            request = builder
                .url(urlBuilder.build())
                .build()
        )
    }
}