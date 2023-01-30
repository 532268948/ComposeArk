package com.nsyw.nsyw_base.theme

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationInstance
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = ThemeColor,
    surface = ThemeColor,
    onPrimary = ThemeColor,
    onSecondary = ThemeColor,
    onBackground = ThemeColor,
    onSurface = ThemeColor,
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = ThemeColor,
    surface = ThemeColor,
    onPrimary = ThemeColor,
    onSecondary = ThemeColor,
    onBackground = ThemeColor,
    onSurface = ThemeColor,
)

//@Composable
//fun ArkTheme(
//    content:@Composable ()->Unit
//){
//    val systemUiCtrl= rememberSystemUiController()
//    systemUiCtrl.setStatusBarColor(ThemeColor)
//    systemUiCtrl.setNavigationBarColor(ThemeColor)
//    systemUiCtrl.setSystemBarsColor(ThemeColor)
//    MaterialTheme(content = content)
//}

@Composable
fun ArkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val systemUiCtrl = rememberSystemUiController()
    systemUiCtrl.setStatusBarColor(ThemeColor)
    systemUiCtrl.setNavigationBarColor(ThemeColor)
    systemUiCtrl.setSystemBarsColor(ThemeColor)

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
    ){
        CompositionLocalProvider(
            LocalIndication provides NoIndication
        ) {
            ProvideTextStyle(value = MaterialTheme.typography.body1, content = content)
        }
    }
}


object NoIndication : Indication {
    private object NoIndicationInstance : IndicationInstance {
        override fun ContentDrawScope.drawIndication() {
            drawContent()
        }
    }

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        return NoIndicationInstance
    }
}