package com.nsyw.composeark.bean

/**
 * @author qianjiang
 * @date   2023/1/12
 */
data class MarketingModuleDetailBean(
    // 页面楼层详情
    val rows: List<FloorEntryBean>? = null
)

class FloorEntryBean(
    val floorAttr: FloorAttrBean? = null,
    val ruleSetList: List<RuleSetDataBean>? = null
)

class FloorAttrBean(
    /**
     * 模块唯一标识
     * index_hotStyle  高佣爆款
     * index_marketActivity 限时抢购、蜂享排行榜
     * index_activityZone 新人专区
     * index_makeMoneyZone 赚钱专区
     */
    val code: String? = null
)

class RuleSetDataBean(
    val ruleSetAttr: RuleSetAttrBean? = null,
    val resourceBitList: List<ResourceBitAttrEntryBean>? = null
)

data class RuleSetAttrBean(
    val show: String? = null,
    // 高佣爆款
    val title: String? = null,
    val titleUrl: String? = null,
    val newTitleUrl: String? = null,
    val introduction: String? = null,
    val route: String? = null,
    val tagUrl: String? = null,
    val countdown: Long? = null,
    val zoneType: Int? = null,
    val bgImageUrl: String? = null,
    val newBgImageUrl: String? = null,
    /**
     * 107 限时抢购
     * 108 蜂享排行榜
     */
    var ruleSetType: Int? = null,
)

class ResourceBitAttrEntryBean(
    val resourceBitAttr: ResourceBitAttrBean? = null
)

class ResourceBitAttrBean(
    // 商品id
    val pitemid: Long? = null,
    // 售价
    val pitemSalePrice: Long? = null,
    // 原价
    val pitemOriginalPrice: Long? = null,
    // 商品图片
    val pitemHeadPicture: String? = null,
    // 商品路由
    val route: String? = null,
    // 标签
    val pitemLabelList: String? = null,
    val pitemCommission: Long? = null,
    val sellingPoint: String? = null,

    // 图片角标地址
    val pictureBadge: String? = null,
    val buttonUrl: String? = null,
)