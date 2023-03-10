package com.nsyw.nsyw_base.widget.banner

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nsyw.nsyw_base.R
import kotlinx.coroutines.delay

@ExperimentalPagerApi
@Composable
fun Banner(
    list: MutableList<BannerData>,
    modifier: Modifier = Modifier,
    contentRadius: Float = 0f,
    timeMillis: Long = 3000,
    @DrawableRes loadImage: Int = R.drawable.base_banner_empty,
    indicatorAlignment: Alignment = Alignment.BottomCenter,
    onClick: (data: BannerData) -> Unit
) {
    val newList= mutableListOf<BannerData>()
    newList.addAll(list)
    newList.add(list[0])
    BannerIn(
        list = newList,
        modifier = modifier,
        contentRadius = contentRadius,
        timeMillis = timeMillis,
        loadImage = loadImage,
        indicatorAlignment = indicatorAlignment,
        onClick = onClick
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun BannerIn(
    list: MutableList<BannerData>?,
    modifier: Modifier = Modifier,
    contentRadius: Float = 0f,
    timeMillis: Long = 3000,
    @DrawableRes loadImage: Int = R.drawable.base_banner_empty,
    indicatorAlignment: Alignment = Alignment.BottomCenter,
    onClick: (data: BannerData) -> Unit
) {
    Box(
        modifier = modifier
    ) {

        if (list == null) {
            //??????????????????
            Image(
                painterResource(loadImage),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(16.dp)),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        } else {
            val pagerState = rememberPagerState(
                //????????????
                initialPage = 0
            )

            //??????????????????
            var executeChangePage by remember { mutableStateOf(false) }
            var currentPageIndex = 0

            //????????????
            LaunchedEffect(pagerState.currentPage, executeChangePage) {
                if (pagerState.pageCount > 0) {
                    //????????????+1???????????????????????????infiniteLoop == true
                    if (pagerState.currentPage == list.size - 1) {
                        pagerState.scrollToPage(0)
                    } else {
                        delay(timeMillis)
                        pagerState.animateScrollToPage(
                            page = (pagerState.currentPage + 1) % (pagerState.pageCount),
                            pageOffset = 0f
                        )
//                        pagerState.scrollToPage((pagerState.currentPage + 1) % (pagerState.pageCount))
                    }
                }
            }

            HorizontalPager(
                count = list.size,
                state = pagerState,
                modifier = Modifier
                    .pointerInput(pagerState.currentPage) {
                        awaitPointerEventScope {
                            while (true) {
                                //PointerEventPass.Initial - ?????????????????????????????????????????????????????????
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                //?????????????????????????????????
                                val dragEvent = event.changes.firstOrNull()
                                when {
                                    //????????????????????????????????????
                                    dragEvent!!.positionChangeConsumed() -> {
                                        return@awaitPointerEventScope
                                    }
                                    //??????????????????(?????????????????????????????????)
                                    dragEvent.changedToDownIgnoreConsumed() -> {
                                        //?????????????????????????????????
                                        currentPageIndex = pagerState.currentPage
                                    }
                                    //??????????????????(?????????????????????????????????)
                                    dragEvent.changedToUpIgnoreConsumed() -> {
                                        //???????????????????????????/???????????????pagerState.targetPage???null??????????????????????????????
                                        if (pagerState.targetPage == null) return@awaitPointerEventScope
                                        //???pageCount??????1?????????????????????????????????????????????????????????????????????
                                        if (currentPageIndex == pagerState.currentPage && pagerState.pageCount > 1) {
                                            executeChangePage = !executeChangePage
                                        }
                                    }
                                }
                            }
                        }
                    }
                    .clickable {
                        with(list[pagerState.currentPage]) {
                            onClick.invoke(this)
                        }
                    }
                    .fillMaxSize(),
            ) { page ->
                Image(
                    painter = rememberImagePainter(list[page].imgUrl),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = RoundedCornerShape(contentRadius.dp)),
                    contentScale = ContentScale.Crop,
                    contentDescription = null
                )
            }

            Box(
                modifier = Modifier
                    .align(indicatorAlignment)
                    .padding(bottom = 6.dp, start = 6.dp, end = 6.dp)
            ) {

                //?????????
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until list.size - 1) {
                        //??????
                        var size by remember { mutableStateOf(5.dp) }
                        size =
                            if (pagerState.currentPage == i || (pagerState.currentPage == list.size - 1 && i == 0)) 7.dp else 5.dp

                        //??????
                        val color =
                            if (pagerState.currentPage == i || (pagerState.currentPage == list.size - 1 && i == 0)) MaterialTheme.colors.primary else Color.Gray

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(color)
                                //???size???????????????????????????????????????
                                .animateContentSize()
                                .size(size)
                        )
                        //?????????????????????
                        if (i != list.lastIndex) Spacer(
                            modifier = Modifier
                                .height(0.dp)
                                .width(4.dp)
                        )
                    }
                }

            }
        }

    }
}