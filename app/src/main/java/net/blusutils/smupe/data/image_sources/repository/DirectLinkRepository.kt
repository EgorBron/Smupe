package net.blusutils.smupe.data.image_sources.repository

import android.net.Uri
import net.blusutils.smupe.data.image_sources.models.contents.ImageResponse
import net.blusutils.smupe.data.image_sources.models.sources.Authorization
import net.blusutils.smupe.data.image_sources.models.sources.InternetImageSource
import net.blusutils.smupe.data.image_sources.models.sources.SearchUnavailable
import net.blusutils.smupe.data.image_sources.models.sources.SourceMode
import okhttp3.Response

class DirectLinkRepository(
    override var name: String,
    override var icon: String?,
    override var description: String?,
    override var apiLink: String,
    override var authorization: Authorization?,
) : InternetImageSource(
    name,
    apiLink = apiLink,
    dataPathSource = null,
    sourcePath = "",
    mode = SourceMode.InternetDirectLink,
) {

    override fun serializeResponse(input: String): ImageResponse? {
        TODO()
    }
    override fun serializeSearchResponse(input: String): ImageResponse? {
        throw SearchUnavailable()
    }


    override fun requestWithCallback(callback: (response: ImageResponse?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun requestRawWithCallback(callback: (response: Uri?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun requestAsync(): ImageResponse? =
        ImageResponse(
            apiLink, name, apiLink,
            occurredException = null
        )

    override suspend fun requestRawAsync(): Uri? =
        requestAsync()?.url?.run { Uri.parse(this) }

    override fun serializeResponse(response: Response): ImageResponse? =
        serializeResponse(response.body!!.source().readUtf8())

    override fun searchWithCallback(query: String, callback: (response: ImageResponse?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun searchRawWithCallback(query: String, callback: (response: Uri?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun searchAsync(query: String): ImageResponse {
        TODO("Not yet implemented")
    }

    override suspend fun searchRawAsync(query: String): Uri? {
        TODO("Not yet implemented")
    }

    override fun serializeSearchResponse(response: Response): ImageResponse? {
        TODO("Not yet implemented")
    }
}