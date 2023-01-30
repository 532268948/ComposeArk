package com.nsyw.nsyw_base.http

import androidx.annotation.Keep

/**
 * @author qianjiang
 * @date   2022/12/20
 */
@Keep
data class PageResponse<T>(
    val datas: List<T>?,
    val curPage: Int?,
    val offset: Int?,
    val over: Boolean?,
    val pageCount: Int?,
    val size: Int?,
    val total: Int?,
)
