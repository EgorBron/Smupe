package net.blusutils.smupe.ui.images

import android.content.res.Configuration
import android.view.View
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import coil.request.ImageRequest
import kotlinx.coroutines.async
import kotlin.math.roundToInt
import kotlin.math.sin

enum class DragAnchors { Start, Center, End, }

@OptIn(ExperimentalFoundationApi::class)
fun rememberAnchoredDraggableState(
    configuration: Configuration,
    density: Density,
    velocityThreshold: Dp = 200.dp
): AnchoredDraggableState<DragAnchors> {
    val screenWidth = configuration.screenWidthDp.dp / 2

    val actionSizePx = with(density) { screenWidth.toPx() }

    return AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            anchors = DraggableAnchors {
                DragAnchors.Start at actionSizePx
                DragAnchors.Center at 0f
                DragAnchors.End at -actionSizePx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { velocityThreshold.toPx() } },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow,
            )
        )
}

@Deprecated("Replaced by [SwipeableCard]")
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AsyncSwipeableImage(
    content: ImageRequest?,
    contentDescription: String?,
    anchoredDraggableState: AnchoredDraggableState<DragAnchors>,
    modifier: Modifier = Modifier,
    surfaceModifier: Modifier = Modifier,
    enabled: Boolean = true,
    handleRtl: Boolean = true,
    actionsReversed: Boolean = false,
    rotate: Boolean = true,
    shape: Shape = RoundedCornerShape(24.dp),
    startAnchorCallback: ()->Unit = {},
    centerAnchorCallback: ()->Unit = {},
    endAnchorCallback: ()->Unit = {},
    onClick: ()->Unit = {},
    onLongClick: ()->Unit = {},
    onDoubleClick: ()->Unit = {},
    onSuccess: (AsyncImagePainter.State.Success)->Unit = {},
    onError: (AsyncImagePainter.State.Error)->Unit = {},
    loadingContent: @Composable SubcomposeAsyncImageScope.(AsyncImagePainter.State.Loading) -> Unit = {},
    successContent: @Composable SubcomposeAsyncImageScope.(AsyncImagePainter.State.Success) -> Unit = {},
    errorContent: @Composable SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit = {},
    additionalContent: @Composable () -> Unit = {}
) {

    val isRightToLeft =
        LocalConfiguration.current.layoutDirection == View.LAYOUT_DIRECTION_RTL

    val interactionSource = remember { MutableInteractionSource() }

    val scope = rememberCoroutineScope()

    var isNormal = true
    if (handleRtl) isNormal = !isRightToLeft
    isNormal = if (actionsReversed) !isNormal else isNormal

    // To prevent frequent recompositions, LaunchedEffect will observe changes on state
    LaunchedEffect(anchoredDraggableState.currentValue) {
        async {
            when (anchoredDraggableState.targetValue) {
                DragAnchors.End -> { // IDK why it should be reversed
                    if (isNormal)
                        endAnchorCallback()
                    else
                        startAnchorCallback()
                }

                DragAnchors.Start -> {
                    if (isNormal)
                        startAnchorCallback()
                    else
                        endAnchorCallback()
                }

                DragAnchors.Center -> {
                    centerAnchorCallback()
                }
            }
        }
    }


    // Fixes offset & rotation direction on RTL (i.e. Arabic) locales
    val direction = if (isRightToLeft && handleRtl) 1 else -1

    var surfaceModifier = surfaceModifier
        .fillMaxSize()
        .offset {
            IntOffset(
                x = direction * anchoredDraggableState
                    .requireOffset()
                    .roundToInt(),
                y = 0
            )
        }
    if (rotate)
        surfaceModifier = surfaceModifier.rotate(
            Math.toDegrees(
                direction * 2 * sin(
                    Math.toRadians(
                        anchoredDraggableState
                            .requireOffset()
                            .toDouble() / 360
                    )
                )
            ).toFloat()
        )
    surfaceModifier = surfaceModifier
        .combinedClickable(
            interactionSource = interactionSource,
            indication = null,
            onLongClick = onLongClick,
            onDoubleClick = onDoubleClick,
            onClick = onClick
        )
        .anchoredDraggable(
            anchoredDraggableState,
            Orientation.Horizontal,
            enabled,
            true
        )

    Surface(
        surfaceModifier,
        color = Color.Transparent
    ) {
        Column(
            Modifier,
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            SubcomposeAsyncImage(
                content,
                contentDescription = contentDescription,
                onSuccess = onSuccess,
                onError = onError,
                modifier = modifier
                    .fillMaxWidth(.95f)
                    .scale(.95f)
                    .clip(shape),
                loading = loadingContent,
                error = errorContent,
                success = successContent
            )
            additionalContent()
        }
    }
}

