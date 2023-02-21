package com.nsyw.nsyw_base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.nsyw.nsyw_base.http.HttpResponse
import com.nsyw.nsyw_base.utils.ToastUtil
import com.nsyw.nsyw_base.widget.statelayout.PageState
import com.nsyw.nsyw_base.widget.statelayout.StateData
import com.nsyw.nsyw_base.widget.statelayout.bindData


abstract class BaseViewModel : ViewModel() {

//    private val _loadEvents = Channel<LoadEvent>(Channel.BUFFERED)
//    val loadEvents = _loadEvents.receiveAsFlow()

    var loadState by mutableStateOf(LoadState())

    var pageState by mutableStateOf(PageState.LOADING.bindData())

    fun <T> checkStatusAndEntryWithToast(response: HttpResponse<T>): Boolean {
        val success = response.status && response.entry != null
        if (success) return true
        val errorMsg = response.message
        if (!errorMsg.isNullOrBlank()) {
            showToast(errorMsg)
        }
        return false
    }

    fun showToast(msg: String) {
        ToastUtil.showToast(msg)
    }

    fun showLoadingDialog() {
        if (loadState.showLoading) return
        loadState = loadState.copy(showLoading = true)
    }

    fun hideLoadingDialog() {
        if (!loadState.showLoading) return
        loadState = loadState.copy(showLoading = false)
    }

    protected fun showLoading() {
        pageState = PageState.LOADING.bindData()
    }

    protected fun showEmpty() {
        pageState = PageState.EMPTY.bindData()
    }

    protected fun showError(msg: String = "出错啦") {
        pageState = PageState.ERROR.bindData(StateData(msg, R.drawable.base_status_error, "重试"))
    }

    protected fun showContent() {
        pageState = PageState.CONTENT.bindData()
    }
}

data class LoadState(
    val showLoading: Boolean = false
)

//sealed class LoadEvent {
//    object ShowLoading : LoadEvent()
//    object HideLoading : LoadEvent()
//}