package com.nsyw.composeark.model

import com.nsyw.nsyw_base.ITypeModel

/**
 * @author qianjiang
 * @date   2023/1/13
 */
class HomeSecKillAndRankModel : ITypeModel {
    var visible: Boolean = false
    var homeSecKillModel: HomeSecKillModel = HomeSecKillModel()
    var homeRankModel: HomeRankModel = HomeRankModel()
}