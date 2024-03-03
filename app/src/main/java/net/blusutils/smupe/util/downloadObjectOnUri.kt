package net.blusutils.smupe.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.File

/**
 * Download a file from an [Uri].
 * @param path the path to save the file to.
 * @param context the Android context to use.
 * @throws IllegalArgumentException if [path] is null or empty.
 */
fun Uri.downloadObjectOnUri(path: String?, context: Context) {
    if (!path.isNullOrBlank()) {
        Log.d("Uri.downloadObjectOnUri", "$path")
        val req = DownloadManager.Request(this)
        req.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, path
        )
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        val mgr = ContextCompat.getSystemService(context, DownloadManager::class.java)
        mgr?.enqueue(req)
    } else throw IllegalArgumentException("path is null or empty")
}

/**
 * Download a file from an [Uri] (and save to [File]).
 * @see [downloadObjectOnUri]
 */
fun Uri.downloadObjectOnUri(path: File, context: Context) =
    this.downloadObjectOnUri(path.path, context)