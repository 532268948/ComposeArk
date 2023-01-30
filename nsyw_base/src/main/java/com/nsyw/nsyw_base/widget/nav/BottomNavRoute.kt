package com.nsyw.nsyw_base.widget.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.nsyw.nsyw_base.route.RouteName

/**
 * @author qianjiang
 * @date   2022/12/21
 */
sealed class BottomNavRoute(
    var routeName: String,
    var desc: String,
    var icon: ImageVector
) {
    object Home : BottomNavRoute(RouteName.HOME, "首页", Icons.Default.Home)
    object ShoppingCart : BottomNavRoute(RouteName.SHOPPING_CART, "购物车", Icons.Default.ShoppingCart)
    object Mine : BottomNavRoute(RouteName.MINE, "我的", Icons.Default.Person)
}