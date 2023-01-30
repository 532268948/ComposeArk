package com.nsyw.composeark.api

import com.nsyw.composeark.bean.*
import com.nsyw.nsyw_base.http.GetawayDomain
import com.nsyw.nsyw_base.http.HttpResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @author qianjiang
 * @date   2023/1/10
 */
interface HomeApi {

    /**
     * 蜂享家首页氛围和胶囊页详情
     * @see <a href="http://yapi.webuy.ai/project/1131/interface/api/120247">地址链接</a>
     */
    @GET("/jl-cms/lingXi/fxjApp/queryCapsulePageDetail")
    suspend fun getHomeCapsulePageDetail(): HttpResponse<AtmosphereAndCapsuleBean>

    /**
     * 底纹词、热搜词
     *
     * @see <a href="http://yapi.webuy.ai/project/687/interface/api/111318">地址链接</a>
     */
    @Headers(GetawayDomain.HEADERS_JL_GATEWAY)
    @POST("/searchplatform/search/queryDrainages")
    suspend fun queryDrainages(@Body params: HashMap<String, Any?>): HttpResponse<DrainagesBean>

    /**
     * 蜂享家app-首页前台类目(活动营销类目+常规类目)
     * @see <a href="http://yapi.webuy.ai/project/1131/interface/api/120525">地址链接</a>
     */
    @GET("/jl-cms/lingXi/fxjApp/queryFrontCategoryList")
    suspend fun queryFrontCategoryList(): HttpResponse<HomeCategoryTabEntryBean>

    /**
     * 蜂享家app-营销频道
     * @see <a href="http://yapi.webuy.ai/project/1131/interface/api/120527">地址链接</a>
     */
    @GET("/jl-cms/lingXi/fxjApp/queryMarketingModuleDetail")
    suspend fun queryMarketingModuleDetail(): HttpResponse<MarketingModuleDetailBean>

    /**
     * 业务首页信息
     *
     * @see <a href="http://yapi.webuy.ai/project/955/interface/api/118478">地址链接</a>
     */
    @GET("/noah/indexAggregat/queryIndexInfoForAppV2")
    suspend fun queryIndexInfoForApp(): HttpResponse<IndexInfoForAppBean>

    /**
     * 会场列表。
     */
    @POST("/noah/exhibitionPark/homeListForApp")
    suspend fun getHomeExhibitionData(@Body param: HashMap<String, Any?>): HttpResponse<HomeExhibitionEntryBean>

    /**
     * 首页品牌会场聚合列表
     *
     * http://yapi.webuy.ai/project/955/interface/api/135765
     */
    @POST("/noah/home/brandList")
    suspend fun getHomeBrandList(@Body param: HashMap<String, Any?>): HttpResponse<HomeBrandEntryBean>
}