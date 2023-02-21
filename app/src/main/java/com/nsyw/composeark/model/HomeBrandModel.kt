package com.nsyw.composeark.model

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.nsyw.base.utils.formatMoney
import com.nsyw.base.utils.getImageRatio
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.composeark.bean.HomeBrandBean
import com.nsyw.composeark.bean.HomeBrandEntryBean
import com.nsyw.nsyw_base.ITypeModel

/**
 * @author qianjiang
 * @date   2023/1/16
 */
class HomeBrandModel : ITypeModel {

    // 会场。
    var brandId: Long = 0L
    var exhibitionId: Long = 0L
    var logoUrl = ""
    var title = ""
    var exhRoute: String = ""

    // 是否下架
    var delisted: Boolean = false

    val tagList: MutableList<TagModel> = mutableListOf()

    // 品牌标签。
    var tag1 = ""
    var tag2 = ""

    // 倒计时。
    var endTime: Long = 0L
    var isBegin: Boolean = false
    var countDownPrefix = ""

    // 是否显示分享赚钱
    var showShareEarn = false

    var addPriceFlag = ""

    val goodsList: MutableList<ITypeModel> = mutableListOf()

    /**
     * 按钮类型
     * 0 分享
     * 1 预售 未提醒
     * 2 预售 已提醒
     */
    var btnType: Int = 0

    companion object {
        fun convert(type: Int? = 0, brandBean: HomeBrandBean): HomeBrandModel {
            return HomeBrandModel().apply {
                val brandInfo = brandBean.brandInfoVO ?: return@apply

                exhibitionId = brandInfo.exhibitionId ?: 0L
                logoUrl = brandInfo.brandLogo?.toLoadUrl().orEmpty()
                title = brandInfo.brandName.orEmpty()
                exhRoute = brandInfo.linkUrl.orEmpty()
                delisted = brandInfo.delisted == true

                brandBean.titleTagList?.forEach { url ->
                    tagList.add(
                        TagModel(
                            imageUrl = url.toLoadUrl(), width = 16f * url.getImageRatio(),
                            height = 16f
                        )
                    )
                }

                // tag 取前两个。
                tag1 = brandBean.marketLabel?.getOrNull(0).orEmpty()
                tag2 = brandBean.marketLabel?.getOrNull(1).orEmpty()

                endTime = if ((brandInfo.countDownTime ?: 0L) <= System.currentTimeMillis()) {
                    0L
                } else {
                    brandInfo.countDownTime ?: 0L
                }
                countDownPrefix = brandInfo.countDownTimePrefix.orEmpty()

                showShareEarn = isBegin

                // 加价会场标签
                addPriceFlag = brandInfo.raisePriceLabelAppUrl?.toLoadUrl().orEmpty()

                // 标题打标
                brandBean.titleTagList?.forEach {
                    tagList.add(
                        TagModel(
                            imageUrl = it.toLoadUrl(),
                            width = 16f * it.getImageRatio(),
                            height = 16f
                        )
                    )
                }
                btnType = when {
                    type == 3 && brandInfo.exhibitionRemindStatus == 1 -> {
                        2
                    }
                    type == 3 && brandInfo.exhibitionRemindStatus != 1 -> {
                        1
                    }
                    else -> 0
                }
                val list = brandBean.indexPitemVOS?.map {
                    HomeExhibitionGoodsModel(
                        goodsId = it.pitemId ?: 0L,
                        imageUrl = it.headPicture.toLoadUrl(),
                        price = it.minShPrice?.formatMoney().orEmpty(),
                        commission = "最多赚${it.totalCommission.formatMoney()}",
                        goodsRoute = it.pitemRoute.orEmpty()
                    )
                }
                if (!list.isNullOrEmpty()) {
                    goodsList.addAll(list)
                    if (list.size >= 10) {
                        goodsList.add(HomeGoodsMoreModel(route = brandInfo.linkUrl.orEmpty()))
                    }
                }
            }
        }
    }
}