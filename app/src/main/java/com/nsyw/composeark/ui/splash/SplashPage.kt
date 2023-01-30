package com.nsyw.composeark.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nsyw.nsyw_base.theme.ThemeColor
import com.nsyw.nsyw_base.theme.White
import kotlinx.coroutines.delay

/**
 * @author qianjiang
 * @date   2022/12/20
 */
@Composable
fun SplashPage(next: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ThemeColor),
        contentAlignment = Alignment.Center
    ) {
        LaunchedEffect(Unit) {
            delay(2000)
            next.invoke()
        }
        Text(
            text = "ARK\nCompose版",
            fontSize = 32.sp,
            color = White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 20.dp),
            textAlign = TextAlign.Center
        )
    }
}