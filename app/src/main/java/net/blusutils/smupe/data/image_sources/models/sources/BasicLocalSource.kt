package net.blusutils.smupe.data.image_sources.models.sources
import android.net.Uri
import net.blusutils.smupe.data.image_sources.models.contents.ImageResponse
import okhttp3.Response

abstract class BasicLocalSource(
    override var name: String = "Local",
    override var icon: String? = null,
    override var description: String? = null,
    override var apiLink: String = "/storage/emulated/0/"
) : LocalImageSource(
    name,
    icon,
    description,
    apiLink = apiLink
) {
    override var authorization: Authorization?
        get() = null
        set(_) {}

    override suspend fun requestRawAsync(): Uri? =
        requestAsync()?.url.run {
            if (this != null)
                Uri.parse(this)
            else
                null
        }


    override fun requestRawWithCallback(callback: (response: Uri?) -> Unit) {
        requestWithCallback {
            callback(Uri.parse(it?.url))
        }
    }

    override fun serializeResponse(response: Response): ImageResponse =
        throw UnsupportedOperationException("Local sources not need to be serialized")

    override fun serializeResponse(input: String): ImageResponse {
        throw UnsupportedOperationException("Local sources not need to be serialized")
    }


    override fun searchRawWithCallback(query: String, callback: (response: Uri?) -> Unit) =
        throw SearchUnavailable()

    override suspend fun searchAsync(query: String): ImageResponse =
        ImageResponse("", source = name, url = null, occurredException = SearchUnavailable())

    override suspend fun searchRawAsync(query: String): Uri? = throw SearchUnavailable()

    override fun serializeSearchResponse(response: Response): ImageResponse = throw SearchUnavailable()

    override fun serializeSearchResponse(input: String): ImageResponse = throw SearchUnavailable()

    override fun searchWithCallback(query: String, callback: (response: ImageResponse?) -> Unit) =
        callback(ImageResponse("", source = name, url = null, occurredException = SearchUnavailable()))
}