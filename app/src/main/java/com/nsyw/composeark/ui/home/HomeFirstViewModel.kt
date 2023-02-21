package com.nsyw.composeark.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.nsyw.composeark.api.HomeApi
import com.nsyw.composeark.bean.HomeBrandEntryBean
import com.nsyw.composeark.bean.HomeExhibitionEntryBean
import com.nsyw.composeark.model.*
import com.nsyw.composeark.repository.HomeRepository
import com.nsyw.nsyw_base.BaseViewModel
import com.nsyw.nsyw_base.ITypeModel
import com.nsyw.nsyw_base.http.RetrofitHelper
import com.nsyw.nsyw_base.widget.refreshloadmorelazycolumn.LoadStatus
import kotlinx.coroutines.launch

/**
 * @author qianjiang
 * @date   2023/1/10
 */
class HomeFirstViewModel : BaseViewModel() {

    private val repository by lazy {
        HomeRepository(RetrofitHelper.createService(HomeApi::class.java))
    }

    var viewStates by mutableStateOf(HomeFirstViewState())

    private var tabModel: HomeMainTabModel? = null
    private var pageNo = 1

    fun dispatch(action: HomeFirstViewIntent) {
        when (action) {
            is HomeFirstViewIntent.Init -> {
                if (viewStates.isInit) return
                viewStates = viewStates.copy(isInit = true, refresh = true)
                tabModel = action.tabModel
                pageNo = 1
                loadData()
            }
            is HomeFirstViewIntent.LoadData -> {
                viewStates = viewStates.copy(refresh = true)
                tabModel = action.tabModel
                pageNo = 1
                loadData()
            }
            is HomeFirstViewIntent.LoadMore -> {
                loadData()
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                if (tabModel == null) return@launch
                if (viewStates.loadStatus == LoadStatus.LOADING) return@launch
                viewStates = viewStates.copy(loadStatus = LoadStatus.LOADING)
                val response = when (tabModel?.type) {
                    2 -> repository.queryHomeBrandList(type = 2, pageNo = pageNo)
                    else -> repository.queryHomeExhibitionData(
                        pageNo = pageNo,
                        type = tabModel?.type,
                        marketCategoryId = tabModel?.marketCategoryId
                    )
                }
                if (checkStatusAndEntryWithToast(response)) {
                    val entry = response.entry!!
                    val list: MutableList<ITypeModel> = mutableListOf()
                    when (entry) {
                        is HomeBrandEntryBean -> {
                            entry.brandList?.map {
                                when (it.showStyle) {
                                    0 -> {
                                        list.add(
                                            HomeBrandModel.convert(
                                                type = tabModel?.type,
                                                brandBean = it
                                            )
                                        )
                                    }
                                    else -> {
                                        list.add(
                                            HomeSingleBrandModel.convert(
                                                type = tabModel?.type,
                                                brandBean = it
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        is HomeExhibitionEntryBean -> {
                            entry.indexExhibitionInfoVOList?.map {
                                when (it.showStyle) {
                                    0 -> {
                                        list.add(
                                            HomeExhibitionModel.convert(
                                                type = tabModel?.type,
                                                homeExhibitionBean = it
                                            )
                                        )
                                    }
                                    else -> {
                                        list.add(
                                            HomeSingleExhibitionModel.convert(
                                                type = tabModel?.type,
                                                exhibitionBean = it
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                    val hasNext = response.hasNext == 1
                    when {
                        pageNo == 1 && list.isEmpty() -> {
                            viewStates = viewStates.copy(
                                refresh = false,
                                loadStatus = LoadStatus.EMPTY,
                                list = mutableListOf()
                            )
                        }
                        pageNo == 1 && !hasNext -> {
                            viewStates = viewStates.copy(
                                refresh = false,
                                loadStatus = LoadStatus.NO_MORE_DATA,
                                list = list
                            )
                        }
                        pageNo == 1 && hasNext -> {
                            pageNo++
                            viewStates = viewStates.copy(
                                refresh = false,
                                loadStatus = LoadStatus.LOAD_COMPLETE,
                                list = list
                            )
                        }
                        !hasNext -> {
                            viewStates.list.addAll(list)
                            viewStates = viewStates.copy(
                                loadStatus = LoadStatus.NO_MORE_DATA,
                            )
                        }
                        else -> {
                            pageNo++
                            viewStates.list.addAll(list)
                            viewStates = viewStates.copy(
                                loadStatus = LoadStatus.LOAD_COMPLETE,
                            )
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    private fun loadMore() {

    }

}

sealed class HomeFirstViewIntent {
    class Init(val tabModel: HomeMainTabModel?) : HomeFirstViewIntent()
    class LoadData(val tabModel: HomeMainTabModel?) : HomeFirstViewIntent()
    object LoadMore : HomeFirstViewIntent()
}

data class HomeFirstViewState(
    val isInit: Boolean = false,
    val refresh: Boolean = false,
    val loadStatus: LoadStatus = LoadStatus.INIT,
    val list: MutableList<ITypeModel> = mutableListOf(),
)