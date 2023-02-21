package com.nsyw.nsyw_base.widget.refresh

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.platform.inspectable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsyw.nsyw_base.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Math.abs
import kotlin.math.pow

/**
 * @author qianjiang
 * @date   2023/2/20
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshLayout(
    modifier: Modifier = Modifier,
    textColor: Color = White,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    content: @Composable () -> Unit,
    ) {
    val refreshingResId = listOf(
        com.nsyw.nsyw_base.R.drawable.base_refresh_pull,
        com.nsyw.nsyw_base.R.drawable.base_refreshing_2,
        com.nsyw.nsyw_base.R.drawable.base_refreshing_2,
        com.nsyw.nsyw_base.R.drawable.base_refreshing_2,
        com.nsyw.nsyw_base.R.drawable.base_refreshing_2,
        com.nsyw.nsyw_base.R.drawable.base_refreshing_2
    )

    //刷新内容高度
    val loadingHeightPx: Float
    with(LocalDensity.current) {
        loadingHeightPx = 36.dp.toPx()
    }

    //刷新循环动画
    val refreshAnimate by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = (refreshingResId.size - 1).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(250, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val state = rememberPullRefreshLayoutState(refreshing, onRefresh)

    Box(
        Modifier
            .pullRefreshLayout(state)

    ) {
        Box(modifier = modifier.graphicsLayer { translationY = state.position }) {
            content.invoke()
        }
        val id = if (refreshing) refreshAnimate else 0
        if (state.position >= loadingHeightPx * 0.5f) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .graphicsLayer {
                        alpha =
                            1f.coerceAtMost((state.position - loadingHeightPx * 0.5f) / (loadingHeightPx))
                        translationY = state.position * 0.5f - loadingHeightPx * 0.5f
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(refreshingResId[id.toInt()]),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(40.dp, 16.dp)
                )
                Text(
                    modifier = modifier.padding(top = 4.dp),
                    text = if (refreshing) {
                        "正在刷新"
                    } else if (state.progress <= 1f) {
                        "下拉刷新"
                    } else {
                        "松开立即刷新"
                    },
                    color = textColor,
                    fontSize = 12.sp
                )

            }
        }
    }
}

@Composable
@ExperimentalMaterialApi
fun rememberPullRefreshLayoutState(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    refreshThreshold: Dp = PullRefreshDefaults.RefreshThreshold,
    refreshingOffset: Dp = PullRefreshDefaults.RefreshingOffset,
): PullRefreshLayoutState {
    require(refreshThreshold > 0.dp) { "The refresh trigger must be greater than zero!" }

    val scope = rememberCoroutineScope()
    val onRefreshState = rememberUpdatedState(onRefresh)
    val thresholdPx: Float
    val refreshingOffsetPx: Float

    with(LocalDensity.current) {
        thresholdPx = refreshThreshold.toPx()
        refreshingOffsetPx = refreshingOffset.toPx()
    }

    val state = remember(scope) {
        PullRefreshLayoutState(scope, onRefreshState, refreshingOffsetPx, thresholdPx)
    }

    SideEffect {
        state.setRefreshing(refreshing)
    }

    return state
}

@ExperimentalMaterialApi
fun Modifier.pullRefreshLayout(
    state: PullRefreshLayoutState,
    enabled: Boolean = true
) = inspectable(inspectorInfo = debugInspectorInfo {
    name = "pullRefresh"
    properties["state"] = state
    properties["enabled"] = enabled
}) {
    Modifier.pullRefresh(state::onPull, { state.onRelease() }, enabled)
}

@ExperimentalMaterialApi
class PullRefreshLayoutState internal constructor(
    private val animationScope: CoroutineScope,
    private val onRefreshState: State<() -> Unit>,
    private val refreshingOffset: Float,
    internal val threshold: Float
) {

    val progress get() = adjustedDistancePulled / threshold

    internal val refreshing get() = _refreshing

    //唯一的变化去掉internal
    val position get() = _position

    private val adjustedDistancePulled by derivedStateOf { distancePulled * 0.5f }

    private var _refreshing by mutableStateOf(false)
    private var _position by mutableStateOf(0f)
    private var distancePulled by mutableStateOf(0f)

    internal fun onPull(pullDelta: Float): Float {
        if (this._refreshing) return 0f

        val newOffset = (distancePulled + pullDelta).coerceAtLeast(0f)
        val dragConsumed = newOffset - distancePulled
        distancePulled = newOffset
        _position = calculateIndicatorPosition()
        return dragConsumed
    }

    internal fun onRelease() {
        if (!this._refreshing) {
            if (adjustedDistancePulled > threshold) {
                onRefreshState.value()
            } else {
                animateIndicatorTo(0f)
            }
        }
        distancePulled = 0f
    }

    internal fun setRefreshing(refreshing: Boolean) {
        if (this._refreshing != refreshing) {
            this._refreshing = refreshing
            this.distancePulled = 0f
            animateIndicatorTo(if (refreshing) refreshingOffset else 0f)
        }
    }

    private fun animateIndicatorTo(offset: Float) = animationScope.launch {
        animate(initialValue = _position, targetValue = offset) { value, _ ->
            _position = value
        }
    }

    private fun calculateIndicatorPosition(): Float = when {
        adjustedDistancePulled <= threshold -> adjustedDistancePulled
        else -> {
            val overshootPercent = abs(progress) - 1.0f
            val linearTension = overshootPercent.coerceIn(0f, 2f)
            val tensionPercent = linearTension - linearTension.pow(2) / 4
            val extraOffset = threshold * tensionPercent
            threshold + extraOffset
        }
    }
}