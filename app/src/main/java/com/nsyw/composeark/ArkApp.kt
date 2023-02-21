package com.nsyw.composeark

import android.app.Application
import coil.ComponentRegistry
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.map.Mapper
import com.nsyw.nsyw_base.BaseApplication
import com.nsyw.nsyw_base.OssStringMapper

/**
 * @author qianjiang
 * @date   2023/1/9
 */
class ArkApp : BaseApplication(), ImageLoaderFactory {

    companion object {
        fun get(): Application = application
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components(ComponentRegistry().newBuilder().add(OssStringMapper()).build()).build().apply {
                components
            }
    }
}

inline fun <reified T : Any> ComponentRegistry.replace(index: Int, mapper: Mapper<T, *>) =
    replace(index, mapper, T::class.java)

/** Register a [Mapper]. */
fun <T : Any> ComponentRegistry.replace(index: Int, mapper: Mapper<T, *>, type: Class<T>) = apply {
    mappers.mapIndexed { i, pair ->
        if (i == index) mapper to type else pair
    }
}
