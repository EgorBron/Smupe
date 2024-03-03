package net.blusutils.smupe.ui.images

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.SmupeSettings
import net.blusutils.smupe.data.room.util.FavesUtil
import net.blusutils.smupe.util.downloadResourceWithSnackbarAsync
import net.blusutils.smupe.util.formatByKeywords
import net.blusutils.smupe.util.shareImage
import net.blusutils.smupe.util.sharePlainText

fun getApiDefOptionTriple(
    isApiDefShow: (Boolean) -> Unit
): Triple<ImageVector, Int, () -> Unit> =
    Triple(
        Icons.Default.Api,
        R.string.api_defs_title
    ) { isApiDefShow(true) }

fun getFaveButtonTriple(
    scope: CoroutineScope,
    imageUrl: String?,
    snackbarState: SnackbarHostState,
    addedText: String,
    removedText: String,
): Triple<ImageVector, Int, () -> Unit> =
    Triple(
        Icons.Default.Favorite,
        R.string.favorites_title
    ) {
        scope.launch {
            imageUrl?.let {
                val isAdded = FavesUtil.tryAddFave(it)
                snackbarState.showSnackbar(
                    if (isAdded) addedText
                    else removedText,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

fun getOpenButtonTriple(
    ctx: Context,
    imageUrl: Uri?
): Triple<ImageVector, Int, () -> Unit> =
    Triple(
        Icons.Default.OpenInBrowser,
        R.string.open_in_browser
    ) { ctx.startActivity(Intent(Intent.ACTION_VIEW, imageUrl)) }

fun getShareButtonTriple(
    ds: SmupeSettings,
    ctx: Context,
    imageUrl: String?,
    themeText: String,
    subjectText: String,
    currentApi: String
): Triple<ImageVector, Int, () -> Unit> =
    Triple(
        Icons.Default.Share,
        R.string.share
    ) {
        if (ds.isSimpleSharePreferred)
            sharePlainText(
                ctx,
                themeText,
                subjectText,
                ds.shareTemplate.formatByKeywords( // TODO: unhardcode it
                    mapOf(
                        "app" to "Smupe!",
                        "link" to (imageUrl ?: "<no image>"),
                        "api" to currentApi
                    ),
                    false
                )
            )
        else
            shareImage(
                ctx,
                imageUrl!!.toUri(),
                themeText,
                subjectText,
                ds.shareTemplate.formatByKeywords( // TODO: unhardcode it
                    mapOf(
                        "app" to "Smupe!",
                        "link" to (imageUrl ?: "<no image>"),
                        "api" to currentApi
                    ),
                    false
                )
            )

    }

@OptIn(ExperimentalPermissionsApi::class)
fun getDownloadButtonTriple(
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    imageUrl: Uri?,
    ctx: Context,
    downloadingText: String,
    failedText: String,
    doneText: String,
    missingPermsText: String,
    storagePermission: MultiplePermissionsState
): Triple<ImageVector, Int, () -> Unit> =
        Triple(
            Icons.Default.Download,
            R.string.save
        ) {
            if (!storagePermission.allPermissionsGranted) {
                storagePermission.launchMultiplePermissionRequest()
                if (!storagePermission.allPermissionsGranted) {
                    scope.launch {
                        snackbarState.showSnackbar(
                            missingPermsText,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            } else downloadResourceWithSnackbarAsync(
                scope,
                snackbarState,
                imageUrl,
                ctx,
                downloadingText,
                failedText,
                doneText
            )
        }
