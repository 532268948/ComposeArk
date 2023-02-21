package com.nsyw.composeark.model

import com.nsyw.base.utils.formatMoney
import com.nsyw.base.utils.getImageRatio
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.composeark.bean.HomeExhibitionBean
import com.nsyw.composeark.bean.HomeExhibitionEntryBean
import com.nsyw.nsyw_base.ITypeModel

/**
 * @author qianjiang
 * @date   2023/1/16
 */
class HomeExhibitionModel : ITypeModel {
    // 活动。
    var showActivity: Boolean = false
    var activityDesc: String = ""
    var addPriceFlag = ""

    // 会场。
    var exhibitionId: Long = 0L
    var logoUrl = ""
    var title = ""
    var exhRoute: String = ""

    // 是否下架
    var delisted: Boolean = false

    val tagList: MutableList<TagModel> = mutableListOf()

    // 满减
    var fullMinusFlag: Boolean = false
    var fullMinusIcon: String = ""

    // 每满200-30
    var fullMinusName: String = ""

    // 品牌标签。
    var tag1 = ""
    var tag2 = ""
    val showTag2: Boolean get() = tag2.isNotEmpty() && !fullMinusFlag

    // 倒计时。
    var endTime: Long = 0L
    var isBegin: Boolean = false
    var showCountDown: Boolean = false
    var countDownPrefix = ""

    /**
     * 按钮类型
     * 0 分享
     * 1 预售 未提醒
     * 2 预售 已提醒
     */
    var btnType: Int = 0

    val goodsList: MutableList<ITypeModel> = mutableListOf()

    companion object {
        fun convert(
            type: Int? = 0,
            homeExhibitionBean: HomeExhibitionBean
        ): HomeExhibitionModel {
            return HomeExhibitionModel().apply {
                showActivity = homeExhibitionBean.activeVO?.type == 2
                activityDesc = homeExhibitionBean.activeVO?.title.orEmpty()
                // 加价会场标签
                addPriceFlag =
                    homeExhibitionBean.activeVO?.raisePriceLabelUrl?.toLoadUrl().orEmpty()


                val exhibitionInfoBean = homeExhibitionBean.exhibitionInfoVO
                val activityInfoBean = homeExhibitionBean.activeVO
                exhibitionInfoBean?.let {
                    exhibitionId = it.exhibitionParkId ?: 0L
                    logoUrl = it.brandLogo?.toLoadUrl().orEmpty()
                    title = it.brandName.orEmpty()
                    exhRoute = it.linkUrl.orEmpty()
                    delisted = it.delisted == true

                    if (activityInfoBean != null && activityInfoBean.doubleCommissionFlag == true) {
                        val url = activityInfoBean.activityLabelUrl.toLoadUrl()
                        tagList.add(
                            TagModel(
                                imageUrl = url,
                                width = 16f * url.getImageRatio(),
                                height = 16f
                            )
                        )
                    }
                    homeExhibitionBean.titleTagList?.forEach { url ->
                        tagList.add(
                            TagModel(
                                imageUrl = url.toLoadUrl(),
                                width = 16f * url.getImageRatio(),
                                height = 16f
                            )
                        )
                    }

                    fullMinusFlag = it.fullMinusFlag ?: false
                    fullMinusIcon = it.fullMinusIcon?.toLoadUrl().orEmpty()
                    fullMinusName = it.fullMinusName.orEmpty()

                    // tag 取前两个。
                    tag1 = homeExhibitionBean.exhibitionParkLabel?.getOrNull(0).orEmpty()
                    tag2 = homeExhibitionBean.exhibitionParkLabel?.getOrNull(1).orEmpty()
                    // 是否开启提醒
                    val isRemind = homeExhibitionBean.exhibitionRemindStatus == 1

                    endTime = if ((it.countDownTime ?: 0L) <= System.currentTimeMillis()) {
                        0L
                    } else {
                        it.countDownTime ?: 0L
                    }
                    countDownPrefix = it.countDownTimePrefix.orEmpty()

                    val showPreEarn = type == 3 && it.preEarnFlag == true
                    if (showPreEarn) {
                        // 提前购只显示一个tag，将tag2置空
                        tag2 = ""
                    }
                    // exhibitionType == 1 表示缓存的数据，实际上已经开始。
                    if (it.exhibitionType == 1 && type == 3) {
                        endTime = 0L
                    }
                    btnType = when {
                        type == 3 && it.preEarnEnable == true && isRemind -> {
                            2
                        }
                        type == 3 && it.preEarnEnable == true && !isRemind -> {
                            1
                        }
                        else -> 0
                    }

                }
                val list = homeExhibitionBean.indexPitemVO?.map {
                    HomeExhibitionGoodsModel(
                        goodsId = it.pitemId ?: 0L,
                        imageUrl = it.headPicture.toLoadUrl(),
                        price = it.minShPrice?.formatMoney().orEmpty(),
                        commission = "最多赚${it.totalCommission?.formatMoney().orEmpty()}",
                        goodsRoute = it.pitemRoute.orEmpty()
                    )
                }?.toMutableList()?: mutableListOf()
                goodsList.addAll(list)
                if(goodsList.size>6){
                    goodsList.subList(0,6)
                    goodsList.add(HomeGoodsMoreModel(route = exhRoute))
                }
            }
        }
    }
}

class HomeExhibitionGoodsModel(
    // 商品。
    var goodsId: Long = 0L,
    var imageUrl: String = "",
    var price: String = "",
    var commission: String = "",
    var isFirst: Boolean = false,

    // 路由。
    var goodsRoute: String = "",
):ITypeModel