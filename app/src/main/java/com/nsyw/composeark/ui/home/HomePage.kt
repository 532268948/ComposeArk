package com.nsyw.composeark.ui.home

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Indicator
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nsyw.base.utils.toLoadUrl
import com.nsyw.composeark.R
import com.nsyw.composeark.model.*
import com.nsyw.nsyw_base.ArkBasePage
import com.nsyw.nsyw_base.ITypeModel
import com.nsyw.nsyw_base.theme.*
import com.nsyw.nsyw_base.utils.ToastUtil
import com.nsyw.nsyw_base.widget.banner.Banner
import com.nsyw.nsyw_base.widget.refreshloadmorelazycolumn.LoadStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * @author qianjiang
 * @date   2023/1/9
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePage() {
    val viewModel: HomeViewModel = viewModel()
    val viewStates = viewModel.viewStates
    val coroutineScope = rememberCoroutineScope()
    val systemUiCtrl = rememberSystemUiController()

    val pullRefreshState = rememberPullRefreshState(
        viewStates.isRefreshing,
        { viewModel.dispatch(HomeViewAction.OnRefresh) })

    LaunchedEffect(Unit) {
        viewModel.dispatch(HomeViewAction.LoadData)
    }
    systemUiCtrl.setStatusBarColor(viewStates.atmosphereModel.topBgColor)
    systemUiCtrl.setNavigationBarColor(viewStates.atmosphereModel.topBgColor)
    systemUiCtrl.setSystemBarsColor(viewStates.atmosphereModel.topBgColor)

    ArkBasePage(viewModel = viewModel,
        modifier = Modifier
            .fillMaxSize()
            .background(COLOR_F5F5F5)
            .pullRefresh(state = pullRefreshState, enabled = true),
        onRetry = {
            viewModel.dispatch(HomeViewAction.LoadData)
        }) {
        Column {
            Row(
                modifier = Modifier
                    .background(viewStates.atmosphereModel.topBgColor)
                    .fillMaxWidth()
                    .height(44.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                val topPicUrl = viewStates.atmosphereModel.topBgPicUrl
                if (topPicUrl.isNotEmpty()) {
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = topPicUrl.toLoadUrl(),
                        contentDescription = "顶部氛围图",
                        contentScale = ContentScale.Crop
                    )
                }
                AsyncImage(
                    modifier = Modifier
                        .padding(12.dp, 0.dp, 12.dp, 0.dp)
                        .width(22.dp)
                        .height(22.dp),
                    model = R.drawable.ark_home_brand_icon,
                    contentDescription = "品牌"
                )
                Row(
                    modifier = Modifier
                        .weight(weight = 1f, fill = true)
                        .height(30.dp)
                        .clip(RoundedCornerShape(50))
                        .background(White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .padding(10.dp, 0.dp, 0.dp, 0.dp)
                            .width(14.dp)
                            .height(14.dp),
                        model = R.drawable.ark_home_search_icon,
                        contentDescription = "搜索"
                    )
                    Text(
                        modifier = Modifier
                            .padding(4.dp, 0.dp, 0.dp, 0.dp)
                            .weight(weight = 1f, fill = true),
                        text = viewStates.searchHint,
                        fontSize = 12.sp,
                        color = COLOR_999999,
                        overflow = TextOverflow.Ellipsis
                    )
                    AsyncImage(
                        modifier = Modifier
                            .padding(10.dp, 0.dp, 10.dp, 0.dp)
                            .width(22.dp)
                            .height(22.dp),
                        model = R.drawable.ark_home_camera_icon,
                        contentDescription = "拍照"
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(7.dp, 0.dp, 0.dp, 0.dp)
                        .width(32.dp)
                        .height(32.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .width(22.dp)
                            .height(22.dp)
                            .align(Alignment.Center),
                        model = R.drawable.ark_home_message_icon,
                        contentDescription = "消息"
                    )
                }
                AsyncImage(
                    modifier = Modifier
                        .padding(11.dp, 0.dp, 12.dp, 0.dp)
                        .width(22.dp)
                        .height(22.dp),
                    model = R.drawable.ark_home_share_icon,
                    contentDescription = "分享",
                    colorFilter = ColorFilter.tint(White)
                )
            }

            HomeTabViewPager(
                homeViewModel = viewModel, coroutineScope = coroutineScope
            )
        }

        //下拉刷新指示器
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = viewStates.isRefreshing,
            state = pullRefreshState,
            backgroundColor = White
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeTabViewPager(
    homeViewModel: HomeViewModel, coroutineScope: CoroutineScope
) {

    val viewStates = homeViewModel.viewStates

    val pageState = rememberPagerState(0)

    ScrollableTabRow(selectedTabIndex = pageState.currentPage,
        backgroundColor = viewStates.atmosphereModel.topBgColor,
        edgePadding = 0.dp,
        indicator = {
            val currentIndicatorWidth by animateDpAsState(
                targetValue = (viewStates.tabList[pageState.currentPage].name.length * 10).dp,
                animationSpec = tween(
                    durationMillis = 250, easing = FastOutSlowInEasing
                )
            )
            val indicatorOffset by animateDpAsState(
                targetValue = it[pageState.currentPage].left + (it[pageState.currentPage].width - currentIndicatorWidth) / 2,
                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
            )
            Indicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.BottomStart)
                    .width(currentIndicatorWidth)
                    .height(2.dp)
                    .offset(x = indicatorOffset)
                    .clip(RoundedCornerShape(1.dp)),
                color = viewStates.atmosphereModel.tabSelectColor
            )
        }) {
        viewStates.tabList.forEachIndexed { index, tabModel ->
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(40.dp)
                    .clickable {
                        coroutineScope.launch {
                            pageState.scrollToPage(index)
                        }
                    }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tabModel.name,
                    fontSize = if (pageState.currentPage == index) 17.sp else 15.sp,
                    color = if (pageState.currentPage == index) viewStates.atmosphereModel.tabSelectColor
                    else viewStates.atmosphereModel.tabUnSelectColor,
                    fontWeight = if (pageState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        count = viewStates.tabList.size,
        state = pageState,
        userScrollEnabled = false
    ) { page ->
        val tab = viewStates.tabList[page]
        when (tab.tabType) {
            0 -> {
                HomeFirstPage(homeViewModel = homeViewModel)
            }
            else -> {
                Text(modifier = Modifier.fillMaxSize(), text = "${page}")
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeFirstPage(homeViewModel: HomeViewModel) {

    val viewModel: HomeFirstViewModel = viewModel()
    val homeViewStates = homeViewModel.viewStates
    val loadStatus = viewModel.viewStates.loadStatus
    val coroutineScope = rememberCoroutineScope()

    val lazyColumnState = rememberLazyListState()
    val capsuleModel = homeViewModel.viewStates.capsuleModel
    val marketModel = homeViewModel.viewStates.marketModel
    var stickerIndex = 0
    if (capsuleModel.visible) stickerIndex++
    if (marketModel.visible) stickerIndex++
    var currentPageIndex by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(Unit) {
        viewModel.dispatch(
            HomeFirstViewAction.LoadData(
                homeViewStates.homeMainTabModel.list.getOrNull(
                    currentPageIndex
                )
            )
        )
    }
    if (viewModel.viewStates.refresh && lazyColumnState.firstVisibleItemIndex >= stickerIndex) {
        coroutineScope.launch {
            lazyColumnState.scrollToItem(stickerIndex)
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(), state = lazyColumnState
    ) {
        // 胶囊页
        if (capsuleModel.visible) {
            item {
                Capsule(capsuleModel = capsuleModel)
            }
        }
        // 营销页
        if (marketModel.visible) {
            item {
                Market(list = marketModel.list)
            }
        }
        // 会场tab
        if (homeViewModel.viewStates.homeMainTabModel.list.isNotEmpty()) {
            stickyHeader {
                HomeExhibitionTab(currentPageIndex = currentPageIndex,
                    backgroundColor = if (lazyColumnState.firstVisibleItemIndex >= stickerIndex) White else COLOR_F5F5F5,
                    model = homeViewModel.viewStates.homeMainTabModel,
                    onTabClick = { index ->
                        currentPageIndex = index
                        viewModel.dispatch(
                            HomeFirstViewAction.LoadData(
                                homeViewStates.homeMainTabModel.list.getOrNull(
                                    index
                                )
                            )
                        )
                    })
            }
        }
        if (viewModel.viewStates.refresh) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalConfiguration.current.screenHeightDp.dp)
                        .padding(top = 20.dp, bottom = 20.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator(color = ThemeColor)
                }
            }
        } else {
            // 会场商品内容
            val goodsList = viewModel.viewStates.list
            if (goodsList.isNotEmpty()) {
                goodsList.forEachIndexed { index, item ->
                    item {
                        HomeExhibitionOrBrand(item = item)
                        if (loadStatus == LoadStatus.LOAD_COMPLETE && index + 3 >= goodsList.size) {
                            viewModel.dispatch(HomeFirstViewAction.LoadMore)
                        }
                    }
                }
            }
            when (loadStatus) {
                LoadStatus.LOADING -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "加载中")
                        }
                    }
                }
                LoadStatus.EMPTY -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "暂无数据")
                        }
                    }
                }
            }
            item {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = com.nsyw.nsyw_base.R.drawable.base_common_footer),
                    contentDescription = null
                )
            }
        }
    }

}

@Composable
fun Capsule(capsuleModel: HomeCapsuleModel) {
    capsuleModel.list.forEach { model ->
        when (model) {
            is HomeCapsuleImageModel -> {
                CapsuleImage(model = model)
            }
            is HomeCapsuleBannerModel -> {
                CapsuleBanner(model = model)
            }
            is HomeCapsuleHotAreaModel -> {
                CapsuleHotArea(model = model)
            }
        }
    }
}

@Composable
fun CapsuleImage(model: HomeCapsuleImageModel) {
    Box(
        modifier = Modifier
            .padding(
                model.sideMargin.dp, model.topMargin.dp, model.sideMargin.dp, 0.dp
            )
            .fillMaxWidth()
            .aspectRatio(model.ratio)
            .clip(RoundedCornerShape(model.radius.dp))
            .background(model.bgColor)
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = model.imageUrl.toLoadUrl(),
            contentDescription = "胶囊页活动单图"
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CapsuleBanner(model: HomeCapsuleBannerModel) {
    Banner(list = model.list, contentRadius = model.radius, modifier = Modifier
        .padding(
            model.sideMargin.dp, model.topMargin.dp, model.sideMargin.dp, 0.dp
        )
        .aspectRatio(model.ratio)
        .fillMaxWidth(), onClick = {})
}

@Composable
fun CapsuleHotArea(model: HomeCapsuleHotAreaModel) {
    Box(
        modifier = Modifier
            .padding(
                model.sideMargin.dp, model.topMargin.dp, model.sideMargin.dp, 0.dp
            )
            .fillMaxWidth()
            .aspectRatio(model.ratio)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(model.radius.dp))
                .background(model.bgColor)
                .clipToBounds(),
            model = model.imgUrl,
            contentDescription = "热区图片"
        )
        model.list.forEach { area ->
            Spacer(modifier = Modifier
                .padding(area.start.dp, area.top.dp, 0.dp, 0.dp)
                .width(area.width.dp)
                .height(area.height.dp)
                .clickable {
                    ToastUtil.showToast(area.linkType.toString())
                })
        }
    }
}

@Composable
fun Market(list: MutableList<ITypeModel>) {
    Column(
        modifier = Modifier
            .padding(12.dp, 10.dp, 12.dp, 0.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .background(White)
    ) {
        list.forEachIndexed { index, model ->
            when (model) {
                is HomeHotCokeModel -> {
                    MarketHotCoke(model = model)
                }
                is HomeSecKillAndRankModel -> {
                    if (index > 0) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.5.dp), color = COLOR_F5F5F5
                        )
                    }
                    SecKillAndRank(model = model)
                }
            }
        }
    }
}

@Composable
fun MarketHotCoke(model: HomeHotCokeModel) {
    Column(modifier = Modifier.padding(top = 15.dp, bottom = 12.dp)) {
        Box(
            modifier = Modifier
                .padding(12.dp, 0.dp, 12.dp, 12.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                modifier = Modifier.align(Alignment.CenterStart),
                model = model.titleUrl,
                contentDescription = null
            )
            AsyncImage(
                modifier = Modifier.align(Alignment.CenterEnd),
                model = R.drawable.ark_home_hot_coke_more,
                contentDescription = null
            )
        }
        LazyRow(
            modifier = Modifier
                .padding(12.dp, 0.dp, 0.dp, 0.dp)
                .fillMaxWidth()
        ) {
            model.pitemList?.forEach { item ->
                item {
                    Column(
                        modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(82.dp)
                                .height(82.dp)
                        ) {
                            AsyncImage(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape = RoundedCornerShape(2.dp)),
                                model = item.picture,
                                contentDescription = null
                            )
                            AsyncImage(
                                modifier = Modifier.height(6.dp),
                                model = item.pictureBadge,
                                contentDescription = null
                            )
                        }
                        Text(
                            modifier = Modifier.padding(0.dp, 2.dp, 0.dp, 0.dp),
                            text = item.price,
                            color = COLOR_101010,
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.din_medium))
                        )
                        Text(
                            modifier = Modifier
                                .padding(0.dp, 5.dp, 0.dp, 0.dp)
                                .width(79.dp)
                                .clip(shape = RoundedCornerShape(10.dp))
                                .background(COLOR_FFF5F0)
                                .padding(8.dp, 2.dp, 0.dp, 2.dp),
                            text = item.sellingPoint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = COLOR_FF4D18,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SecKillAndRank(model: HomeSecKillAndRankModel) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        if (model.homeSecKillModel.show xor model.homeRankModel.visible) {
            if (model.homeSecKillModel.show) {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .weight(weight = 1f, fill = true)
                ) {
                    HomeSecKill(model = model.homeSecKillModel)
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .weight(weight = 1f, fill = true)
                ) {
                    HomeRank(model = model.homeRankModel)
                }
            }
            Spacer(modifier = Modifier.weight(weight = 1f, fill = true))
        } else {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
                    .weight(weight = 1f, fill = true)
            ) {
                HomeSecKill(model = model.homeSecKillModel)
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(0.5.dp), color = COLOR_F5F5F5
            )
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
                    .weight(weight = 1f, fill = true)
            ) {
                HomeRank(model = model.homeRankModel)
            }
        }
    }
}

@Composable
fun HomeSecKill(model: HomeSecKillModel) {
    Column(
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier.height(20.dp), model = model.titleUrl, contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = model.introduction,
            color = COLOR_929292,
            fontSize = 12.sp
        )
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
        ) {
            val goodsOne = model.goodsOne
            if (goodsOne.show) {
                Box(
                    modifier = Modifier.weight(weight = 1f, fill = true)
                ) {
                    SecKillGoods(goods = goodsOne)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            val goodsTwo = model.goodsTwo
            if (goodsTwo.show) {
                Box(modifier = Modifier.weight(weight = 1f, fill = true)) {
                    SecKillGoods(goods = goodsOne)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f, fill = true))
            }
        }
    }
}

@Composable
fun SecKillGoods(goods: HomeHelpOrderGoodsModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(2.dp)),
            model = goods.imageUrl,
            contentDescription = null
        )
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(bottom = 1.dp),
                text = "¥",
                fontSize = 10.sp,
                color = COLOR_FD3D04,
                fontFamily = FontFamily(Font(R.font.din_bold))
            )
            Text(
                text = goods.price,
                fontSize = 15.sp,
                color = COLOR_FD3D04,
                fontFamily = FontFamily(Font(R.font.din_bold))
            )
            Text(
                modifier = Modifier.padding(bottom = 1.dp),
                text = goods.originPrice,
                fontSize = 12.sp,
                color = COLOR_999999,
                fontFamily = FontFamily(Font(R.font.din_regular)),
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}

@Composable
fun HomeRank(model: HomeRankModel) {
    Column(
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp)
            .fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier.height(20.dp), model = model.titleUrl, contentDescription = null
        )
        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = model.introduction,
            color = COLOR_929292,
            fontSize = 12.sp
        )
        Row(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
        ) {
            val goodsOne = model.goods1
            if (goodsOne.show) {
                Box(
                    modifier = Modifier.weight(weight = 1f, fill = true)
                ) {
                    RankGoods(goods = goodsOne)
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            val goodsTwo = model.goods2
            if (goodsTwo.show) {
                Box(modifier = Modifier.weight(weight = 1f, fill = true)) {
                    RankGoods(goods = goodsOne)
                }
            } else {
                Spacer(modifier = Modifier.weight(1f, fill = true))
            }
        }
    }
}

@Composable
fun RankGoods(goods: HomeRankGoodsModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(2.dp)),
            model = goods.imageUrl,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .border(width = 0.5.dp, color = COLOR_FFE1D4, shape = RoundedCornerShape(20.dp))
                .padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp),
            text = goods.saleCountDesc,
            color = COLOR_FF4D18,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HomeExhibitionTab(
    currentPageIndex: Int,
    backgroundColor: Color, model: HomeMainTabEntryModel, onTabClick: (index: Int) -> Unit
) {

    ScrollableTabRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = 10.dp, bottom = 10.dp),
        selectedTabIndex = currentPageIndex,
        indicator = {},
        edgePadding = 0.dp,
        backgroundColor = backgroundColor,
        contentColor = backgroundColor
    ) {
        model.list.forEachIndexed { index, tab ->
            Column(
                modifier = Modifier.clickable {
                    onTabClick.invoke(index)
                }, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = tab.title,
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = tab.subTitle,
                    color = if (index == currentPageIndex) Color.Black else COLOR_999999,
                    fontSize = 11.sp,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (index == currentPageIndex) ThemeColor else COLOR_00_FFFFFF)
                        .padding(4.dp, 2.dp, 4.dp, 3.dp)
                )
            }
        }
    }
}

@Composable
fun HomeExhibitionOrBrand(item: ITypeModel) {
    when (item) {
        is HomeExhibitionModel -> {
            HomeExhibition(item = item)
        }
        is HomeSingleExhibitionModel -> {
            HomeSingleExhibition(item = item)
        }
        is HomeBrandModel -> {
            HomeBrand(item = item)
        }
        is HomeSingleBrandModel -> {
            HomeSingleBrand(item = item)
        }
    }
}

@Composable
fun HomeExhibition(item: HomeExhibitionModel) {
    Column(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .background(White)
    ) {
        if (item.showActivity) {
            Box(modifier = Modifier.paint(painterResource(id = R.drawable.ark_home_exhibition_activity_bg))) {
                Text(
                    text = item.activityDesc,
                    modifier = Modifier
                        .width(250.dp)
                        .align(Alignment.CenterEnd),
                    color = COLOR_333333,
                    fontSize = 13.sp,
                    textAlign = TextAlign.End
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(start = 4.dp, top = 10.dp)
                .fillMaxWidth()
                .height(62.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(62.dp)
                    .height(62.dp), contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(46.dp)
                        .height(46.dp)
                        .border(
                            width = 0.5f.dp,
                            color = COLOR_EEEEEE,
                            shape = RoundedCornerShape(6.dp)
                        ),
                    model = item.logoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                if (item.delisted) {
                    Text(
                        modifier = Modifier
                            .width(46.dp)
                            .height(46.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(COLOR_80_666666),
                        text = "已下架",
                        color = White,
                        fontSize = 10.sp
                    )
                }
                AsyncImage(
                    modifier = Modifier
                        .width(38.dp)
                        .height(15.dp),
                    model = item.addPriceFlag,
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                ) {
                    Row {
                        item.tagList.forEach { tag ->
                            AsyncImage(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .width(tag.width.dp)
                                    .height(tag.height.dp),
                                model = tag.imageUrl,
                                contentDescription = null
                            )
                        }
                    }
                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.title,
                        color = COLOR_333333,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.countDownPrefix, color = COLOR_333333, fontSize = 12.sp
                    )
                    CountDownTime(endTime = item.endTime)
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (item.fullMinusIcon.isNotEmpty() && item.fullMinusName.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .border(
                                    width = 0.5.dp,
                                    color = COLOR_FD3D04,
                                    shape = RoundedCornerShape(2.dp)
                                )
                                .background(
                                    COLOR_FFF8F5
                                )
                                .wrapContentWidth()
                        ) {
                            AsyncImage(model = item.fullMinusIcon, contentDescription = null)
                            Text(
                                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                                text = item.fullMinusName,
                                color = COLOR_FD3D04,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Text(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .wrapContentWidth()
                            .border(
                                width = 0.5.dp,
                                color = COLOR_FD3D04,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .background(
                                COLOR_FFF8F5
                            )
                            .padding(start = 4.dp, end = 4.dp),
                        text = item.tag1,
                        color = COLOR_FD3D04,
                        fontSize = 12.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .wrapContentWidth()
                            .border(
                                width = 0.5.dp,
                                color = COLOR_FD3D04,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .background(
                                COLOR_FFF8F5
                            )
                            .padding(start = 4.dp, end = 4.dp),
                        text = item.tag2,
                        color = COLOR_FD3D04,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    when (item.btnType) {
                        0 -> {
                            Text(
                                modifier = Modifier
                                    .width(66.dp)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(ThemeColor)
                                    .padding(top = 3.dp, bottom = 3.dp),
                                text = "分享",
                                color = COLOR_101013,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        1 -> {
                            Text(
                                modifier = Modifier
                                    .width(66.dp)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(COLOR_48A670)
                                    .padding(top = 3.dp, bottom = 3.dp),
                                text = "提醒我",
                                color = White,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        2 -> {
                            Text(
                                modifier = Modifier
                                    .width(66.dp)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(COLOR_F5F5F5)
                                    .padding(top = 3.dp, bottom = 3.dp),
                                text = "已提醒",
                                color = COLOR_999999,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(6.dp)
        ) {
            item.goodsList.forEach {
                when (it) {
                    is HomeExhibitionGoodsModel -> {
                        item {
                            HomeGoods(item = it)
                        }
                    }
                    is HomeGoodsMoreModel -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .width(34.dp)
                                    .height(110.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "查\n看\n更\n多", color = COLOR_999999, fontSize = 12.sp)
                                Image(
                                    modifier = Modifier.padding(top = 4.dp),
                                    painter = painterResource(id = R.drawable.home_row_more),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeSingleExhibition(item: HomeSingleExhibitionModel) {
    Column(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .background(White)
            .padding(12.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(215.dp)
                    .clip(RoundedCornerShape(3.dp)),
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            if (item.isSoldOut) {
                Image(
                    painter = painterResource(id = R.drawable.home_goods_sold_out),
                    contentDescription = null,
                )
            } else {
                Image(
                    modifier = Modifier
                        .padding(top = 17.dp, end = 17.dp)
                        .align(Alignment.TopEnd),
                    painter = painterResource(id = R.drawable.home_single_brand_share),
                    contentDescription = null,
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .height(50.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = item.exhibitionName,
                    color = COLOR_333333,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.interestPoint,
                    color = COLOR_FD3D04,
                    fontSize = 11.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.End) {
                Row(modifier = Modifier.height(16.dp), verticalAlignment = Alignment.Bottom) {
                    Text(text = item.countDownPrefix, color = COLOR_666666, fontSize = 12.sp)
                    CountDownTime(endTime = item.endTime)
                }
                if (item.isSoldOut) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(34.dp))
                            .background(ThemeColor)
                            .padding(start = 10.dp, end = 10.dp),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "找相似",
                            color = COLOR_101010,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxHeight()) {
                        Image(
                            painter = painterResource(id = item.btnBgRes), contentDescription = null
                        )
                        Row(
                            modifier = Modifier
                                .width(73.dp)
                                .align(Alignment.TopStart),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(text = "¥", color = White, fontSize = 10.sp)
                            Text(
                                text = item.price,
                                color = White,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(R.font.din_medium)
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            modifier = Modifier
                                .width(73.dp)
                                .align(Alignment.BottomStart),
                            text = item.commission,
                            color = COLOR_FEBF11,
                            fontSize = 9.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeBrand(item: HomeBrandModel) {
    Column(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .background(White)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 4.dp, top = 10.dp)
                .fillMaxWidth()
                .height(62.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(62.dp)
                    .height(62.dp), contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(46.dp)
                        .height(46.dp)
                        .border(
                            width = 0.5f.dp,
                            color = COLOR_EEEEEE,
                            shape = RoundedCornerShape(6.dp)
                        ),
                    model = item.logoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
                if (item.delisted) {
                    Text(
                        modifier = Modifier
                            .width(46.dp)
                            .height(46.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(COLOR_80_666666),
                        text = "已下架",
                        color = White,
                        fontSize = 10.sp
                    )
                }
                AsyncImage(
                    modifier = Modifier
                        .width(38.dp)
                        .height(15.dp),
                    model = item.addPriceFlag,
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp)
                ) {
                    LazyRow {
                        item.tagList.forEach { tag ->
                            item {
                                AsyncImage(
                                    modifier = Modifier
                                        .padding(end = 4.dp)
                                        .width(tag.width.dp)
                                        .height(tag.height.dp),
                                    model = tag.imageUrl,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                    Text(
                        modifier = Modifier.weight(1f),
                        text = item.title,
                        color = COLOR_333333,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = item.countDownPrefix, color = COLOR_333333, fontSize = 12.sp
                    )
                    CountDownTime(endTime = item.endTime)
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .wrapContentWidth()
                            .border(
                                width = 0.5.dp,
                                color = COLOR_FD3D04,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .background(
                                COLOR_FFF8F5
                            )
                            .padding(start = 4.dp, end = 4.dp),
                        text = item.tag1,
                        color = COLOR_FD3D04,
                        fontSize = 12.sp
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .wrapContentWidth()
                            .border(
                                width = 0.5.dp,
                                color = COLOR_FD3D04,
                                shape = RoundedCornerShape(2.dp)
                            )
                            .background(
                                COLOR_FFF8F5
                            )
                            .padding(start = 4.dp, end = 4.dp),
                        text = item.tag2,
                        color = COLOR_FD3D04,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    when (item.btnType) {
                        0 -> {
                            Text(
                                modifier = Modifier
                                    .width(66.dp)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(ThemeColor)
                                    .padding(top = 3.dp, bottom = 3.dp),
                                text = "分享",
                                color = COLOR_101013,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        1 -> {
                            Text(
                                modifier = Modifier
                                    .width(66.dp)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(COLOR_48A670)
                                    .padding(top = 3.dp, bottom = 3.dp),
                                text = "提醒我",
                                color = White,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        2 -> {
                            Text(
                                modifier = Modifier
                                    .width(66.dp)
                                    .clip(
                                        RoundedCornerShape(20.dp)
                                    )
                                    .background(COLOR_F5F5F5)
                                    .padding(top = 3.dp, bottom = 3.dp),
                                text = "已提醒",
                                color = COLOR_999999,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(6.dp)
        ) {
            item.goodsList.forEach {
                when (it) {
                    is HomeExhibitionGoodsModel -> {
                        item {
                            HomeGoods(item = it)
                        }
                    }
                    is HomeGoodsMoreModel -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .width(34.dp)
                                    .height(110.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "查\n看\n更\n多", color = COLOR_999999, fontSize = 12.sp)
                                Image(
                                    modifier = Modifier.padding(top = 4.dp),
                                    painter = painterResource(id = R.drawable.home_row_more),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeSingleBrand(item: HomeSingleBrandModel) {
    Column(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .background(White)
            .padding(12.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(215.dp)
                    .clip(RoundedCornerShape(3.dp)),
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            if (item.isSoldOut) {
                Image(
                    painter = painterResource(id = R.drawable.home_goods_sold_out),
                    contentDescription = null,
                )
            } else {
                Image(
                    modifier = Modifier
                        .padding(top = 17.dp, end = 17.dp)
                        .align(Alignment.TopEnd),
                    painter = painterResource(id = R.drawable.home_single_brand_share),
                    contentDescription = null,
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(top = 10.dp)
                .height(50.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = item.exhibitionName,
                    color = COLOR_333333,
                    fontSize = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.interestPoint,
                    color = COLOR_FD3D04,
                    fontSize = 11.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.End) {
                Row(modifier = Modifier.height(16.dp), verticalAlignment = Alignment.Bottom) {
                    Text(text = item.countDownPrefix, color = COLOR_666666, fontSize = 12.sp)
                    CountDownTime(endTime = item.endTime)
                }
                if (item.isSoldOut) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(34.dp))
                            .background(ThemeColor)
                            .padding(start = 10.dp, end = 10.dp),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "找相似",
                            color = COLOR_101010,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    Box(modifier = Modifier.fillMaxHeight()) {
                        Image(
                            painter = painterResource(id = item.btnBgRes), contentDescription = null
                        )
                        Row(
                            modifier = Modifier
                                .width(73.dp)
                                .align(Alignment.TopStart),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(text = "¥", color = White, fontSize = 10.sp)
                            Text(
                                text = item.price,
                                color = White,
                                fontSize = 16.sp,
                                fontFamily = FontFamily(
                                    Font(R.font.din_medium)
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            modifier = Modifier
                                .width(73.dp)
                                .align(Alignment.BottomStart),
                            text = item.commission,
                            color = COLOR_FEBF11,
                            fontSize = 9.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeGoods(item: HomeExhibitionGoodsModel) {
    Column(
        modifier = Modifier
            .padding(start = 6.dp)
            .width(110.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .width(110.dp)
                .height(110.dp)
                .clip(RoundedCornerShape(6.dp)),
            model = item.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Row(modifier = Modifier.height(20.dp), verticalAlignment = Alignment.Bottom) {
            Text(
                modifier = Modifier.padding(bottom = 1.dp),
                text = "¥",
                color = COLOR_333333,
                fontSize = 12.sp
            )
            Text(text = item.price, color = COLOR_333333, fontSize = 16.sp)
        }
        Text(
            modifier = Modifier.height(20.dp),
            text = item.commission,
            color = COLOR_FD3D04,
            fontSize = 12.sp
        )
    }
}

@Composable
fun CountDownTime(endTime: Long) {
    var countDownTimeStr by remember {
        mutableStateOf("")
    }
    DisposableEffect(Unit) {
        val time = endTime - System.currentTimeMillis()
        var timer = object : CountDownTimer(time, 1000) {
            override fun onTick(p0: Long) {
                val hour = p0 / 1000 / 60 / 60
                val minute = p0 / 1000 / 60 % 60
                val second = p0 / 1000 % 60
                countDownTimeStr = "${hour}:${minute}:${second}"
            }

            override fun onFinish() {
                countDownTimeStr = ""
            }
        }.start()
        this.onDispose {
            timer.cancel()
            timer = null
        }
    }
    Text(
        text = countDownTimeStr,
        modifier = Modifier.padding(start = 6.dp),
        color = COLOR_80_666666,
        fontSize = 12.sp
    )
}
