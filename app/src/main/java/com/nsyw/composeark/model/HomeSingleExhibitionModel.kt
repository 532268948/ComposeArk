package com.nsyw.composeark.model

import com.nsyw.base.utils.formatMoney
import com.nsyw.base.utils.getImageRatio
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.composeark.R
import com.nsyw.composeark.bean.HomeExhibitionBean
import com.nsyw.nsyw_base.ITypeModel

/**
 * @author qianjiang
 * @date   2023/1/29
 */
class HomeSingleExhibitionModel : ITypeModel {
    // 商品id
    var pItemId = 0L

    // 封面图片链接
    var imageUrl = ""

    // 会场名
    var exhibitionName = ""

    // 价格
    var price = ""

    // 佣金
    var commission = ""

    // 利益点
    var interestPoint = ""

    // 倒计时前缀
    var countDownPrefix = ""

    // 截止时间
    var endTime = 0L

    // 是否售罄
    var isSoldOut = false

    // 是否显示提前赚按钮
    var showPreEarnBtn = false

    // 是否提醒
    var isRemind = false

    // 路由
    var route = ""

    val tagList = mutableListOf<ITypeModel>()

    // 按钮背景图片
    var btnBgRes = R.drawable.home_btn_panic_buy

    companion object {
        fun convert(type: Int? = 0, exhibitionBean: HomeExhibitionBean): HomeSingleExhibitionModel {
            return HomeSingleExhibitionModel().apply {
                val firstGoods = exhibitionBean.indexPitemVO?.firstOrNull() ?: return@apply
                val exhibitionInfo = exhibitionBean.exhibitionInfoVO ?: return@apply
                pItemId = firstGoods.pitemId ?: 0L
                interestPoint = firstGoods.interestPoint.orEmpty()
                imageUrl = exhibitionInfo.homepageBanner?.toLoadUrl().orEmpty()
                exhibitionName = exhibitionInfo.exhibitionParkName.orEmpty()
                price = firstGoods.minShPrice?.formatMoney().orEmpty()
                countDownPrefix = exhibitionInfo.countDownTimePrefix.orEmpty()
                endTime = if ((exhibitionInfo.countDownTime ?: 0L) <= System.currentTimeMillis()) {
                    0L
                } else {
                    exhibitionInfo.countDownTime ?: 0L
                }
                // exhibitionType == 1 表示缓存的数据，实际上已经开始。
                if (exhibitionInfo.exhibitionType == 1 && type == 3) {
                    endTime = 0L
                }
                commission = "最多赚¥${firstGoods.totalCommission?.formatMoney().orEmpty()}"
                isRemind = exhibitionBean.exhibitionRemindStatus == 1
                isSoldOut = firstGoods.status == 3
                btnBgRes = when (type) {
                    3 -> {
                        if (isSoldOut) {
                            R.drawable.home_btn_sold_out
                        } else if (showPreEarnBtn) {
                            R.drawable.home_btn_pre_earn
                        } else {
                            if (isRemind) {
                                R.drawable.home_btn_already_remind
                            } else {
                                R.drawable.home_btn_redmind
                            }
                        }
                    }
                    else -> {
                        if (isSoldOut) {
                            R.drawable.home_btn_sold_out
                        } else {
                            R.drawable.home_btn_panic_buy
                        }
                    }
                }
                route = exhibitionInfo.linkUrl.orEmpty()
                exhibitionBean.titleTagList?.forEach {
                    tagList.add(
                        TagModel(
                            imageUrl = it.toLoadUrl(),
                            width = 16f * it.getImageRatio(),
                            height = 16f
                        )
                    )
                }
            }
        }
    }
}