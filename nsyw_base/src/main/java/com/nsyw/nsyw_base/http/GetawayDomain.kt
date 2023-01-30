package com.nsyw.nsyw_base.http

import com.nsyw.nsyw_base.Constant

/**
 * @author qianjiang
 * @date   2023/1/9
 */
object GetawayDomain {
    const val KEY_GATEWAY = "gateway"
    private const val KEY_GATEWAY_VALUE = "jlGateway"
    const val HEADERS_JL_GATEWAY = "${KEY_GATEWAY}:${KEY_GATEWAY_VALUE}"

    private val domainGateway = HashMap<String, String>()

    init {
        resetDomainGateway()
    }

    private fun resetDomainGateway() {
        domainGateway[KEY_GATEWAY_VALUE] = Constant.GATEWAY_URL
    }

    fun getGateway(key: String?): String? {
        key ?: return null
        return domainGateway[key]
    }

}