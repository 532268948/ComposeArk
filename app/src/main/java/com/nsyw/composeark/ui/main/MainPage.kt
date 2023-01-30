package com.nsyw.composeark.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nsyw.composeark.ui.home.HomePage
import com.nsyw.composeark.ui.home.HomeViewModel
import com.nsyw.nsyw_base.route.RouteName
import com.nsyw.nsyw_base.theme.White
import com.nsyw.nsyw_base.widget.nav.BottomNavigationBarView

/**
 * @author qianjiang
 * @date   2023/1/9
 */
@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainPage() {

    val navCtrl = rememberNavController()
    val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
//    val pagerState = rememberPagerState(0)

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = {
            when (currentDestination?.route) {
                RouteName.HOME -> BottomNavigationBarView(navCtrl = navCtrl)
                RouteName.SHOPPING_CART -> BottomNavigationBarView(navCtrl = navCtrl)
                RouteName.MINE -> BottomNavigationBarView(navCtrl = navCtrl)
            }
        },
        content = {
//            HorizontalPager(count = 3,
//            state = pagerState) {
//
//            }
            NavHost(
                modifier = Modifier
                    .padding(it)
                    .background(White),
                navController = navCtrl,
                startDestination = RouteName.HOME
            ) {
                // 首页
                composable(RouteName.HOME) {
                    HomePage()
                }
                // 购物车
                composable(RouteName.SHOPPING_CART) {
                    Text(text = "shopping_cart")
                }
                // 个人主页
                composable(RouteName.MINE) {
                    Text(text = "mine")
                }
            }
        }
    )
}