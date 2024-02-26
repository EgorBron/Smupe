package net.blusutils.smupe.util

import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import ru.gildor.coroutines.okhttp.await
import java.io.IOException

/**
 * Helper class to the OkHttp library. Provides shorthands for invoking HTTP requests.
 */
object OkHttpUtils {

    val okHttpClient = OkHttpClient()

    fun getRequest(url: String) =
        Request.Builder().url(url).get().build()

    fun get(url: String, callback: (Response?, IOException?) -> Unit) {
        val req = getRequest(url)
        request(req, callback)
    }

    suspend fun getAsync(url: String): Response =
        okHttpClient.newCall(getRequest(url)).await()

    fun postRequest(url: String, data: String, contentType: MediaType? = null) =
        Request.Builder().url(url).post(data.toRequestBody(contentType)).build()

    fun post(url: String, data: String, contentType: MediaType? = null, callback: (Response?, IOException?) -> Unit) {
        val req = Request.Builder().url(url).post(data.toRequestBody(contentType)).build()
        request(req, callback)
    }

    suspend fun postAsync(url: String, data: String, contentType: MediaType? = null): Response =
        okHttpClient.newCall(postRequest(url, data, contentType)).await()

    private fun request(req: Request, callback: (Response?, IOException?) -> Unit) {
        okHttpClient.newCall(req).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null, e)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response, null)
            }
        })
    }
}