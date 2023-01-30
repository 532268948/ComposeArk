package com.nsyw.nsyw_base.widget.refreshloadmorelazycolumn

import androidx.compose.runtime.Composable

/**
 * @author qianjiang
 * @date   2023/1/28
 */
@Composable
fun SmartRefreshLoadMoreLazyColumn() {
}

enum class LoadStatus{
    INIT,
    LOADING,
    LOAD_COMPLETE,
    EMPTY,
    NO_MORE_DATA,
}

