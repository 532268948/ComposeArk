package com.nsyw.nsyw_base.http

import android.annotation.SuppressLint
import android.os.Build
import com.nsyw.nsyw_base.Constant
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * @author qianjiang
 * @date   2022/12/20
 */
object RetrofitHelper {
    private const val CONNECT_TIMEOUT: Long = 15
    private const val BASE_URL = Constant.BASE_URL

    private lateinit var retrofit: Retrofit

    private fun buildOkHttpClient(interceptors: List<Interceptor>): OkHttpClient {

        val builder = OkHttpClient.Builder()
            .connectTimeout(
                CONNECT_TIMEOUT,
                TimeUnit.SECONDS
            )
            .retryOnConnectionFailure(true)
            .hostnameVerifier(TrustAllNameVerifier())
        if (Build.VERSION.SDK_INT < 29) {
            //不验证证书
            builder.sslSocketFactory(createSSLSocketFactory(),TrustAllCerts())
        }
        interceptors.forEach { interceptor -> builder.addInterceptor(interceptor) }
        return builder.build()
    }

    fun <T> createService(api: Class<T>): T {
        return retrofit.create(api)
    }

    fun init(interceptors: List<Interceptor>) {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildOkHttpClient(interceptors))
            .build()
    }

    private fun createSSLSocketFactory(): SSLSocketFactory {
        lateinit var ssfFactory: SSLSocketFactory
        try {
            val sslFactory = SSLContext.getInstance("TLS")
            sslFactory.init(null, arrayOf(TrustAllCerts()), SecureRandom());
            ssfFactory = sslFactory.socketFactory
        } catch (e: Exception) {
            print("SSL错误：${e.message}")
        }
        return ssfFactory
    }

}

class TrustAllNameVerifier : HostnameVerifier {
    @SuppressLint("BadHostnameVerifier")
    override fun verify(hostname: String?, session: SSLSession?): Boolean = true
}

@SuppressLint("CustomX509TrustManager")
class TrustAllCerts : X509TrustManager {

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    @SuppressLint("TrustAllX509TrustManager")
    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
}