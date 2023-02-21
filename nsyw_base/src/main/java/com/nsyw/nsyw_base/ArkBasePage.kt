package com.nsyw.nsyw_base

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nsyw.nsyw_base.widget.load.LoadingDialog
import com.nsyw.nsyw_base.widget.statelayout.OnRetry
import com.nsyw.nsyw_base.widget.statelayout.PageState
import com.nsyw.nsyw_base.widget.statelayout.StateData
import com.nsyw.nsyw_base.widget.statelayout.StateLayoutData

/**
 * @author qianjiang
 * @date   2023/1/10
 */
@Composable
fun ArkBasePage(
    viewModel: BaseViewModel,
    modifier: Modifier = Modifier,
    onRetry: OnRetry = { },
    loading: @Composable (StateLayoutData) -> Unit = { DefaultLoadingLayout(stateLayoutData = it) },
    empty: @Composable (StateLayoutData) -> Unit = { DefaultEmptyLayout(stateLayoutData = it) },
    error: @Composable (StateLayoutData) -> Unit = { DefaultErrorLayout(stateLayoutData = it) },
    content: @Composable BoxScope.() -> Unit
) {
    val loadingState = viewModel.loadState

    val pageState = viewModel.pageState
    val stateLayoutData = StateLayoutData(pageState, onRetry)
    Box(
        modifier = modifier,
    ) {
        when (pageState.status) {
            PageState.LOADING -> loading(stateLayoutData)
            PageState.EMPTY -> empty(stateLayoutData)
            PageState.ERROR -> error(stateLayoutData)
            PageState.CONTENT -> content()
        }
        LoadingDialog(loadingState.showLoading)
    }
}

@Composable
fun DefaultLoadingLayout(stateLayoutData: StateLayoutData) {
    stateLayoutData.pageStateData.let {
        (it.tag as? StateData)?.let { item ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = item.tipTex ?: "",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DefaultEmptyLayout(stateLayoutData: StateLayoutData) {
    stateLayoutData.pageStateData.let {
        (it.tag as? StateData)?.let { item ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = item.tipImg ?: 0),
                        modifier = Modifier.size(200.dp),
                        contentDescription = ""
                    )
                    Text(
                        text = item.tipTex ?: "",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DefaultErrorLayout(stateLayoutData: StateLayoutData) {
    stateLayoutData.pageStateData.let {
        (it.tag as? StateData)?.let { item ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = item.tipImg ?: 0),
                        modifier = Modifier.size(200.dp),
                        contentDescription = ""
                    )
                    Text(
                        text = item.tipTex ?: "",
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .padding(16.dp, 0.dp)
                            .offset(0.dp, -(20.dp))
                    )

                    Text(
                        text = item.btnText ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp, 0.dp)
                            .width(100.dp)
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Blue)
                            .padding(0.dp, 10.dp)
                            .clickable {
                                stateLayoutData.retry.invoke(it)
                            })
                }
            }
        }
    }
}