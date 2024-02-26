package net.blusutils.smupe.data.image_sources.repository

import android.net.Uri
import net.blusutils.smupe.data.image_sources.models.contents.ImageResponse
import net.blusutils.smupe.data.image_sources.models.sources.*
import net.blusutils.smupe.util.OkHttpUtils
import net.blusutils.smupe.util.getJsonValueFromPath
import okhttp3.Response
import java.io.IOException

class JsonRepository(
    override var name: String,
    override var icon: String?,
    override var description: String?,
    override var apiLink: String,
    override var search: SearchInfo?,
    override var authorization: Authorization?,
    override var blurHash: BlurHashInfo?,
    override var tags: TagsInfo?,
    override var sourcePath: String,
    override var dataPathSource: String?,
    var sourceIdPath: String?,
    var pathDelimiter: String?
) : InternetImageSource(
    name,
    apiLink = apiLink,
    dataPathSource = dataPathSource,
    sourcePath = sourcePath,
    mode = SourceMode.InternetJson
) {

    override fun serializeResponse(input: String): ImageResponse? {
        return getJsonValueFromPath(input, dataPathSource?:"", pathDelimiter?:",")?.let { id ->
            getJsonValueFromPath(input, sourcePath, pathDelimiter?:",")?.let { value ->
                ImageResponse(
                    id.toString(),
                    name,
                    value.toString()
                )
            }
        }
    }
    override fun serializeSearchResponse(input: String): ImageResponse? {
        return search?.let {
            getJsonValueFromPath(input, it.path, pathDelimiter?:",")?.let { id ->
                getJsonValueFromPath(input, it.idPath, pathDelimiter?:",")?.let { value ->
                    ImageResponse(
                        id.toString(),
                        name,
                        value.toString()
                    )
                }
            }
        } ?: if (search == null || !search!!.supported) null else throw SearchUnavailable()
    }


    override fun requestWithCallback(callback: (response: ImageResponse?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun requestRawWithCallback(callback: (response: Uri?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun requestAsync(): ImageResponse? {
        var err: IOException? = null
        val resp =
            try {
                OkHttpUtils.getAsync(apiLink)
            } catch (e: IOException) {
                err = e
                null
            }
        return resp?.let {
            if (it.isSuccessful)
                (serializeResponse(it))?.apply { occurredException = err }
            else
                ImageResponse("", source = name, url = null, occurredException = err)
        }
    }

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