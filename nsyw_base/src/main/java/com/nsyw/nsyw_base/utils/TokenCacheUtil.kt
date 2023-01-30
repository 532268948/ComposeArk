package com.nsyw.nsyw_base.utils

/**
 * @author qianjiang
 * @date   2023/1/9
 */
object TokenCacheUtil {

    private const val KEY_TOKEN = "key_token"

    /**
     * 保存 token
     */
    fun saveToken(token: String) {
        SpUtil.save(KEY_TOKEN, token)
    }

    /**
     * 获取  token
     */
    fun getToken(): String {
        return SpUtil.getString(KEY_TOKEN).orEmpty()
    }

    /**
     * 清空 token
     */
    fun clearToken() {
        saveToken("")
    }
}