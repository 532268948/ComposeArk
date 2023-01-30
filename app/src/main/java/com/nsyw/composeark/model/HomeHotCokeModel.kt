package com.nsyw.composeark.model

import com.nsyw.base.utils.formatMoney
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.composeark.bean.FloorEntryBean
import com.nsyw.nsyw_base.ITypeModel
import com.nsyw.nsyw_base.utils.PriceUtil

/**
 * @author qianjiang
 * @date   2023/1/13
 */
class HomeHotCokeModel : ITypeModel {
    var visible: Boolean = false
    var titleUrl: String = ""
    var route: String = ""
    var pitemList: List<HomeHotCokeItemModel>? = null
    val showMore: Boolean get() = route.isNotEmpty()

    companion object {
        fun convert(bean: FloorEntryBean): HomeHotCokeModel {
            val model = HomeHotCokeModel()
            if (!bean.ruleSetList.isNullOrEmpty()) {
                val hotCake = bean.ruleSetList[0]
                model.visible = hotCake.ruleSetAttr?.show == "1"
                model.titleUrl =
                    hotCake.ruleSetAttr?.titleUrl?.toLoadUrl().orEmpty()
                model.route = hotCake.ruleSetAttr?.route.orEmpty()
                model.pitemList = hotCake.resourceBitList?.mapIndexed { index, it ->
                    HomeHotCokeItemModel().apply {
                        this.pitemId = it.resourceBitAttr?.pitemid ?: 0
                        this.picture =
                            it.resourceBitAttr?.pitemHeadPicture.orEmpty()
                        this.route = it.resourceBitAttr?.route.orEmpty()
                        this.pictureBadge =
                            it.resourceBitAttr?.pictureBadge.orEmpty()
                        this.sellingPoint = "最多赚${
                            (it.resourceBitAttr?.pitemCommission
                                ?: 0).formatMoney()
                        }"
                        this.buttonUrl = it.resourceBitAttr?.buttonUrl.orEmpty()

                        val price = it.resourceBitAttr?.pitemSalePrice ?: 0
                        this.price = "¥${PriceUtil.getPrice(price).orEmpty()}"

                        val originPrice =
                            it.resourceBitAttr?.pitemOriginalPrice ?: 0
                        this.originPrice = "¥${PriceUtil.getPrice(originPrice)}"
                    }
                }
            }
            return model
        }
    }
}

class HomeHotCokeItemModel : ITypeModel {
    var pitemId: Long = 0

    // 商品图片
    var picture: String = ""

    // 售价
    var price: String = ""

    // 原价
    var originPrice: String = ""

    // 商品路由
    var route: String = ""

    // 角标
    var pictureBadge: String = ""

    val pictureBadgeShown get() = pictureBadge.isNotEmpty()

    // 最大佣金
    var sellingPoint: String = ""

    // 按钮
    var buttonUrl: String = ""
}