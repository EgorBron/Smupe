package net.blusutils.smupe.ui.images

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.SmupeSettings
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore
import net.blusutils.smupe.data.room.util.FavesUtil
import net.blusutils.smupe.util.*

fun getApiDefOptionTriple(
    isApiDefShow: (Boolean) -> Unit
): Triple<ImageVector, Int, () -> Unit> =
    Triple(
        Icons.Default.Api,
        R.string.api_defs_title,
        { isApiDefShow(true) }
    )

fun getFaveButtonTriple(
    scope: CoroutineScope,
    imageUrl: String?,
    snackbarState: SnackbarHostState,
    addedText: String,
    removedText: String,
): Triple<ImageVector, Int, () -> Unit> =
    Triple(
        Icons.Default.Favorite,
        R.string.favorites_title,
        {
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
    )

fun getOpenButtonTriple(
    ctx: Context,
    imageUrl: Uri?
): Triple<ImageVector, Int, () -> Unit> =
    Triple(
        Icons.Default.OpenInBrowser,
        R.string.open_in_browser,
        { ctx.startActivity(Intent(Intent.ACTION_VIEW, imageUrl)) }
    )

fun getShareButtonTriple(
    ds: SmupeSettings,
    ctx: Context,
    imageUrl: String?,
    themeText: String,
    subjectText: String,
    messageText: String,
    currentApi: String
): Triple<ImageVector, Int, () -> Unit> =
    Triple(
        Icons.Default.Share,
        R.string.share,
        {
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
    )

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
            R.string.save,
            {
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
        )

@OptIn(ExperimentalFoundationApi::class)
@Deprecated("Just deprecated")
fun getApiDefButton(
    isApiDefShow: (Boolean) -> Unit,
    scope: CoroutineScope,
    anchorState: AnchoredDraggableState<DragAnchors>
) = (@Composable {
    ActionMenuButton({ Icon(Icons.Default.Api, null) }) {
        isApiDefShow(true)
        scope.launch {
            anchorState.animateTo(
                DragAnchors.Center,
                anchorState.lastVelocity / 8f
            )
        }
    }
})

@Deprecated("Just deprecated")
fun getFaveButton(
    scope: CoroutineScope,
    imageUrl: Uri?,
    snackbarState: SnackbarHostState
) = (@Composable {
    val added = stringResource(R.string.msg_added_to_favorites)
    val removed = stringResource(R.string.msg_removed_from_favorites)
    ActionMenuButton({ Icon(Icons.Default.Favorite, null) }) {
        scope.launch {
            val isAdded = FavesUtil.tryAddFave(imageUrl.toString())
            snackbarState.showSnackbar(if (isAdded) added else removed, duration = SnackbarDuration.Short)
        }
    }
})

@Deprecated("Just deprecated")
fun getOpenButton(
    ctx: Context,
    imageUrl: Uri?
) = (@Composable {
    ActionMenuButton({ Icon(Icons.Default.OpenInBrowser, null) }) {
        ctx.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                imageUrl
            )
        )
    }
})

@Deprecated("Just deprecated")
fun getShareButton(
    ds: SmupeSettings,
    ctx: Context,
    imageUrl: Uri?
) = (@Composable {
    val theme = stringResource(R.string.send_image_dialog)
    val subject = stringResource(R.string.send_image_dialog_subject)
    val message = stringResource(R.string.send_image_dialog_message)
    ActionMenuButton({ Icon(Icons.Default.Share, null) }) {
        if (ds.isSimpleSharePreferred)
            sharePlainText(
                ctx,
                theme,
                subject,
                ds.shareTemplate.formatByKeywords( // TODO: unhardcode it
                    mapOf(
                        "app" to "Smupe!",
                        "link" to (imageUrl ?: ""),
                        "api" to "Cats as a Service"
                    ),
                    false
                )
            )
        else
            shareImage(
                ctx,
                imageUrl!!,
                theme,
                subject,
                ds.shareTemplate.formatByKeywords( // TODO: unhardcode it
                    mapOf(
                        "app" to "Smupe!",
                        "link" to (imageUrl ?: ""),
                        "api" to "Cats as a Service"
                    ),
                    false
                )
            )

    }
})

@Deprecated("Just deprecated")
@OptIn(ExperimentalPermissionsApi::class)
fun getDownloadButton(
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    imageUrl: Uri?,
    ctx: Context
) = (@Composable {
    val ds by ctx.settingsDataStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue
    )
    val storagePermission = rememberMultiplePermissionsState(perms)

    val downloadingText = stringResource(R.string.msg_downloading)
    val failedText = stringResource(R.string.msg_failed_download)
    val doneText = stringResource(R.string.done)
    val missingPermsText = stringResource(R.string.missing_permissions)

    ActionMenuButton({ Icon(Icons.Default.SaveAlt, null) }) {
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
})

@Deprecated("Just deprecated")
@Composable
fun ActionMenuButton(icon: @Composable () -> Unit, onClick: () -> Unit) {
    LargeFloatingActionButton(onClick, shape = CircleShape) { icon() }
}