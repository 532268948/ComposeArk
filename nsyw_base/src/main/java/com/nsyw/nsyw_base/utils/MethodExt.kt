package com.nsyw.base.utils

import android.webkit.URLUtil
import com.google.gson.Gson
import com.nsyw.nsyw_base.Constant.IMAGE_URL_PREFIX
import java.lang.reflect.Type
import java.math.BigDecimal

/**
 * @author qianjiang
 * @date   2023/1/5
 */
fun Any?.toJson(): String {
    return try {
        Gson().toJson(this)
    } catch (e: Exception) {
        ""
    }
}

fun <T> String.fromJson(typeOfT: Type): T? {
    return try {
        Gson().fromJson(this, typeOfT)
    } catch (e: Throwable) {
        null
    }
}

fun String?.toLoadUrl(): String {
    return if (this == null) {
        ""
    } else if (this.isEmpty() || this.isNetworkUrl()) {
        this
    } else {
        IMAGE_URL_PREFIX + this
    }
}

fun String?.isNetworkUrl(): Boolean {
    return URLUtil.isNetworkUrl(this)
}

fun Number.formatMoney(
    isYuan: Boolean = false,
    trans2W: Boolean = false,
    scale: Int = 2,
    stripTrailingZeros: Boolean = true
): String {
    val moneyF = if (isYuan) {
        this.toDouble()
    } else {
        // 分转为元
        this.toDouble() / 100
    }
    try {
        when {
            trans2W && moneyF / 10000 > 0 -> {
                val formattedValue =
                    BigDecimal.valueOf(moneyF / 10000).setScale(1, BigDecimal.ROUND_DOWN)
                return if (stripTrailingZeros) {
                    formattedValue.stripTrailingZeros().toPlainString().plus("W")
                } else {
                    formattedValue.toPlainString().plus("W")
                }
            }
            else -> {
                val formattedValue =
                    BigDecimal.valueOf(moneyF).setScale(scale, BigDecimal.ROUND_DOWN)
                return if (stripTrailingZeros) {
                    formattedValue.stripTrailingZeros().toPlainString()
                } else {
                    formattedValue.toPlainString()
                }
            }
        }
    } catch (e: Exception) {
        return moneyF.toString()
    }
}

fun String.getImageRatio(): Float {
    val indexOfSize = this.indexOf("___size")
    var indexOfX = -1
    if (indexOfSize != -1) {
        indexOfX = this.indexOf("x", indexOfSize)
    }
    var indexOfDot = -1
    if (indexOfX != -1) {
        indexOfDot = this.indexOf('.', indexOfX);
    }
    return if (indexOfSize == -1 || indexOfX == -1 || indexOfDot == -1) {
        1f
    } else {
        val width = this.substring(indexOfSize + 7, indexOfX).safeToFloat(1f)
        val height = this.substring(indexOfX + 1, indexOfDot).safeToFloat(1f)
        width / height
    }
}

fun String?.safeToFloat(default: Float = 0f): Float {
    if (this == null) return default
    return try {
        this.toFloat()
    } catch (e: Throwable) {
        default
    }
}