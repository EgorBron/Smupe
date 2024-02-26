package net.blusutils.smupe.util

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Tries to download a resource and shows a snackbar during process.
 * @param coroutineScope A coroutine scope to launch the process.
 * @param snackbarHostState The snackbar host state, allows to show a snackbar.
 * @param uri The resource uri to download.
 * @param context The activity context to download the resource.
 * @param downloadingText The text to show in the snackbar while downloading.
 * @param failedText The text to show in the snackbar if the download failed.
 * @param doneText The text to show in the snackbar if the download succeeded.
 */
fun downloadResourceWithSnackbarAsync(
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    uri: Uri?,
    context: Context,
    downloadingText: String,
    failedText: String,
    doneText: String
) {
    coroutineScope.launch {
        snackbarHostState.showSnackbar(downloadingText, duration = SnackbarDuration.Short)
    }
    coroutineScope.launch {
        Log.d("downloadResourceAsync", "$uri, ${uri == null}, ${uri?.let { it::class.qualifiedName }}")
        if (uri != null) {
            val ext = FileTypeAnalyzer.getExtension(uri)
            Log.d("downloadResourceAsync", "Downloading: barMsg")
            uri.downloadObjectOnUri(
                "smupe.$ext",
                context
            )
        }
    }.invokeOnCompletion { exception ->
        coroutineScope.launch {
            val barMsg =
                if (uri == null || exception != null) {
                    Log.e("downloadResourceAsync", "Failed to download", exception)
                    failedText
                } else {
                    Log.i("downloadResourceAsync", "Done downloading!")
                    doneText
                }
            snackbarHostState.showSnackbar(barMsg, duration = SnackbarDuration.Short)
        }
    }
}