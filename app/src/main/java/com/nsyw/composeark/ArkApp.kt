package com.nsyw.composeark

import android.app.Application
import com.nsyw.nsyw_base.BaseApplication

/**
 * @author qianjiang
 * @date   2023/1/9
 */
class ArkApp: BaseApplication() {

    companion object{
        fun get():Application= application
    }
}