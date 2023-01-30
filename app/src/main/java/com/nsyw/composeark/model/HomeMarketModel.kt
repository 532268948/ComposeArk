package com.nsyw.composeark.model

import com.nsyw.composeark.bean.MarketingModuleDetailBean
import com.nsyw.nsyw_base.ITypeModel

/**
 * @author qianjiang
 * @date   2023/1/12
 */
class HomeMarketModel(
    val list: MutableList<ITypeModel> = mutableListOf()
) {

    val visible: Boolean get() = list.isNotEmpty()

    companion object {
        fun convert(bean: MarketingModuleDetailBean): HomeMarketModel {
            val list: MutableList<ITypeModel> = mutableListOf()
            bean.rows?.forEach { floor ->
                when (floor.floorAttr?.code) {
                    "index_hotStyle" -> {
                        // 高佣爆款
                        list.add(HomeHotCokeModel.convert(floor))
                    }
                    "index_marketActivity" -> {
                        // 限时抢购、蜂享排行榜
                        if (!floor.ruleSetList.isNullOrEmpty()) {
                            val model = HomeSecKillAndRankModel()
                            floor.ruleSetList.forEach { ruleSetBean ->
                                when (ruleSetBean.ruleSetAttr?.ruleSetType) {
                                    107 -> {
                                        // 限时抢购
                                        model.visible
                                        model.homeSecKillModel =
                                            HomeSecKillModel.convert(ruleSetBean)
                                    }
                                    108 -> {
                                        // 蜂享排行榜
                                        model.visible
                                        model.homeRankModel = HomeRankModel.convert(ruleSetBean)
                                    }
                                }
                            }
                            list.add(model)
                        }
                    }
                    "index_activityZone", "index_makeMoneyZone" -> {
                        // 新人/赚钱专区

                    }
                }
            }
            return HomeMarketModel(list = list)
        }
    }
}