package com.nsyw.nsyw_base

import android.app.Application
import com.nsyw.nsyw_base.http.*

/**
 * @author qianjiang
 * @date   2022/12/20
 */
open class BaseApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        application = this
        RetrofitHelper.init(
            listOf(ArkInterceptor(), GateWayDomainInterceptor(), HeaderInterceptor(),VerifyInterceptor())
        )
    }

    companion object {
        lateinit var application: Application
    }
}