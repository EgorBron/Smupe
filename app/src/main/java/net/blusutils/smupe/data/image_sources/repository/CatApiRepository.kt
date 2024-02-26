package net.blusutils.smupe.data.image_sources.repository

import android.net.Uri
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.blusutils.smupe.data.image_sources.models.contents.ImageResponse
import net.blusutils.smupe.data.image_sources.models.sources.BuiltinSource
import net.blusutils.smupe.data.image_sources.models.sources.InternetImageSource
import net.blusutils.smupe.data.image_sources.models.sources.SearchInfo
import net.blusutils.smupe.data.image_sources.models.sources.SearchUnavailable
import net.blusutils.smupe.util.OkHttpUtils
import okhttp3.Response
import java.io.IOException
import java.lang.reflect.Type

object CatApi : InternetImageSource(
    "Cats As A Service",
    null, null,
    apiLink = "https://api.thecatapi.com/v1/images/search",
    sourcePath = "",
    dataPathSource = null,
    search = SearchInfo(true, "https://api.thecatapi.com/v1/images/search", "","") // TODO!
), BuiltinSource {

    // IDK why it didn't work directly with generics
    private val listOfCatsType: Type =
        Types.newParameterizedType(
            MutableList::class.java,
            CatImageInfo::class.java
        )
    private val catAdapter: JsonAdapter<List<CatImageInfo>> =
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
            .adapter(listOfCatsType)

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
                        (serializeResponse(it)).apply { occurredException = err }
                else
                    ImageResponse("", source = "cataas", url = null, occurredException = err)
            }
    }

    override suspend fun requestRawAsync(): Uri? =
        requestAsync()?.url?.run { Uri.parse(this) }


    override fun requestRawWithCallback(callback: (response: Uri?) -> Unit) {
        requestWithCallback {
            callback(Uri.parse(it?.url))
        }
    }

    override fun requestWithCallback(callback: (response: ImageResponse?) -> Unit) {
        OkHttpUtils.get(apiLink) { resp, err ->
            var catResp: ImageResponse? = null
            if (err == null && resp != null && resp.isSuccessful)
                catResp = serializeResponse(resp)
            callback(
                catResp?.apply { occurredException = err }
                    ?: ImageResponse("", source = "cataas", url = null, occurredException = err)
            )
        }
    }

    override fun serializeResponse(response: Response): ImageResponse =
        serializeResponse(response.body!!.source().readUtf8())

    override fun serializeResponse(input: String): ImageResponse {
        return catAdapter.fromJson(input)?.getOrNull(0) as ImageResponse
    }

    override fun searchRawWithCallback(query: String, callback: (response: Uri?) -> Unit) =
        callback(null)

    override suspend fun searchAsync(query: String): ImageResponse =
        ImageResponse("", source = "cataas", url = null, occurredException = SearchUnavailable())

    override suspend fun searchRawAsync(query: String): Uri? = null

    override fun serializeSearchResponse(response: Response): ImageResponse? = null

    override fun serializeSearchResponse(input: String): ImageResponse? = null

    override fun searchWithCallback(query: String, callback: (response: ImageResponse?) -> Unit) =
        callback(ImageResponse("", source = "cataas", url = null, occurredException = SearchUnavailable()))
}

@JsonClass(generateAdapter = true)
data class CatImageInfo(
    override val source: String = "cataas",
    override val id: String,
    override val url: String,
    val width: Int,
    val height: Int,
    @Transient
    override var occurredException: Exception? = null
) : ImageResponse(
    source = source,
    id = id,
    url = url
)
