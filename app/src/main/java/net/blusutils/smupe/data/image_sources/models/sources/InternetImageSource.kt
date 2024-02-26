package net.blusutils.smupe.data.image_sources.models.sources

import android.net.Uri
import net.blusutils.smupe.data.image_sources.models.contents.ImageResponse
import okhttp3.Response

open class InternetImageSource(
    override var name: String,
    override var icon: String? = null,
    override var description: String? = null,
    override val mode: SourceMode = SourceMode.Internet,
    override var sourcePath: String,
    override var dataPathSource: String?,
    override var apiLink: String,
    override var authorization: Authorization? = null,
    override var blurHash: BlurHashInfo? = BlurHashInfo(false, ""),
    override var search: SearchInfo? = SearchInfo(false, "", "", ""),
    override var tags: TagsInfo? = TagsInfo(false, ""),
): BaseImageSource(
    name,
    icon,
    description,
    mode,
    apiLink,
    sourcePath,
    dataPathSource,
    authorization,
    blurHash,
    search,
    tags
) {
    override fun requestWithCallback(callback: (response: ImageResponse?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun requestRawWithCallback(callback: (response: Uri?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun requestAsync(): ImageResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun requestRawAsync(): Uri? {
        TODO("Not yet implemented")
    }

    override fun serializeResponse(response: Response): ImageResponse? {
        TODO("Not yet implemented")
    }

    override fun serializeResponse(input: String): ImageResponse? {
        TODO("Not yet implemented")
    }

    override fun searchWithCallback(query: String, callback: (response: ImageResponse?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun searchRawWithCallback(query: String, callback: (response: Uri?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun searchAsync(query: String): ImageResponse? {
        TODO("Not yet implemented")
    }

    override suspend fun searchRawAsync(query: String): Uri? {
        TODO("Not yet implemented")
    }

    override fun serializeSearchResponse(response: Response): ImageResponse? {
        TODO("Not yet implemented")
    }

    override fun serializeSearchResponse(input: String): ImageResponse? {
        TODO("Not yet implemented")
    }
}