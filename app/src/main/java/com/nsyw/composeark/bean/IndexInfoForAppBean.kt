package com.nsyw.composeark.bean

/**
 * @author qianjiang
 * @date   2023/1/13
 */
data class IndexInfoForAppBean(
    // tab信息。
    val typeBannerVOList: List<TypeBannerVOBean>?,
    // 品牌筛选器路由地址。
    val categoryRoute: String?,
)