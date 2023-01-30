package com.nsyw.nsyw_base.http

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * @author qianjiang
 * @date   2023/1/10
 */
class GateWayDomainInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // 获取request
        val request = chain.request()
        val gatewayValue = request.headers(GetawayDomain.KEY_GATEWAY).firstOrNull()
        val gateway = GetawayDomain.getGateway(gatewayValue)
        // 从request中获取原有的HttpUrl实例oldHttpUrl
        if (gateway != null && gateway.isNotEmpty()) {
            // 获取request的创建者builder
            val builder = request.newBuilder()
            // 匹配获得新的BaseUrl
            val newBaseUrl: HttpUrl = gateway.toHttpUrlOrNull() ?: return chain.proceed(request)

            // 重建新的HttpUrl，修改需要修改的url部分
            val newFullUrl = request.url
                .newBuilder()
                // 更换网络协议
                .scheme(newBaseUrl.scheme)
                // 更换主机名
                .host(newBaseUrl.host)
                // 更换端口
                .port(newBaseUrl.port)
                .build()
            // 重建这个request，通过builder.url(newFullUrl).build()；
            // 然后返回一个response至此结束修改
            return chain.proceed(builder.url(newFullUrl).build())
        }
        return chain.proceed(request)
    }
}