package com.nsyw.nsyw_base.utils

import android.content.Context
import com.nsyw.nsyw_base.BaseApplication

/**
 * @author qianjiang
 * @date   2022/12/20
 */
object SpUtil {

    private val mSharedPreferences =
        BaseApplication.application.getSharedPreferences(
            "Compose_ARK",
            Context.MODE_PRIVATE
        )

    fun save(name: String, data: Any) {
        when (data) {
            is Int -> {
                mSharedPreferences.edit().putInt(name, data).apply()
            }
            is String -> {
                mSharedPreferences.edit().putString(name, data).apply()
            }
            is Boolean -> {
                mSharedPreferences.edit().putBoolean(name, data).apply()
            }
        }
    }

    fun getString(name: String, defaultValue: String = ""): String? {
        return mSharedPreferences.getString(name, defaultValue)
    }

}