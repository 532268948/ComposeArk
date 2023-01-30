package com.nsyw.composeark.model

import com.nsyw.base.utils.formatMoney
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.composeark.bean.RuleSetDataBean
import com.nsyw.nsyw_base.ITypeModel

/**
 * @author qianjiang
 * @date   2023/1/13
 */
class HomeSecKillModel : ITypeModel {
    // 标题：限时秒杀
    var title = ""
    var titleUrl = ""

    // 模块描述
    var introduction = ""

    // 倒计时截止时间
    var endTime = 0L

    // 跳转路由
    var route = ""

    // 模块是否显示
    var show: Boolean = false

    val goodsList: MutableList<HomeHelpOrderGoodsModel> = mutableListOf()

    var tagUrl: String = ""

    val goodsOne get() = goodsList.getOrNull(0) ?: HomeHelpOrderGoodsModel()
    val goodsTwo  get() = goodsList.getOrNull(1) ?: HomeHelpOrderGoodsModel()

    companion object {
        fun convert(bean: RuleSetDataBean): HomeSecKillModel {
            return HomeSecKillModel().apply {
                val pItemList = bean.resourceBitList
                show = bean.ruleSetAttr?.show == "1" && !pItemList.isNullOrEmpty()
                title = bean.ruleSetAttr?.title.orEmpty()
                titleUrl = bean.ruleSetAttr?.titleUrl?.toLoadUrl().orEmpty()
                introduction = bean.ruleSetAttr?.introduction.orEmpty()
                endTime = (System.currentTimeMillis() + (bean.ruleSetAttr?.countdown ?: 0))
                route = bean.ruleSetAttr?.route.orEmpty()
                tagUrl = bean.ruleSetAttr?.tagUrl?.toLoadUrl().orEmpty()
                pItemList?.let {
                    val maxPosition = 2.coerceAtMost(it.size)
                    val subList = it.subList(0, maxPosition)
                    subList.forEach { bean ->
                        goodsList.add(
                            HomeHelpOrderGoodsModel(
                                pItemId = bean.resourceBitAttr?.pitemid,
                                imageUrl = bean.resourceBitAttr?.pitemHeadPicture?.toLoadUrl()
                                    .orEmpty(),
                                price = bean.resourceBitAttr?.pitemSalePrice?.formatMoney()
                                    .orEmpty(),
                                originPrice = bean.resourceBitAttr?.pitemOriginalPrice?.formatMoney()
                                    .orEmpty(),
                                route = bean.resourceBitAttr?.route.orEmpty()
                            )
                        )
                    }
                }
            }
        }
    }
}

class HomeHelpOrderGoodsModel(
    var pItemId: Long? = null,
    var imageUrl: String = "",
    var price: String = "",
    var originPrice: String = "",
    // 助你开单
    var route: String = ""
) {
    val show: Boolean get() = imageUrl.isNotEmpty()
}