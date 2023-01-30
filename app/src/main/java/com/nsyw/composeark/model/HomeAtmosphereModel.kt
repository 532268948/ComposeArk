package com.nsyw.composeark.model

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.composeark.bean.AtmosphereAndCapsuleBean
import com.nsyw.nsyw_base.theme.Gray
import com.nsyw.nsyw_base.theme.ThemeColor
import com.nsyw.nsyw_base.theme.White

/**
 * @author qianjiang
 * @date   2023/1/10
 * 首页氛围信息
 */
class HomeAtmosphereModel(
    // 状态栏颜色;0:默认 1:浅色 3:深色
    val statusBarStyle: Int = 0,
    // 顶部氛围背景色
    val topBgColor: Color = ThemeColor,
    // 顶部氛围图
    val topBgPicUrl: String = "",
    // 胶囊页背景色
    val capsuleBgColor: Color = ThemeColor,
    // tab栏未选中颜色
    val tabUnSelectColor: Color = Gray,
    // tab栏选中颜色
    val tabSelectColor: Color = White,
) {


    companion object {
        fun convert(bean: AtmosphereAndCapsuleBean) :HomeAtmosphereModel{
            return HomeAtmosphereModel(
                statusBarStyle = bean.statusBarStyle?:0,
                topBgColor =if(bean.topBgColor.isNullOrBlank()) ThemeColor else Color(bean.topBgColor.toColorInt()),
                topBgPicUrl = bean.topBgPic.toLoadUrl()
            )
        }
    }
}