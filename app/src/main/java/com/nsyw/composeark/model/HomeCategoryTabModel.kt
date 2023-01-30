package com.nsyw.composeark.model

/**
 * @author qianjiang
 * @date   2023/1/10
 */
class HomeCategoryTabModel(
    val name: String,
    // 1:活动类目-图片,2:活动类目-文案,3:预告,4:常规类目 0:精选
    val tabType: Int,
    // 1 当前页打开 2 跳转新页面
    val linkType:Int,
    // 跳转地址
    val linkUrl: String? = null,
    // 是否选中
    val check:Boolean=false,
)