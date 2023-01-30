package com.nsyw.nsyw_base.widget.statelayout

import com.nsyw.nsyw_base.R

/**
 * @author qianjiang
 * @date   2023/1/5
 */

enum class PageState {
    LOADING,
    EMPTY,
    ERROR,
    CONTENT
}

data class StateData(
    val tipTex: String? = null,
    val tipImg: Int? = null,
    val btnText: String? = null
)

data class PageStateData(val status: PageState, val tag: Any? = null)

fun PageState.bindData(stateData: StateData? = null): PageStateData {
    when (this) {
        PageState.LOADING -> {
            val data = stateData ?: StateData("小蜂蜂努力加载中")
            return PageStateData(this, data)
        }
        PageState.EMPTY -> {
            val data = stateData ?: StateData("暂无内容", R.drawable.base_status_empty)
            return PageStateData(this, data)
        }
        PageState.ERROR -> {
            val data = stateData ?: StateData("出错啦", R.drawable.base_status_error, "重试")
            return PageStateData(this, data)
        }
        PageState.CONTENT -> {
            return PageStateData(this, stateData)
        }
    }
}

data class StateLayoutData(val pageStateData: PageStateData, val retry: OnRetry = {})

typealias OnRetry = (PageStateData) -> Unit