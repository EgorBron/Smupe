package net.blusutils.smupe.util

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import net.blusutils.smupe.util.OkHttpUtils.getRequest
import net.blusutils.smupe.util.OkHttpUtils.okHttpClient
import okio.IOException
import ru.gildor.coroutines.okhttp.await
import java.io.File

/**
 * A helper class for determining the file type of file.
 * Uses the file extension or the first few bytes of the file to determine the file type.
 */
object FileTypeAnalyzer {

    /**
     * Determines the file type of the given file.
     * @param input The [Uri]/[String] with path or URI or file as [ByteArray]/[Bitmap] to determine the file type of.
     * @return The file type extension.
     * @throws IllegalArgumentException if the file type could not be determined.
     * @throws IOException if the file could not be read.
     */
    suspend fun getExtension(input: Any): String {
        Log.d("FileTypeAnalyzer.getExtension", "$input")
        return when (input) {
            is String -> getExtensionFromUrlOrPath(input.toString())
            is Uri -> getExtensionFromUrlOrPath(input.toString())
            is ByteArray -> getExtensionFromBytes(input)
            is Bitmap -> getExtensionFromBitmap(input)
            else -> throw IllegalArgumentException("Unsupported input type: $input")
        }
    }

    /**
     * Determines the file type of the given Uri-like string.
     */
    private suspend fun getExtensionFromUrlOrPath(urlOrPath: String): String {
        // If URL, download first using OkHttp
        if (urlOrPath.startsWith("http")) {
            val response = okHttpClient.newCall(getRequest(urlOrPath)).await()
            if (response.isSuccessful) {
                return getExtensionFromBytes(response.body!!.bytes())
            } else {
                throw IOException("Failed to download file: $urlOrPath")
            }
        }
        // Otherwise, treat as file path
        val file = File(urlOrPath)
        return if (file.exists()) {
            file.extension.takeIf { it.isNotEmpty() }
                ?: throw IllegalArgumentException("File has no extension: $urlOrPath")
        } else {
            throw IllegalArgumentException("File does not exist: $urlOrPath")
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun getExtensionFromBytes(bytes: ByteArray): String {
        // Analyze magic bytes of the first kilobyte
        val magicBytes = bytes.slice(0..1024).toByteArray()
        return MagicNumberIdentifier.identify(magicBytes.toUByteArray())
    }

    private fun getExtensionFromBitmap(bitmap: Bitmap): String {
        // Extract format from Bitmap's config
        return when (bitmap.config) {
            Bitmap.Config.ARGB_8888 -> "png"
            Bitmap.Config.RGB_565 -> "jpg"
            else -> throw IllegalArgumentException("Unsupported bitmap config: ${bitmap.config}")
        }
    }
}
