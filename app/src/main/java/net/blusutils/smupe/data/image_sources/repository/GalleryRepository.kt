package net.blusutils.smupe.data.image_sources.repository

import android.os.Environment
import android.util.Log
import net.blusutils.smupe.data.image_sources.models.contents.ImageResponse
import net.blusutils.smupe.data.image_sources.models.sources.BasicLocalSource
import net.blusutils.smupe.data.image_sources.models.sources.BuiltinSource
import okio.FileNotFoundException
import java.io.File

object Gallery : BasicLocalSource(
    "Gallery",
    null, null,
    "/storage/emulated/0/${Environment.DIRECTORY_DCIM}/Camera",
), BuiltinSource {
    override suspend fun requestAsync(): ImageResponse {
        var files = File(apiLink).listFiles()?.filter { it.isFile && !it.name.startsWith(".") }
        if (files!=null && files.isEmpty()) { files = null }
        // Iterate through the files in the directory and return random one
        return files?.random()?.let {
            Log.d("Gallery", "Found file ${it.name}")
                GalleryImageInfo(
                    id = it.name,
                    url = it.absolutePath
                )
        } ?: ImageResponse("", source = "localgallery", url = null, occurredException = FileNotFoundException("No files found in gallery"))
    }

    override fun requestWithCallback(callback: (response: ImageResponse?) -> Unit) {
        var files = File(apiLink).listFiles()?.filter { it.isFile && !it.name.startsWith(".") }
        if (files!=null && files.isEmpty()) { files = null }
        callback(
            files?.random()?.let {
                Log.d("Gallery", "Found file ${it.name}")
                GalleryImageInfo(
                    id = it.name,
                    url = it.absolutePath
                )
            } ?: ImageResponse("", source = "localgallery", url = null, occurredException = FileNotFoundException("No files found in gallery"))
        )
    }
}

data class GalleryImageInfo(
    override val source: String = "localgallery",
    override val id: String,
    override val url: String
) : ImageResponse(
    source,
    id,
    url
)
