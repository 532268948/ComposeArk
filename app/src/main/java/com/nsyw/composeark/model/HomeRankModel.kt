package com.nsyw.composeark.model

import com.nsyw.base.utils.toLoadUrl
import com.nsyw.composeark.bean.RuleSetDataBean

/**
 * @author qianjiang
 * @date   2023/1/13
 */
class HomeRankModel {
    var visible: Boolean = false
    var titleUrl = ""
    var introduction: String = ""
    private val goodsList: MutableList<HomeRankGoodsModel> = mutableListOf()
    private val emptyGoods by lazy { HomeRankGoodsModel() }
    val goods1 get() = goodsList.getOrNull(0) ?: emptyGoods
    val goods2 get() = goodsList.getOrNull(1) ?: emptyGoods
    var route = ""

    companion object {
        fun convert(ruleBean: RuleSetDataBean): HomeRankModel {
            return HomeRankModel().apply {
                val hotListPitemVOList = ruleBean.resourceBitList
                visible = ruleBean.ruleSetAttr?.show=="1" && !hotListPitemVOList.isNullOrEmpty()
                route = ruleBean.ruleSetAttr?.route.orEmpty()
                titleUrl = ruleBean.ruleSetAttr?.titleUrl?.toLoadUrl().orEmpty()
                introduction = ruleBean.ruleSetAttr?.introduction.orEmpty()
                hotListPitemVOList?.let {
                    val goodsList = arrayListOf<HomeRankGoodsModel>()
                    val maxPosition = 2.coerceAtMost(it.size)
                    val subList = it.subList(0, maxPosition)
                    subList.forEach { bean ->
                        goodsList.add(
                            HomeRankGoodsModel(
                                imageUrl = bean.resourceBitAttr?.pitemHeadPicture.orEmpty().toLoadUrl(),
                                saleCountDesc = bean.resourceBitAttr?.pitemLabelList.orEmpty()
                            )
                        )
                    }
                    this.goodsList.addAll(goodsList)
                }
            }
        }
    }
}

class HomeRankGoodsModel(
    var imageUrl: String = "",
    var saleCountDesc: String = ""
) {
    val show get() = imageUrl.isNotEmpty()
}