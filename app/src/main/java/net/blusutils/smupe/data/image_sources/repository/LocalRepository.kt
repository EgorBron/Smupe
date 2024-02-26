package net.blusutils.smupe.data.image_sources.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import net.blusutils.smupe.data.image_sources.models.contents.ImageResponse
import net.blusutils.smupe.data.image_sources.models.sources.LocalImageSource
import okhttp3.Response
import java.io.File
import java.io.FileNotFoundException

class LocalRepository(
    override var name: String,
    override var icon: String?,
    override var description: String?,
    override var apiLink: String
) : LocalImageSource(
    name,
    apiLink = apiLink,
) {

    override fun serializeResponse(input: String): ImageResponse? {
        TODO("Not yet implemented")
    }
    override fun serializeSearchResponse(input: String): ImageResponse? {
        TODO("Not yet implemented")
    }


    override fun requestWithCallback(callback: (response: ImageResponse?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun requestRawWithCallback(callback: (response: Uri?) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun requestAsync(): ImageResponse? {
        var files = apiLink.toUri().toFile().listFiles()?.filter { it.isFile && !it.name.startsWith(".") }
        Log.d("srcsss", "$apiLink, $files")
        if (files!=null && files.isEmpty()) { files = null }
        // Iterate through the files in the directory and return random one
        return files?.random()?.let {
            GalleryImageInfo(
                id = it.name,
                url = it.absolutePath
            )
        } ?: ImageResponse("", source = name, url = null, occurredException = FileNotFoundException("No files found in gallery"))

    }

    override suspend fun requestRawAsync(): Uri? =
        requestAsync()?.url?.run { Uri.parse(this) }

    override fun serializeResponse(response: Response): ImageResponse? {
        TODO("Not yet implemented")
    }

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