package com.nsyw.nsyw_base.utils

import android.widget.Toast
import com.nsyw.nsyw_base.BaseApplication

/**
 * @author qianjiang
 * @date   2022/12/23
 */
object ToastUtil {
    fun showToast(msg: String) {
        Toast.makeText(BaseApplication.application, msg, Toast.LENGTH_SHORT).show()
    }
}