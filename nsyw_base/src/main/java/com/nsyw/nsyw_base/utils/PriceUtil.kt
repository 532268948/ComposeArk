package com.nsyw.nsyw_base.utils

import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * @author qianjiang
 * @date   2023/1/13
 */
object PriceUtil {
    fun getPrice(price: Long): String? {
        return if (price == 0L) {
            "0"
        } else try {
            val decimalFormat = DecimalFormat("0.00")
            val a = BigDecimal(decimalFormat.format(price / 100.0))
            a.stripTrailingZeros().toPlainString()
        } catch (e: Exception) {
            (price / 100.0).toString()
        }
    }
}