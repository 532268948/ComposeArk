package com.nsyw.nsyw_base.widget.banner

/**
 * @author qianjiang
 * @date   2023/1/11
 */
data class BannerData(
    val imgUrl: String,
    /**
     * 0 h5
     * 1 路由跳转
     */
    val linkType: Int,
    val linkUrl: String,
    val extData: String? = null,
)