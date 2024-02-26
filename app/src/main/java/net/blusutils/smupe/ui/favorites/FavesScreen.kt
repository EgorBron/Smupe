package net.blusutils.smupe.ui.favorites

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore
import net.blusutils.smupe.data.room.entities.Favorite
import net.blusutils.smupe.data.room.util.FavesUtil
import net.blusutils.smupe.ui.misc.CenteredColumn
import net.blusutils.smupe.ui.misc.CenteredContainer
import net.blusutils.smupe.ui.misc.CenteredRow
import net.blusutils.smupe.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Preview
@Composable
fun FavesScreen(closeFavorites: () -> Unit = {}) {

    val configuration = LocalConfiguration.current
    val ctx = LocalContext.current

    val settings by ctx.settingsDataStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)
    val storagePermission = rememberMultiplePermissionsState(perms)

    val screenWidth = configuration.screenWidthDp.dp

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var currentImage by remember { mutableStateOf<Favorite?>(null) }
    var faves by remember { mutableStateOf<List<Favorite>?>(null) }

    LaunchedEffect("favesscreen") {
        faves = FavesUtil.getFaves()
    }

    val theme = stringResource(R.string.send_image_dialog)
    val subject = stringResource(R.string.send_image_dialog_subject)
    val message = stringResource(R.string.send_image_dialog_message)
    val downloadingText = stringResource(R.string.msg_downloading)
    val failedText = stringResource(R.string.msg_failed_download)
    val doneText = stringResource(R.string.done)
    val missingPermsText = stringResource(R.string.missing_permissions)

    BackHandler(true) {
        closeFavorites()
    }

    if (currentImage != null) {
        Dialog(
            { currentImage = null }
        ) {
            Card {
                CenteredColumn(
                    Modifier
                        .fillMaxWidth(.95f)
                        .padding(32.dp)
                ) {
                    AsyncImage(
                        ImageRequest.Builder(LocalContext.current)
                            .decoderFactory(
                                if (Build.VERSION.SDK_INT >= 28)
                                    ImageDecoderDecoder.Factory()
                                else
                                    GifDecoder.Factory()
                            ).data(currentImage!!.link).crossfade(true).build(),
                        null,
                        Modifier
                            .fillMaxWidth(.95f)
                            .clip(RoundedCornerShape(16.dp))
                    )
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxWidth(.95f)
//                            .height(128.dp)
//                            .padding(16.dp)
//                            .clip(RoundedCornerShape(16.dp))
//                    ) {
//                        CenteredColumn {
//                            Text("${currentImage?.link}")
//
//                        }
//                    }
                    CenteredRow {
                        IconButton(onClick = {
                            if (currentImage != null)
                                scope.launch {
                                    FavesUtil.tryAddFave(currentImage!!)
                                    faves = faves?.toMutableList()?.apply { remove(currentImage!!) }
                                    currentImage = null
                                }
                        }) {
                            Icon(Icons.Default.Delete, null)
                        }
                        IconButton(onClick = {
                            if (settings.isSimpleSharePreferred)
                                sharePlainText(
                                    ctx,
                                    theme,
                                    subject,
                                    settings.shareTemplate.formatByKeywords( // TODO: unhardcode it
                                        mapOf(
                                            "app" to "Smupe!",
                                            "link" to (currentImage?.link ?: ""),
                                            "api" to "Cats as a Service"
                                        ),
                                    false
                                    )
                                )
                            else
                                shareImage(
                                    ctx,
                                    Uri.parse(currentImage?.link.toString()),
                                    theme,
                                    subject,
                                    settings.shareTemplate.formatByKeywords( // TODO: unhardcode it
                                        mapOf(
                                            "app" to "Smupe!",
                                            "link" to (currentImage?.link ?: ""),
                                            "api" to "Cats as a Service"
                                        ),
                                        false
                                    )
                                )
                        }) {
                            Icon(Icons.Default.Share, null)
                        }
                        IconButton(onClick = {
                            if (!storagePermission.allPermissionsGranted) {
                                storagePermission.launchMultiplePermissionRequest()
                                if (!storagePermission.allPermissionsGranted) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            missingPermsText
                                        )
                                    }
                                }
                            } else downloadResourceWithSnackbarAsync(
                                scope,
                                snackbarHostState,
                                Uri.parse(currentImage?.link),
                                ctx,
                                downloadingText,
                                failedText,
                                doneText
                            )
                        }) {
                            Icon(Icons.Default.SaveAlt, null)
                        }
                        IconButton(onClick = {
                            if (currentImage != null)
                                ctx.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        currentImage!!.link.toUri()
                                    )
                                )
                        }) {
                            Icon(Icons.Default.OpenInBrowser, null)
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(R.string.favorites_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = closeFavorites) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { pad ->
        Column(modifier = Modifier.padding(pad)) {
            Surface(modifier = Modifier.fillMaxSize()) {
                if (faves != null)
                    if (faves!!.isEmpty())
                        CenteredContainer(true) {
                            AsyncImage(R.drawable.smupe_icon_foreground, null)
                            Text(stringResource(R.string.no_images), modifier = Modifier.padding(16.dp))
                        }
                    else
                        LazyVerticalStaggeredGrid(
                            StaggeredGridCells.Adaptive(screenWidth / 3),
                            Modifier
                                .fillMaxWidth()
                                .padding(24.dp, 16.dp),
                            verticalItemSpacing = 8.dp,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            faves!!.forEach {
                                item {
                                    AsyncImage(
                                        ImageRequest.Builder(LocalContext.current)
                                            .decoderFactory(
                                                if (Build.VERSION.SDK_INT >= 28)
                                                    ImageDecoderDecoder.Factory()
                                                else
                                                    GifDecoder.Factory()
                                            ).data(it.link).crossfade(true).build(),
                                        null,
                                        Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(16.dp))
                                            .clickable {
                                                currentImage = it
                                            }
                                    )
                                }
                            }
                        }
            }
        }
    }
}