package net.blusutils.smupe.data.image_sources.models.sources

import android.net.Uri
import net.blusutils.smupe.data.image_sources.models.contents.ImageResponse
import okhttp3.Response

abstract class BaseImageSource(
    open var name: String,
    open var icon: String? = null,
    open var description: String? = null,
    open val mode: SourceMode,
    open var apiLink: String,
    open var sourcePath: String,
    open var dataPathSource: String? = null,
    open var authorization: Authorization? = null,
    open var blurHash: BlurHashInfo? = null,
    open var search: SearchInfo? = null,
    open var tags: TagsInfo? = null,
    open var extra: Extra? = null,
) {
    abstract fun requestWithCallback(callback: (response: ImageResponse?) -> Unit)

    abstract fun requestRawWithCallback(callback: (response: Uri?) -> Unit)

    abstract suspend fun requestAsync(): ImageResponse?

    abstract suspend fun requestRawAsync(): Uri?

    abstract fun serializeResponse(response: Response): ImageResponse?

    abstract fun serializeResponse(input: String): ImageResponse?

    abstract fun searchWithCallback(query: String, callback: (response: ImageResponse?) -> Unit)

    abstract fun searchRawWithCallback(query: String, callback: (response: Uri?) -> Unit)

    abstract suspend fun searchAsync(query: String): ImageResponse?

    abstract suspend fun searchRawAsync(query: String): Uri?

    abstract fun serializeSearchResponse(response: Response): ImageResponse?

    abstract fun serializeSearchResponse(input: String): ImageResponse?
}