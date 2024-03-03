package net.blusutils.smupe.ui.images

import android.os.Build
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.request.NullRequestDataException
import com.aghajari.compose.lazyswipecards.LazySwipeCards
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.data.image_sources.CurrentApiDefParams
import net.blusutils.smupe.data.image_sources.models.contents.BaseImageContent
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore
import net.blusutils.smupe.data.room.util.FavesUtil
import net.blusutils.smupe.ui.misc.CenteredContainer
import net.blusutils.smupe.ui.misc.CenteredRow
import net.blusutils.smupe.util.globalAppPermissions
import net.blusutils.smupe.util.isInternetAvailable
import net.blusutils.smupe.util.reorder

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun IndefiniteImageDisplay(
    img: suspend ()-> BaseImageContent? = {null},
    isMainShow: (Boolean)->Unit = {},
    isApiDefShow: (Boolean)->Unit = {}
) {

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val snackbarState = remember { SnackbarHostState() }
    val lottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.smupe_loading))
    val ds by ctx.settingsDataStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue
    )
    val storagePermission = rememberMultiplePermissionsState(ctx.globalAppPermissions)

    var imageListStub = remember { mutableStateListOf<Any?>(0) }
    var isHeartVisible by rememberSaveable { mutableStateOf(false) }
    var isFavAddedFor by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(true) {
        imageListStub.add(img())
    }

    Scaffold(
        Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarState) }
    ) {
        LazySwipeCards(
            modifier = Modifier.fillMaxSize().padding(it),
            cardShape = RoundedCornerShape(16.dp),
            cardShadowElevation = 4.dp,
            visibleItemCount = 3
        ) {
            onSwiped { item, dir ->
                Log.d("indef", "postswipe")
                scope.launch {
                    imageListStub.add(img())
                }
            }
            items(imageListStub) { item ->
                CenteredContainer {
                    Card(
                        Modifier.fillMaxSize()
                    ) {
                        Column(
                            Modifier.fillMaxSize(),
                            Arrangement.Center,
                            Alignment.CenterHorizontally
                        ) {
                            val buttons = (item as? BaseImageContent)?.let {
                                listOf<Triple<ImageVector, Int, () -> Unit>>(
                                    getDownloadButtonTriple(
                                        scope,
                                        snackbarState,
                                        item.url?.toUri(),
                                        ctx,
                                        stringResource(R.string.msg_downloading),
                                        stringResource(R.string.msg_failed_download),
                                        stringResource(R.string.done),
                                        stringResource(R.string.missing_permissions),
                                        storagePermission
                                    ),
                                    getShareButtonTriple(
                                        ds,
                                        ctx,
                                        item.url,
                                        stringResource(R.string.send_image_dialog),
                                        stringResource(R.string.send_image_dialog_subject),
                                        CurrentApiDefParams.currentApi?.name ?: "<unknown API>"
                                    ),
                                    getOpenButtonTriple(ctx, item.url?.toUri()),
                                    getFaveButtonTriple(
                                        scope,
                                        item.url,
                                        snackbarState,
                                        stringResource(R.string.msg_added_to_favorites),
                                        stringResource(R.string.msg_removed_from_favorites)
                                    ),
                                    getApiDefOptionTriple(isApiDefShow)
                                ).reorder(
                                    ds.actionMenuElementsOrder
                                )
                            }
                            when (item) {
                                is Int -> LottieTip()
                                is BaseImageContent -> {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CenteredRow {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.SpaceEvenly,
                                            ) {
                                                SubcomposeAsyncImage(
                                                    ImageRequest.Builder(ctx).decoderFactory(
                                                        if (Build.VERSION.SDK_INT >= 28)
                                                            ImageDecoderDecoder.Factory()
                                                        else
                                                            GifDecoder.Factory()
                                                    ).data(item.url).crossfade(true).build(),
                                                    contentDescription = null,
                                                    onSuccess = { },
                                                    onError = { },
                                                    modifier = Modifier
                                                        .fillMaxWidth(.95f)
                                                        .scale(.95f)
                                                        .clip(RoundedCornerShape(24.dp)),
                                                    loading = {
                                                        Column {
                                                            Text(
                                                                stringResource(R.string.msg_fetching),
                                                                Modifier.padding(16.dp, 16.dp),
                                                                style = MaterialTheme.typography.headlineMedium
                                                            )
                                                            LottieAnimation(
                                                                composition = lottie,
                                                                Modifier.scale(0.6f),
                                                                restartOnPlay = true,
                                                                iterations = LottieConstants.IterateForever
                                                            )
                                                        }
                                                    },
                                                    error = {
                                                        var exc = it.result.throwable
                                                        if (item.occurredException != null)
                                                            exc = item.occurredException!!
                                                        Log.d("SubcomposeAsyncImage.Error", "$exc")
                                                        Column {
                                                            Icon(Icons.Default.WarningAmber, null)
                                                            Text(stringResource(R.string.error_represent, "SwipeableImage"))
                                                            if (!exc.message.isNullOrBlank()) {
                                                                Log.d("SwipeableImage.Error", exc.message!!)
                                                                Text(exc.message!!)
                                                            }
                                                            Text(
                                                                if (exc is NullRequestDataException && !isInternetAvailable())
                                                                    stringResource(R.string.error_no_internet_connection)
                                                                else
                                                                    ""
                                                            )
                                                            CurrentApiDefParams.currentApi?.name?.let { Text(it) }
                                                        }
                                                    },
                                                    success = {
                                                        SubcomposeAsyncImageContent(
                                                            Modifier
                                                                .fillMaxWidth(.95f)
                                                                .scale(.95f)
                                                                .clip(RoundedCornerShape(24.dp))
                                                        )
                                                    }
                                                )
                                            }

                                        }
                                        item.url?.let { iu ->
                                            Favorite(iu.toUri(), isHeartVisible) { isHeartVisible = it }
                                        }
                                        var actionButtonsShown by remember { mutableStateOf(false) }
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(.98f)
                                                .padding(bottom = 48.dp),
                                            Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                                            Alignment.Bottom
                                        ) {
                                            var isFavAdded by remember { mutableStateOf(false) }

                                            LaunchedEffect(item) {
                                                isFavAddedFor = item.url
                                            }
                                            Button({ isMainShow(true) }) { Icon(Icons.Default.Menu, null) }
                                            Button(
                                                {
                                                    scope.launch {
                                                        if (isFavAddedFor == item.url)
                                                            isFavAdded =
                                                                FavesUtil.tryAddFave(item.url.toString())
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor =
                                                    if (isFavAdded)
                                                        MaterialTheme.colorScheme.error
                                                    else
                                                        MaterialTheme.colorScheme.primary
                                                ),
                                                enabled = isFavAddedFor != null && isFavAddedFor == item.url
                                            ) {
                                                Icon(
                                                    if (isFavAdded)
                                                        Icons.Filled.Favorite
                                                    else
                                                        Icons.Outlined.FavoriteBorder,
                                                    null
                                                )
                                            }
                                            Button(
                                                { actionButtonsShown = true },
                                                enabled = isFavAddedFor != null && isFavAddedFor == item.url
                                            ) { Icon(Icons.Default.SmartButton, null) }
                                        }
                                        DropdownMenu(actionButtonsShown, { actionButtonsShown = false }) {
                                            buttons?.forEach {
                                                DropdownMenuItem({
                                                    Row(
                                                        horizontalArrangement = Arrangement.Center,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Icon(it.first, null)
                                                        Text(stringResource(it.second))
                                                    }
                                                }, onClick = { it.third() })
                                            }
                                        }
                                    }
                                }

                                else -> Unit
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun LottieTip() {
    val ctx = LocalContext.current
    val ds by ctx.settingsDataStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue
    )
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            when (ds.themePreference.number) {
                1 -> R.raw.swipe_tip_black
                2 -> R.raw.swipe_tip
                else -> if (isSystemInDarkTheme()) R.raw.swipe_tip
                else R.raw.swipe_tip_black
            }
        ))
    Card(Modifier.padding(24.dp)) {
        Text(stringResource(R.string.swipe_tip), style = MaterialTheme.typography.headlineSmall)
        LottieAnimation(
            composition = composition,
            restartOnPlay = true,
            iterations = LottieConstants.IterateForever
        )
    }
}