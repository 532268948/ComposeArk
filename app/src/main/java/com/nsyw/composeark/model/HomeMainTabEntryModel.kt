package com.nsyw.composeark.model

import com.nsyw.composeark.bean.IndexInfoForAppBean
import com.nsyw.nsyw_base.ITypeModel

/**
 * @author qianjiang
 * @date   2023/1/13
 */
class HomeMainTabEntryModel : ITypeModel {

    val list: MutableList<HomeMainTabModel> = mutableListOf()

    companion object {
        fun convert(bean: IndexInfoForAppBean): HomeMainTabEntryModel {
            return HomeMainTabEntryModel().apply {
                bean.typeBannerVOList?.forEach { banner ->
                    list.add(
                        HomeMainTabModel(
                            type = banner.type ?: 0,
                            title = banner.title.orEmpty(),
                            subTitle = banner.description.orEmpty(),
                            marketCategoryId = banner.marketCategoryId
                        )
                    )
                }
            }
        }
    }
}

class HomeMainTabModel(
    val type: Int = 0,
    val title: String,
    val subTitle: String,
    val marketCategoryId: Long?,
)