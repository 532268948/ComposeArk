package com.nsyw.composeark.repository

import com.nsyw.composeark.api.HomeApi
import com.nsyw.composeark.bean.*
import com.nsyw.nsyw_base.Constant
import com.nsyw.nsyw_base.http.HttpResponse

/**
 * @author qianjiang
 * @date   2023/1/10
 */
class HomeRepository(private val api: HomeApi) {

    /**
     * 蜂享家首页氛围和胶囊页详情
     */
    suspend fun getHomeCapsulePageDetail(): HttpResponse<AtmosphereAndCapsuleBean> {
        return api.getHomeCapsulePageDetail()
    }

    /**
     * 搜索热搜词，底纹词
     */
    suspend fun queryDrainages(
        typeList: IntArray,
    ): HttpResponse<DrainagesBean> {
        return api.queryDrainages(
            hashMapOf(
                "subBizType" to Constant.SUB_BIZ_TYPE,
                "typeList" to typeList,
                "reqSource" to "FXJ_APP",
            )
        )
    }

    /**
     * 蜂享家app-首页前台类目(活动营销类目+常规类目)
     */
    suspend fun queryFrontCategoryList(): HttpResponse<HomeCategoryTabEntryBean> {
        return api.queryFrontCategoryList()
    }

    /**
     * 蜂享家app-营销频道
     */
    suspend fun queryMarketingModuleDetail(): HttpResponse<MarketingModuleDetailBean> {
        return api.queryMarketingModuleDetail()
    }

    suspend fun queryIndexInfoForApp(): HttpResponse<IndexInfoForAppBean> {
        return api.queryIndexInfoForApp()
    }

    suspend fun queryHomeExhibitionData(
        pageNo: Int,
        type: Int? = 0,
        marketCategoryId: Long? = null
    ): HttpResponse<HomeExhibitionEntryBean> {
        val body = hashMapOf<String, Any?>(
            "type" to type,
            "pageNo" to pageNo,
            "pageSize" to 10,
            "marketCategoryId" to marketCategoryId
        )
        return api.getHomeExhibitionData(body)
    }

    suspend fun queryHomeBrandList(
        type: Int,
        pageNo: Int,
        nextStartIndex: Long? = null,
    ): HttpResponse<HomeBrandEntryBean> {
        return api.getHomeBrandList(
            hashMapOf(
                "type" to type,
                "pageNo" to pageNo,
                "pageSize" to 10,
                "nextStartIndex" to nextStartIndex
            )
        )
    }
}