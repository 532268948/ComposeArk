package com.nsyw.composeark.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.nsyw.composeark.api.HomeApi
import com.nsyw.composeark.model.*
import com.nsyw.composeark.repository.HomeRepository
import com.nsyw.nsyw_base.BaseViewModel
import com.nsyw.nsyw_base.http.RetrofitHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * @author qianjiang
 * @date   2023/1/10
 */
class HomeViewModel : BaseViewModel() {

    private val repository by lazy {
        HomeRepository(RetrofitHelper.createService(HomeApi::class.java))
    }

    var viewStates by mutableStateOf(HomeViewState())

    fun dispatch(action: HomeViewIntent) {
        when (action) {
            is HomeViewIntent.LoadData -> {
                loadData()
            }
            is HomeViewIntent.OnRefresh -> {
                onRefresh()
            }
        }
    }

    private fun loadData() {
        if (viewStates.isInit) return
        viewModelScope.launch {
            supervisorScope {
                try {
                    showLoading()
                    val atmosphereAndCapsuleModel = async { queryAtmosphereAndCapsuleData() }
                    val categoryTabList = async { queryCategoryTabList() }
                    val marketModel = async { queryMarketData() }
                    val homeMainTabEntryModel = async { queryHomeMainTabData() }
                    queryDrainages()
                    val (atmosphereModel, capsuleModel) = atmosphereAndCapsuleModel.await()
                    atmosphereModel?.let {
                        viewStates =
                            viewStates.copy(
                                isInit = true,
                                atmosphereModel = it,
                                tabList = categoryTabList.await(),
                                capsuleModel = capsuleModel,
                                marketModel = marketModel.await(),
                                homeMainTabModel = homeMainTabEntryModel.await()
                            )
                    }
                    showContent()
                } catch (e: Exception) {
                    showError()
                }
            }
        }
    }

    private fun onRefresh() {
        viewModelScope.launch {
            supervisorScope {
                try {
                    viewStates = viewStates.copy(isRefreshing = true)
                    val atmosphereAndCapsuleModel = async { queryAtmosphereAndCapsuleData() }
                    val categoryTabList = async { queryCategoryTabList() }
                    val marketModel = async { queryMarketData() }
                    val homeMainTabEntryModel = async { queryHomeMainTabData() }
                    queryDrainages()
                    val (atmosphereModel, capsuleModel) = atmosphereAndCapsuleModel.await()
                    atmosphereModel?.let {
                        viewStates =
                            viewStates.copy(
                                isRefreshing = false,
                                atmosphereModel = it,
                                tabList = categoryTabList.await(),
                                capsuleModel = capsuleModel,
                                marketModel = marketModel.await(),
                                homeMainTabModel = homeMainTabEntryModel.await()
                            )
                    }
                } catch (e: Exception) {
                    viewStates = viewStates.copy(isRefreshing = false)
                    showError()
                }
            }
        }
    }

    private suspend fun queryAtmosphereAndCapsuleData(): Pair<HomeAtmosphereModel?, HomeCapsuleModel> {
        val response = repository.getHomeCapsulePageDetail()
        if (checkStatusAndEntryWithToast(response)) {
            val homeAtmosphereModel = HomeAtmosphereModel.convert(response.entry!!)
            return Pair(
                homeAtmosphereModel,
                HomeCapsuleModel.convert(
                    bgColor = homeAtmosphereModel.capsuleBgColor,
                    bean = response.entry
                )
            )
        }
        return Pair(null, HomeCapsuleModel())
    }

    /**
     * 获取搜索底纹词
     */
    private fun queryDrainages() {
        viewModelScope.launch {
//            supervisorScope {
                try {
                    val response = repository.queryDrainages(intArrayOf(1))
                    if (checkStatusAndEntryWithToast(response)) {
                        val list = response.entry?.shadingWords?.map {
                            it.name
                        }?.filterNotNull()
                        if (list.isNullOrEmpty()) {
                            val hint = "请输入品牌名、商品名、货号或关键字"
                            viewStates = viewStates.copy(searchHint = hint)
                            return@launch
                        }
                        for (word in list) {
                            viewStates = viewStates.copy(searchHint = word)
                            delay(5000)
                        }
                    }
                } catch (e: Exception) {
                    val hint = "请输入品牌名、商品名、货号或关键字"
                    viewStates = viewStates.copy(searchHint = hint)
                    return@launch
                }
//            }
        }
    }

    private suspend fun queryCategoryTabList(): MutableList<HomeCategoryTabModel> {
        val response = repository.queryFrontCategoryList()
        val list = if (checkStatusAndEntryWithToast(response)) {
            response.entry?.rows?.map {
                HomeCategoryTabModel(
                    name = it.name.orEmpty(),
                    tabType = it.type ?: 4,
                    linkType = it.interactionType,
                    linkUrl = it.linkUrl
                )
            }?.toMutableList() ?: mutableListOf()
        } else {
            mutableListOf()
        }
        list.add(
            0, HomeCategoryTabModel(
                name = "精选",
                tabType = 0,
                linkType = 1,
            )
        )
        return list
    }

    /**
     * 营销数据
     */
    private suspend fun queryMarketData(): HomeMarketModel {
        val response = repository.queryMarketingModuleDetail()
        if (checkStatusAndEntryWithToast(response)) {
            return HomeMarketModel.convert(response.entry!!)
        }
        return HomeMarketModel()
    }

    private suspend fun queryHomeMainTabData(): HomeMainTabEntryModel {
        val response = repository.queryIndexInfoForApp()
        if (checkStatusAndEntryWithToast(response)) {
            return HomeMainTabEntryModel.convert(response.entry!!)
        }
        return HomeMainTabEntryModel()
    }
}

sealed class HomeViewIntent {
    object LoadData : HomeViewIntent()
    object OnRefresh : HomeViewIntent()
}

data class HomeViewState(
    val isInit: Boolean = false,
    val isRefreshing: Boolean = false,
    // 首页氛围
    val atmosphereModel: HomeAtmosphereModel = HomeAtmosphereModel(),
    // tab列表
    val tabList: MutableList<HomeCategoryTabModel> = mutableListOf(
        HomeCategoryTabModel(
            name = "精选",
            tabType = 0,
            linkType = 1,
        )
    ),
    val searchHint: String = "请输入品牌名、商品名、货号或关键字",
    // 胶囊页数据
    val capsuleModel: HomeCapsuleModel = HomeCapsuleModel(),
    // 首页营销数据
    val marketModel: HomeMarketModel = HomeMarketModel(),
    val homeMainTabModel: HomeMainTabEntryModel = HomeMainTabEntryModel()
)