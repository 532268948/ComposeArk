package com.nsyw.composeark.bean

/**
 * @author qianjiang
 * @date   2023/1/13
 */
data class TypeBannerVOBean(
    // 类型。1最后疯抢，2今日特卖，3即将开售，8前台类目
    val type: Int?,
    // 标题
    val title: String? = null,
    // 描述。
    val description: String? = null,
    // 类目id
    val marketCategoryId: Long? = null,
)