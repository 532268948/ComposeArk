package com.nsyw.composeark.bean

/**
 * @author qianjiang
 * @date   2023/1/16
 */
data class HomeExhibitionEntryBean(
    // 会场列表。
    val indexExhibitionInfoVOList: List<HomeExhibitionBean>?,
    // 类型。
    val type: Int?,
)

data class HomeExhibitionBean(
    // 会场。
    val exhibitionInfoVO: ExhibitionInfoBean?,
    // 提醒状态。
    val exhibitionRemindStatus: Int?,
    // 标签列表。
    val exhibitionParkLabel: List<String>?,
    // 商品列表。
    val indexPitemVO: List<ExhibitionGoodsInfoBean>?,
    // 活动信息。
    val activeVO: ActivityInfoBean?,
    // 展示样式 0 普通会场 1 单品爆款会场
    val showStyle: Int?,
    val titleTagList: ArrayList<String>?,
)

data class ExhibitionInfoBean(
    // logo。
    val brandLogo: String?,
    // 名称。
    val brandName: String?,
    // 倒计时。
    val countDownTime: Long?,
    // ID。
    val exhibitionParkId: Long?,
    // 类型。
    val exhibitionType: Int?,
    // 路由。
    val linkUrl: String?,
    // 会场名称
    val exhibitionParkName: String?,
    // 会场爆款图
    val homepageBanner: String?,
    // 是否满足提前购条件：提前购会场+VIP
    val preEarnFlag: Boolean?,
    // 提前购开始（时间满足）
    val preEarnEnable: Boolean?,
    // 倒计时前缀：专业版距开始/专业版可立即购买
    val countDownTimePrefix: String?,
    val fullMinusFlag: Boolean?,
    val fullMinusIcon: String?,
    // 每满200-30
    val fullMinusName: String?,
    // 是否为下架状态
    val delisted: Boolean?,
)

data class ExhibitionGoodsInfoBean(
    // 佣金。
    val commission: Long?,
    // 图片。
    val headPicture: String?,
    // 价格。
    val minShPrice: Long?,
    // ID。
    val pitemId: Long?,
    // 路由。
    val pitemRoute: String?,
    // 利益点
    val interestPoint: String?,
    // 商品状态
    val status: Int?,
    // 是否参与双倍佣金的标志 true展示 false不展示
    val doubleCommissionFlag: Boolean?,
    // 双倍佣金金额
    val doubleCommission: Long?,
    // 最多赚
    val totalCommission: Long?,
)

data class ActivityInfoBean(
    // 标题。
    val title: String?,
    // 类型（2：掌柜活动）。
    val type: Int?,
    // 佣金活动的标签图片url（目前是双倍佣金活动）
    val activityLabelUrl: String?,
    // 加价会场图片url
    val raisePriceLabelUrl: String?,
    // 双倍佣金Flag
    val doubleCommissionFlag: Boolean?,
)

