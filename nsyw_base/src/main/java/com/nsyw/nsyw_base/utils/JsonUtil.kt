package com.nsyw.base.utils

import android.os.Parcelable
import com.google.gson.Gson

/**
 * @author qianjiang
 * @date   2022/12/21
 */

fun Parcelable.toJson(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.fromJson(): T? {
    return try {
        Gson().fromJson(this, T::class.java)
    } catch (e: Exception) {
        null
    }
}