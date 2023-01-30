package com.nsyw.nsyw_base.http

import androidx.annotation.Keep

/**
 * @author qianjiang
 * @date   2022/12/20
 */
@Keep
data class HttpResponse<T>(
    val status: Boolean = true,
    val message: String? = "",
    val responseCode: Int = 0,
    val count: Int = 0,
    //兼容老版本： 0 没有 1 有
    val hasNext: Int = 0,
    val entry: T? = null,
)