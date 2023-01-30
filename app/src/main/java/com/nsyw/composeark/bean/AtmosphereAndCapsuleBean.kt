package com.nsyw.composeark.bean

import androidx.annotation.Keep

/**
 * @author qianjiang
 * @date   2023/1/10
 */
@Keep
data class AtmosphereAndCapsuleBean(
    // 活动页id
    val activityPageId: Long? = null,
    // 顶部氛围背景色
    val topBgColor: String? = null,
    // 顶部氛围图
    val topBgPic: String? = null,
    // 胶囊页背景色
    val pageBgColor: String? = null,
    // tab栏未选中颜色
    val categoryFontColor: String? = null,
    // tab栏选中颜色
    val selectedColor: String? = null,
    // 状态栏颜色;0:默认 1:浅色 3:深色
    val statusBarStyle: Int ?,
    // 页面楼层详情
    val floorList: List<CapsuleFloorBean>? = null,
    /**
     * 追悼日模式顶部与底部开关(1：打开 0或null都为关闭)
     */
    val memorialDayModeSwitch: Int? = null,

    /**
     * 追悼日模式热区开关(1：打开 0或null都为关闭)
     */
    val memorialDayCapsuleSwitch: Int? = null
)

data class CapsuleFloorBean(
    val floorAttr: CapsuleFloorAttrBean? = null,
    val resourceBitList: List<ResourceBitBean>? = null
)

data class CapsuleFloorAttrBean(
    // 楼层id
    val cmsFloorId: String? = null,
    val componentType: String? = null,
    val topMargin: String? = null,
    val sideMargin: String? = null,
    val basicPic: String? = null,
    val basicPicRatio: String? = null,
    val radius: String? = null,
)

data class ResourceBitBean(
    val resourceBitAttr: CapsuleResourceBitAttrBean? = null
)

data class CapsuleResourceBitAttrBean(
    // 坑位id
    val cmsResourceBitId: String? = null,
    val top: String? = null,
    val left: String? = null,
    val width: String? = null,
    val height: String? = null,
    val customPic: String? = null,
    val picRatio: String? = null,
    val route: String? = null,
)