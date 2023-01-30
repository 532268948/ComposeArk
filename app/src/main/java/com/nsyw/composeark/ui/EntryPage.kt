package com.nsyw.composeark.ui

import androidx.compose.runtime.*
import com.nsyw.composeark.ui.main.MainPage
import com.nsyw.composeark.ui.splash.SplashPage

/**
 * @author qianjiang
 * @date   2023/1/9
 */
@Composable
fun EntryPage() {

    var isSplash by remember { mutableStateOf(true) }

    if (isSplash) {
        SplashPage {
            isSplash = false
        }
    } else {
        MainPage()
    }
}