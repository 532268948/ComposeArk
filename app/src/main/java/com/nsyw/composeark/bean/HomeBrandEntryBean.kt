package com.nsyw.composeark.bean

/**
 * @author qianjiang
 * @date   2023/1/16
 */
data class HomeBrandEntryBean(
    val brandList: List<HomeBrandBean>?,
    val nextStartIndex: Long?,
)

data class HomeBrandBean(
    // 0:普通样式；1:单品爆款样式
    val showStyle: Int?,
    val brandInfoVO: BrandInfoBean?,
    val indexPitemVOS: List<HomeBrandGoodsBean>?,
    // 营销标签
    val marketLabel: List<String>?,
    // 【佣金翻倍】、【大牌】、【进口】、【首发】、【上新】、【清仓】
    val titleTagList: List<String>?,
)

data class BrandInfoBean(
    val brandLogo: String?,
    val brandId: Long?,
    val brandName: String?,
    val countDownTime: Long?,
    val linkUrl: String?,
    val raisePriceLabelAppUrl: String?,
    val exhibitionParkName: String?,
    val homepageBanner: String?,
    val countDownTimePrefix: String?,
    val delisted: Boolean?,
    val exhibitionId: Long?,
    val exhibitionIds: List<Long>?,
    val exhibitionType: Int?,
    val exhibitionRemindStatus: Int?,
)

data class HomeBrandGoodsBean(
    val pitemId: Long?,
    val minShPrice: Long?,
    val headPicture: String?,
    val pitemRoute: String?,
    val interestPoint: String?,
    val status: Int?,
    val activityLabelUrl: String?,
    val doubleCommissionFlag: Boolean?,
    val totalCommission: Long = 0L,
)