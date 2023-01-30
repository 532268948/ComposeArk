package com.nsyw.nsyw_base.http

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.security.MessageDigest
import java.util.*

/**
 * @author qianjiang
 * @date   2023/1/28
 */
class VerifyInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder()
        builder.header("encryptType", "Android")
        //图片上传接口不加密
        val encodedPath = originalRequest.url.encodedPath
        if (encodedPath.needEncryptValue()) {
            val paramStr: String? = if ("GET" == originalRequest.method) {
                originalRequest.url.toUrl().query
            } else {
                val buffer = Buffer()
                originalRequest.body?.writeTo(buffer)
                buffer.readUtf8()
            }
            val encryptValue = if (paramStr?.isNotEmpty() == true) {
                md5(md5("GreatSale_Android_20") + md5(paramStr))
            } else ""
            builder.header("encryptValue", encryptValue)
        }
        val newRequest: Request = builder.build()
        return chain.proceed(newRequest)
    }

    private fun String.needEncryptValue(): Boolean {
        return !arrayOf("imgUpload", "fileUpload").any { this.contains(it) }
    }

    private fun md5(text: String): String {
        var sig = ""

        try {
            val messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.reset()
            messageDigest.update(text.toByteArray())

            val cipherData = messageDigest.digest()

            val formatter = Formatter()
            for (data in cipherData) {
                formatter.format("%02x", data)
            }

            sig = formatter.toString()
            formatter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return sig
    }
}