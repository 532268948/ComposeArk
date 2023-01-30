package com.nsyw.composeark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nsyw.composeark.ui.EntryPage
import com.nsyw.nsyw_base.theme.ArkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArkTheme {
                EntryPage()
            }
        }
    }
}