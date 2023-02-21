package com.nsyw.composeark.model

import androidx.compose.ui.graphics.Color
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.composeark.ArkApp
import com.nsyw.composeark.bean.AtmosphereAndCapsuleBean
import com.nsyw.composeark.bean.CapsuleFloorAttrBean
import com.nsyw.composeark.bean.CapsuleFloorBean
import com.nsyw.composeark.bean.CapsuleResourceBitAttrBean
import com.nsyw.nsyw_base.ITypeModel
import com.nsyw.nsyw_base.theme.White
import com.nsyw.nsyw_base.widget.banner.BannerData

/**
 * @author qianjiang
 * @date   2023/1/10
 */
class HomeCapsuleModel {

    var visible: Boolean = false

    val list: MutableList<ITypeModel> = mutableListOf()

    companion object {
        fun convert(bgColor: Color, bean: AtmosphereAndCapsuleBean?): HomeCapsuleModel {
            val homeCapsuleModel = HomeCapsuleModel()
            if (bean == null) return homeCapsuleModel
            bean.floorList?.forEach { floor ->
                val floorAttrBean = floor.floorAttr
                floorAttrBean?.let { attr ->
                    when (attr.componentType) {
                        "1" -> {
                            // 图片
                            homeCapsuleModel.visible = true
                            val resources = floor.resourceBitList
                            if (!resources.isNullOrEmpty()) {
                                if (resources.size == 1) {
                                    val resource = resources[0].resourceBitAttr
                                    resource?.let {
                                        homeCapsuleModel.list.add(
                                            HomeCapsuleImageModel.convert(
                                                bgColor = bgColor,
                                                attr = attr,
                                                bean = resource
                                            )
                                        )
                                    }
                                } else {
                                    homeCapsuleModel.list.add(
                                        HomeCapsuleBannerModel.convert(
                                            bgColor = bgColor,
                                            bean = floor
                                        )
                                    )
                                }
                            }
                        }
                        "9" -> {
                            homeCapsuleModel.visible = true
                            homeCapsuleModel.list.add(
                                HomeCapsuleHotAreaModel.convert(bgColor = bgColor, floor = floor)
                            )
                            // 热区
                        }
                    }
                }
            }
            return homeCapsuleModel
        }
    }
}

class HomeCapsuleImageModel(
    val cmsFloorId: String,
    val cmsBlockId: String,
    val radius: Float,
    val sideMargin: Float,
    val topMargin: Float,
    val bgColor: Color,
    val imageUrl: String,
    val ratio: Float,
    val route: String,
) : ITypeModel {
    companion object {
        fun convert(
            bgColor: Color,
            attr: CapsuleFloorAttrBean,
            bean: CapsuleResourceBitAttrBean
        ): HomeCapsuleImageModel {
            return HomeCapsuleImageModel(
                cmsFloorId = attr.cmsFloorId.orEmpty(),
                cmsBlockId = bean.cmsResourceBitId.orEmpty(),
                radius = attr.radius?.toFloat() ?: 0f,
                sideMargin = attr.sideMargin?.toFloat() ?: 0f,
                topMargin = attr.topMargin?.toFloat() ?: 0f,
                bgColor = bgColor,
                imageUrl = bean.customPic.toLoadUrl(),
                ratio = bean.picRatio?.toFloat() ?: 0f,
                route = bean.route.orEmpty()
            )
        }
    }
}

class HomeCapsuleBannerModel(
    val list: MutableList<BannerData>,
    val radius: Float,
    val sideMargin: Float,
    val topMargin: Float,
    val bgColor: Color,
    val ratio: Float,
) : ITypeModel {
    companion object {
        fun convert(bgColor: Color, bean: CapsuleFloorBean): HomeCapsuleBannerModel {
            var ratio = 0f
            val list = bean.resourceBitList?.map {
                ratio = it.resourceBitAttr?.picRatio?.toFloat() ?: 0.3f
                BannerData(
                    imgUrl = it.resourceBitAttr?.customPic.toLoadUrl(),
                    linkType = 0,
                    linkUrl = it.resourceBitAttr?.route.orEmpty()
                )
            }?.toMutableList() ?: mutableListOf()
            return HomeCapsuleBannerModel(
                list = list,
                radius = bean.floorAttr?.radius?.toFloat() ?: 0f,
                sideMargin = bean.floorAttr?.sideMargin?.toFloat() ?: 0f,
                topMargin = bean.floorAttr?.topMargin?.toFloat() ?: 0f,
                bgColor = bgColor,
                ratio = ratio
            )
        }
    }
}

class HomeCapsuleHotAreaModel(

) : ITypeModel {
    val list: MutableList<CapsuleHotArea> = mutableListOf()
    var radius: Float = 0f
    var sideMargin: Float = 0f
    var topMargin: Float = 0f
    var bgColor: Color = White
    var ratio: Float = 0f
    var imgUrl: String = ""

    companion object {
        fun convert(bgColor: Color, floor: CapsuleFloorBean): HomeCapsuleHotAreaModel {
            val model = HomeCapsuleHotAreaModel()
            val screenWidthDp = ArkApp.get().resources.configuration.screenWidthDp
            val topMarginDp = floor.floorAttr?.topMargin?.toFloat() ?: 0f
            val sideMarginDp = floor.floorAttr?.sideMargin?.toFloat() ?: 0f
            val viewWidthDp = screenWidthDp - sideMarginDp
            val viewHeightDp = viewWidthDp / (floor.floorAttr?.basicPicRatio?.toFloat() ?: 1f)
            model.imgUrl = floor.floorAttr?.basicPic.toLoadUrl()
            model.ratio = floor.floorAttr?.basicPicRatio?.toFloat() ?: 0f
            model.radius = floor.floorAttr?.radius?.toFloat() ?: 0f
            model.topMargin = topMarginDp
            model.bgColor = bgColor
            model.sideMargin = sideMarginDp
            floor.resourceBitList?.forEachIndexed { index, resourceAttr ->
                val resource = resourceAttr.resourceBitAttr
                resource?.let {
                    val top = resource.top?.toFloat() ?: 0f
                    val left = resource.left?.toFloat() ?: 0f
                    val width = resource.width?.toFloat() ?: 0f
                    val height = resource.height?.toFloat() ?: 0f
                    val regionTop = viewHeightDp * top
                    val regionLeft = viewWidthDp * left
                    val regionWidth = viewWidthDp * width
                    val regionHeight = viewHeightDp * height
                    model.list.add(
                        CapsuleHotArea(
                            start = 0f.coerceAtLeast(regionLeft),
                            top = 0f.coerceAtLeast(regionTop),
                            width = 0f.coerceAtLeast(regionWidth),
                            height = 0f.coerceAtLeast(regionHeight),
                            linkUrl = resourceAttr.resourceBitAttr.route.orEmpty()
                        )
                    )
                }
            }
            return model
        }
    }
}

class CapsuleHotArea(
    val start: Float,
    val width: Float,
    val top: Float,
    val height: Float,
    val linkType: Int = 0,
    val linkUrl: String,
)