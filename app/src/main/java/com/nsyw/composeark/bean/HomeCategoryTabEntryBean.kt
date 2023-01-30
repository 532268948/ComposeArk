package com.nsyw.composeark.bean

/**
 * @author qianjiang
 * @date   2023/1/10
 */
data class HomeCategoryTabEntryBean(
    val rows: List<HomeCategoryTabBean>? = null,
)

data class HomeCategoryTabBean(
    // 一级营销类目ID。
    val categoryId: Long? = null,
    // 一级营销类目名称。
    val name: String? = null,
    // 一级营销类目Icon。
    val icon: String? = null,
    // 1:活动类目-图片,2:活动类目-文案,3:预告,4:常规类目
    val type: Int? = null,
    // 跳转地址。
    val linkUrl: String? = null,
    // 1 当前页打开 2 跳转新页面
    val interactionType: Int = 0,
)