package com.nsyw.composeark.ui.home

import androidx.lifecycle.viewModelScope
import com.nsyw.composeark.api.HomeApi
import com.nsyw.composeark.bean.HomeBrandEntryBean
import com.nsyw.composeark.bean.HomeExhibitionEntryBean
import com.nsyw.composeark.model.HomeBrandModel
import com.nsyw.composeark.model.HomeExhibitionModel
import com.nsyw.composeark.model.HomeSingleBrandModel
import com.nsyw.composeark.repository.HomeRepository
import com.nsyw.nsyw_base.BaseViewModel
import com.nsyw.nsyw_base.ITypeModel
import com.nsyw.nsyw_base.http.RetrofitHelper
import kotlinx.coroutines.launch

/**
 * @author qianjiang
 * @date   2023/1/16
 */
class HomeExhibitionViewModel : BaseViewModel() {

    private val repository by lazy {
        HomeRepository(RetrofitHelper.createService(HomeApi::class.java))
    }

    val viewStates by lazy {
        HomeExhibitionViewState()
    }

    // 最后疯抢:1 今日特卖:2  即将开售:3
    private var type = 2
    private var pageNo = 1

    fun dispatch(action: HomeExhibitionViewAction) {
        when (action) {
            is HomeExhibitionViewAction.LoadData -> {
//                type = action.type
                loadData()
            }
            is HomeExhibitionViewAction.LoadMore -> {
                loadMore()
            }
            is HomeExhibitionViewAction.ScrollToPage -> {
//                type = action.type
                loadData()
            }
        }
    }

    private fun loadData() {
//        viewModelScope.launch {
//            try {
//                val response = when (type) {
//                    2 -> repository.queryHomeBrandList(type = type, pageNo = pageNo)
//                    else -> repository.queryHomeExhibitionData(type = type, pageNo = pageNo)
//                }
//                if (checkStatusAndEntryWithToast(response)) {
//                    val entry = response.entry!!
//                    val list: MutableList<ITypeModel> = mutableListOf()
//                    when (entry) {
//                        is HomeBrandEntryBean -> {
//                            when(entry.showStyle){
//                                0->{
//                                    list.addAll(HomeBrandModel.convert(type = type, bean = entry))
//                                }
//                                else->{
//                                    list.addAll(HomeSingleBrandModel.convert(type = type, bean = entry))
//                                }
//                            }
//                        }
//                        is HomeExhibitionEntryBean -> {
//                            list.addAll(HomeExhibitionModel.convert(type = type, bean = entry))
//                        }
//                    }
//                    val hasNext = response.hasNext == 1
//                    when {
//                        pageNo == 1 && list.isEmpty() -> {
//
//                        }
//                        pageNo == 1 && !hasNext -> {
//
//                        }
//                        !hasNext -> {
//
//                        }
//                        else -> {
//
//                        }
//                    }
//                }
//                showContent()
//            } catch (e: Exception) {
//                showError()
//            }
//        }
    }

    private fun loadMore() {

    }

    private suspend fun queryHomeExhibitionData(pageNo: Int): MutableList<HomeExhibitionModel> {
        val response = repository.queryHomeExhibitionData(type, pageNo)
        if (checkStatusAndEntryWithToast(response)) {

        }
        return mutableListOf()
    }
}

class HomeExhibitionViewState(
    val list: MutableList<ITypeModel> = mutableListOf()
)

sealed class HomeExhibitionViewAction {
    class LoadData(val type: Int) : HomeExhibitionViewAction()
    object LoadMore : HomeExhibitionViewAction()
    class ScrollToPage(val tabIndex: Int, val type: Int) : HomeExhibitionViewAction()
}