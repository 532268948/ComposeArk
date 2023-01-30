package com.nsyw.nsyw_base.widget.load

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.nsyw.nsyw_base.theme.COLOR_60_000000
import com.nsyw.nsyw_base.theme.COLOR_80_FFFFFF

/**
 * @author qianjiang
 * @date   2023/1/4
 */

@Composable
fun LoadingDialog(
    dialogState: Boolean
) {
    if (dialogState) {
        Dialog(
            onDismissRequest = { },
        ) {
            Surface(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = COLOR_60_000000
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(10.dp, 16.dp, 10.dp, 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.TopCenter),
                        color = COLOR_80_FFFFFF,
                        strokeWidth = 2.dp
                    )
                    Text(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        text = "加载中",
                        fontSize = 14.sp,
                        color = COLOR_80_FFFFFF
                    )
                }
            }

        }
    }
}