package net.blusutils.smupe.ui.images

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.InternalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.SubcomposeAsyncImageScope
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.request.NullRequestDataException
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.SmupeSettings
import net.blusutils.smupe.data.image_sources.CurrentApiDefParams
import net.blusutils.smupe.data.image_sources.models.contents.BaseImageContent
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore
import net.blusutils.smupe.data.room.util.FavesUtil
import net.blusutils.smupe.ui.misc.CenteredContainer
import net.blusutils.smupe.ui.misc.ErrorRepr
import net.blusutils.smupe.util.*


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class, InternalAnimationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun SmupeImageDisplay(
    img: suspend () -> BaseImageContent?,
    isApiDefShow: (Boolean) -> Unit,
    isMenuShow: (Boolean) -> Unit
) {
    Log.d("SmupeImag", "RECOMPOSING")
    val ctx = LocalContext.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val downloadingText = stringResource(R.string.msg_downloading)
    val failedText = stringResource(R.string.msg_failed_download)
    val doneText = stringResource(R.string.done)
    val missingPermsText = stringResource(R.string.missing_permissions)

    val scope = rememberCoroutineScope()
    val anchorState = remember { rememberAnchoredDraggableState(configuration, density) }
    val snackbarState = remember { SnackbarHostState() }
    val ds by ctx.settingsDataStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue
    )
    val storagePermission = rememberMultiplePermissionsState(perms)

    var once by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var imageRequest by remember { mutableStateOf<ImageRequest?>(null) }
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    var currentException by remember { mutableStateOf<Throwable?>(null) }
    var blockingNextGens by remember { mutableStateOf(false) }
    var actionButtonsShown by remember { mutableStateOf(false) }
    var isHeartVisible by remember { mutableStateOf(false) }

    val callback = { _: Any -> blockingNextGens = false }

    val successCallback = { a: Any ->
        successCallback(
            ds,
            storagePermission,
            scope,
            snackbarState,
            imageUrl,
            ctx,
            downloadingText,
            failedText,
            doneText,
            missingPermsText
        )
        callback(a)
    }

    val handleNext = getHandleNext(ctx, scope, anchorState, blockingNextGens, img)
    {  req, uri, exc, bl ->
        once = true
        imageRequest = req
        imageUrl = uri
        currentException = exc
        blockingNextGens = bl
    }
    val handleMenu =
        getHandleMenu(
            imageUrl,
            actionButtonsShown,
            scope,
            anchorState,
            ds,
            storagePermission,
            snackbarState,
            ctx,
            downloadingText,
            failedText,
            doneText,
            missingPermsText
        ) { actionButtonsShown = it }

    val onLongClick = {
        if (!actionButtonsShown)
            isMenuShow(true)
    }
    val onDoubleClick = {
        if (!actionButtonsShown && imageUrl != null)
            isHeartVisible = true
    }
    val onClick = {
        // TODO open image view
    }

    val loading = getLoadingPair()

    val errorPair = getErrorPair(ctx, imageUrl)
    val error = errorPair.first
    imageUrl = errorPair.second

    val success = getSuccess(ctx)

    val buttons = listOf<@Composable () -> Unit>(
        getDownloadButton(scope, snackbarState, imageUrl, ctx),
        getShareButton(ds, ctx, imageUrl),
        getOpenButton(ctx, imageUrl),
        getFaveButton(scope, imageUrl, snackbarState),
        getApiDefButton(isApiDefShow, scope, anchorState)
    ).reorder(
        ds.actionMenuElementsOrder
    )

    val actionButtons = @Composable {
        AnimatedVisibility(actionButtonsShown) {
            Column(
                // the buttons should appear on correct side
                Modifier
                    .fillMaxSize()
                    .padding(
                        top = 36.dp,
                        start = if (!ds.controlsReversed) 48.dp else 0.dp,
                        end = if (ds.controlsReversed) 48.dp else 0.dp,
                    ),
                Arrangement.spacedBy(8.dp),
                if (!ds.controlsReversed)
                    Alignment.Start
                else
                    Alignment.End
            ) { buttons.forEach { it() } }
        }
    }

    LaunchedEffect(true) {
        once = false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (!ds.buttonsShown) // if disabled, show below
                actionButtons()
            AsyncSwipeableImage(
                imageRequest,
                null,
                anchorState,
                enabled = !ds.buttonsShown,
                actionsReversed = ds.controlsReversed,
                startAnchorCallback = handleNext,
                centerAnchorCallback = { actionButtonsShown = false },
                endAnchorCallback = handleMenu,
                onClick = onClick,
                onLongClick = onLongClick,
                onDoubleClick = onDoubleClick,
                onSuccess = successCallback,
                onError = callback,
                loadingContent = { isLoading = true; once = true; loading() },
                successContent = { isLoading = false; once = true; this.apply { success() } },
                errorContent = {
                    if (!once) {
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
                            Text(stringResource(R.string.welcome_swipe_tip), style = MaterialTheme.typography.headlineSmall)
                            LottieAnimation(
                                composition = composition,
                                restartOnPlay = true,
                                iterations = LottieConstants.IterateForever
                            )
                            Text(stringResource(R.string.welcome_actions_tip), style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                    else {
                        var ti = it
                        if (currentException != null) {
                            ti = it.copy(
                                result = it.result.copy(throwable = currentException!!)
                            )
                        }
                        error(ti)
                    }
                    isLoading = false
               },
            ) {}

            if (ds.buttonsShown) {

                actionButtons() // if enabled, show above
                Row(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.98f)
                        .padding(bottom = 48.dp), Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally), Alignment.Bottom) {
                    var isFavAdded by remember { mutableStateOf(false) }
                    if (isLoading) isFavAdded = false
                    Button(
                        { handleNext() },
                        enabled = !isLoading
                    ) { Icon(Icons.Default.Image, null) }
                    Button({ isMenuShow(true) }) { Icon(Icons.Default.Menu, null) }
                    if (imageUrl!=null) {
                        Button(
                            {scope.launch { isFavAdded = FavesUtil.tryAddFave(imageUrl.toString()) }},
                            colors = ButtonDefaults.buttonColors(
                                containerColor =
                                    if (isFavAdded)
                                        MaterialTheme.colorScheme.error
                                    else
                                        MaterialTheme.colorScheme.primary
                            ),
                            enabled = !isLoading
                        ) { Icon(
                            if (isFavAdded)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                            null
                        ) }
                        Button(
                            {
                                if (!actionButtonsShown)
                                    handleMenu()
                                else
                                    actionButtonsShown = false
                            },
                            enabled = !isLoading
                        ) { Icon(Icons.Default.SmartButton, null) }
                }
                }
            }

            imageUrl?.let { img -> Favorite(img, isHeartVisible) { isHeartVisible = it} }
        }
    }
}

//@OptIn(ExperimentalPermissionsApi::class)
@OptIn(ExperimentalPermissionsApi::class)
private fun successCallback(
    ds: SmupeSettings,
    storagePermission: MultiplePermissionsState,
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    imageUrl: Uri?,
    ctx: Context,
    downloadingText: String,
    failedText: String,
    doneText: String,
    missingPermsText: String
) {
    if (ds.autoSavesEnabled) {
        if (!storagePermission.allPermissionsGranted) {
            storagePermission.launchMultiplePermissionRequest()
            if (!storagePermission.allPermissionsGranted) {
                scope.launch {
                    val a = async {
                        snackbarState.showSnackbar(
                            missingPermsText,
                            duration = SnackbarDuration.Short
                        )
                    }
                     val b = async {
                         successCallback(ds, storagePermission, scope, snackbarState, imageUrl, ctx, downloadingText, failedText, doneText, missingPermsText)
                     }
                    awaitAll(a, b)
                }
            }
        } else
            downloadResourceWithSnackbarAsync(
                scope,
                snackbarState,
                imageUrl,
                ctx,
                downloadingText,
                failedText,
                doneText
            )
    }
}

@Composable
fun getSuccess(
    ctx: Context
): @Composable (SubcomposeAsyncImageScope.() -> Unit) = imgScope@{
    Log.d("SubcomposeAsyncImage.Success", "Success")
    CenteredContainer(false, Modifier.fillMaxSize(), Modifier.fillMaxSize()) {
        Card {
            CenteredContainer(true, Modifier.fillMaxSize(), Modifier.fillMaxSize()) {
                if (isNetworkConnected(ctx))
                    this@imgScope.SubcomposeAsyncImageContent(
                        Modifier
                            .fillMaxWidth(.95f)
                            .scale(.95f)
                            .clip(RoundedCornerShape(24.dp))
                    )
                else {
                    Icon(Icons.Default.WarningAmber, null)
                    ErrorRepr(
                        "SwipeableImage",
                        "Provider: cats",
                        stringResource(R.string.error_no_internet_connection)
                    )
                }
            }
        }
    }
}

@Composable
fun getErrorPair( // TODO fix it
    ctx: Context,
    imageUrl: Uri?
): Pair<@Composable (AsyncImagePainter.State.Error) -> Unit, Uri?> {
    var imageUrl1 = imageUrl
    val error = @Composable { err: AsyncImagePainter.State.Error ->
        // The error placeholder shows at first time and at any error
        // (it gives for us and user a chance to detect any issues and possibly retry)
        // Error placeholder wont work when image cached
        // (so maybe there's possibly no error occurring)

        if (!isNetworkConnected(ctx))
            imageUrl1 = null

        CenteredContainer(false, Modifier.fillMaxSize(), Modifier.fillMaxSize()) {
            Card {
                CenteredContainer(true, Modifier.fillMaxSize(), Modifier.fillMaxSize()) {
                    Log.d("SubcomposeAsyncImage.Error", "${err.result.throwable}")
                    Icon(Icons.Default.WarningAmber, null)
                    ErrorRepr(
                        "SwipeableImage",
                        CurrentApiDefParams.currentApi?.name,
                       if (err.result.throwable is NullRequestDataException && !isInternetAvailable())
                           stringResource(R.string.error_no_internet_connection)
                        else "", // TODO fix here
                        err.result.throwable
                    )
                }
            }
        }
    }
    return Pair(error, imageUrl1)
}

@Composable
fun getLoadingPair(): @Composable () -> Unit {
    val lottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.smupe_loading),)
    val loading = @Composable {
        Log.d("SubcomposeAsyncImage.Loading", "Loading")
        CenteredContainer(false, Modifier.fillMaxSize(), Modifier.fillMaxSize()) {
            Card {
                CenteredContainer(true, Modifier.fillMaxSize(), Modifier.fillMaxSize()) {
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
            }
        }
    }
    return loading
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
fun getHandleMenu(
    imageUrl: Uri?,
    actionButtonsShown: Boolean,
    scope: CoroutineScope,
    anchorState: AnchoredDraggableState<DragAnchors>,
    ds: SmupeSettings,
    storagePermission: MultiplePermissionsState,
    snackbarState: SnackbarHostState,
    ctx: Context,
    downloadingText: String,
    failedText: String,
    doneText: String,
    missingPermsText: String,
    setActionButtonsShown: (Boolean)->Unit
): () -> Unit = {
    Log.d("handleMenu", "$imageUrl - $actionButtonsShown")

    if (imageUrl == null) scope.launch {
        anchorState.animateTo(
            DragAnchors.Center,
            anchorState.lastVelocity / 16f
        )
    }
    else
        if (!ds.actionSwipeIsSave) {
            if (imageUrl != null)
                setActionButtonsShown(true)
        } else {
            scope.launch {
                val ao = async {
                    anchorState.animateTo(
                        DragAnchors.Center,
                        anchorState.lastVelocity / 16f
                    )
                }

                val bo = async {
                    if (!storagePermission.allPermissionsGranted) {
                        val ai = async {
                            snackbarState.showSnackbar(
                                missingPermsText,
                                duration = SnackbarDuration.Short
                            )
                        }
                        Log.d("ERRONOPERM", storagePermission.revokedPermissions.joinToString(",") { it.permission })
                        val bi = async { storagePermission.launchMultiplePermissionRequest() }
                        awaitAll(ai, bi)
                    } else
                        downloadResourceWithSnackbarAsync(
                            scope,
                            snackbarState,
                            imageUrl,
                            ctx,
                            downloadingText,
                            failedText,
                            doneText
                        )
                }
                awaitAll(ao, bo)
            }
        }
}

@OptIn(ExperimentalFoundationApi::class)
 fun getHandleNext(
    ctx: Context,
    scope: CoroutineScope,
    anchorState: AnchoredDraggableState<DragAnchors>,
    blockingNextGens: Boolean,
    img: suspend () -> BaseImageContent?,
    setVars: (ImageRequest?, Uri?, Throwable?, Boolean)->Unit
): ()->Unit = {
    scope.launch {
        val a = async {
            anchorState.animateTo(
                DragAnchors.Center,
                anchorState.lastVelocity / 16f
            )
        }

        val b = async {
            if (!blockingNextGens) {
                val imgData = img()
                val imga = ImageRequest.Builder(ctx)
                    .decoderFactory(
                        if (Build.VERSION.SDK_INT >= 28)
                            ImageDecoderDecoder.Factory()
                        else
                            GifDecoder.Factory()
                    )
                    .data(
                        imgData?.url
                    )
                    .crossfade(true)
                    .build()
                setVars(imga, imgData?.let { data -> Uri.parse(data.url.toString()) }, imgData?.occurredException, true)
            }
        }
        awaitAll(a, b)
    }
}