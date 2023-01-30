package com.nsyw.nsyw_base.widget.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nsyw.nsyw_base.theme.COLOR_60_FFFFFF
import com.nsyw.nsyw_base.theme.ThemeColor
import com.nsyw.nsyw_base.theme.White

/**
 * @author qianjiang
 * @date   2022/12/21
 */
@Composable
fun BottomNavigationBarView(navCtrl: NavHostController) {
    val bottomNavList = listOf(
        BottomNavRoute.Home,
        BottomNavRoute.ShoppingCart,
        BottomNavRoute.Mine,
    )
    BottomNavigation {
        val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomNavList.forEach { screen ->
            BottomNavigationItem(
                modifier=Modifier.background(ThemeColor),
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.desc
                    )
                },
                label = {
                    Text(
                        text = screen.desc
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.routeName } == true,
                selectedContentColor = White,
                unselectedContentColor = COLOR_60_FFFFFF,
                onClick = {
                    if (currentDestination?.route != screen.routeName) {
                        navCtrl.navigate(screen.routeName){
                            popUpTo(navCtrl.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}