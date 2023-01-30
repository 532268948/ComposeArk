package com.nsyw.composeark.bean

/**
 * @author qianjiang
 * @date   2023/1/10
 */
data class DrainagesBean(
    // 大家都在搜
    val hotSearchWords: List<ShadingWordBean>? = null,
    // 底纹词
    val shadingWords: List<ShadingWordBean>? = null,
)

data class ShadingWordBean(
    val id: Long? = null,
    val name: String? = null,
    val attributes: Attributes? = null,
)

data class Attributes(
    val keyWord: String? = null,
    val relations: List<Relations>? = null
) {
    class Relations(
        val id: String? = null,
        val url: String? = null,
        val type: Int? = null,
        val name: String? = null
    )
}