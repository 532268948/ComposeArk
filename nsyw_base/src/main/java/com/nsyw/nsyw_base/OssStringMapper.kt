package com.nsyw.nsyw_base

import android.net.Uri
import android.webkit.URLUtil
import androidx.core.net.toUri
import coil.map.Mapper
import coil.request.Options
import coil.size.Dimension
import coil.size.Scale
import coil.size.isOriginal
import coil.size.pxOrElse

/**
 * @author qianjiang
 * @date   2023/2/16
 */
class OssStringMapper : Mapper<String, Uri> {

    override fun map(data: String, options: Options) = toUri(data, options).toUri()

    private fun toUri(data: String, options: Options): String {
        var originUrl = data
        return if (data.isNotBlank() && !options.size.isOriginal) {
            val width = options.size.width.toPx(options.scale)
            var endIndex: Int =
                originUrl.lastIndexOf(OSS_MAX_SUFFIX)
            if (endIndex < 0) {
                endIndex =
                    originUrl.lastIndexOf(OSS_SMALL_SUFFIX)
            }
            if (endIndex >= 0) {
                originUrl = originUrl.substring(0, endIndex)
            }
            if (originUrl.isEmpty() || originUrl.isNetworkUrl()) {
                originUrl + OSS_PROCESS_IMAGE + OSS_RESIZE_W + width
            } else {
                IMAGE_URL_PREFIX + originUrl + OSS_PROCESS_IMAGE + OSS_RESIZE_W + width
            }
        } else {
            data
        }

    }

    companion object {
        const val IMAGE_URL_PREFIX = "https://cdn.webuy.ai/"

        const val OSS_MAX_SUFFIX = "!max"
        const val OSS_SMALL_SUFFIX = "!small"
        const val OSS_PROCESS_IMAGE = "?x-oss-process=image"
        const val OSS_RESIZE_W = "/resize,w_"
    }
}

fun Dimension.toPx(scale: Scale) = pxOrElse {
    when (scale) {
        Scale.FILL -> Int.MIN_VALUE
        Scale.FIT -> Int.MAX_VALUE
    }
}

fun String?.isNetworkUrl(): Boolean {
    return URLUtil.isNetworkUrl(this)
}