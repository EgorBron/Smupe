package net.blusutils.smupe.ui.about

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore
import net.blusutils.smupe.ui.misc.CenteredContainer

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun WelcomeScreen(
    hide: () -> Unit = {}
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val ds by ctx.settingsDataStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue
    )
    val isDark = when (ds.themePreference.number) {
        1 -> true
        2 -> false
        else -> isSystemInDarkTheme()
    }

    Surface(Modifier.fillMaxSize()) {
            CenteredContainer(true, Modifier.fillMaxSize(.9f), Modifier.fillMaxSize(.9f)) {
                Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge)
                HorizontalDivider()
                HorizontalPager(
                    state = pagerState,
                    Modifier
                        .wrapContentSize()
                        .fillMaxWidth(.8f)
                ) { page ->
                    val (txt, img) = when (page) {
                        0 -> {
                            Pair(R.string.welcome_greeting, R.drawable.smupe_icon_foreground)
                        }

                        1 -> {
                            Pair(R.string.swipe_tip, if (isDark) R.raw.swipe_tip else R.raw.swipe_tip_black)
                        }

                        2 -> {
                            Pair(R.string.welcome_menu_tip, R.raw.smupe_loading)
                        }

//                    2 -> {
//                        Pair(R.string.welcome_actions_tip, if (isDark) R.raw.action_tip else R.raw.action_tip_black)
//                    }
//
//                    3 -> {
//                        Pair(R.string.welcome_favourite_tip, if (isDark) R.raw.rendo_loading else R.raw.rendo_loading)
//                    }
//
//                    4 -> {
//                        Pair(R.string.welcome_menu_tip, R.raw.smupe_loading)
//                    }

                        else -> Pair(0, R.raw.smupe_loading)
                    }
                    CenteredContainer(true, Modifier.fillMaxWidth(), Modifier.fillMaxWidth()) {
                        val lottie by rememberLottieComposition(LottieCompositionSpec.RawRes(img))
                        CenteredContainer(true, Modifier.fillMaxWidth(), Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(txt),
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (page == 0) { // TODO replace to animation
                                Box(
                                    Modifier.clip(RoundedCornerShape(16.dp))
                                ) {
                                    Image(painterResource(R.drawable.smupe_icon_background), null)
                                    Image(painterResource(img), null)
                                }
                            } else {

                                LottieAnimation(
                                    composition = lottie,
                                    Modifier.scale(0.4f),
                                    restartOnPlay = true,
                                    iterations = LottieConstants.IterateForever
                                )
                            }
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
                    Row(
                        Modifier
//                    .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(bottom = 64.dp),
                        Arrangement.Center,
                        Alignment.Bottom
                    ) {
                        repeat(pagerState.pageCount) {
                            val color =
                                if (pagerState.currentPage == it) Color.LightGray
                                else Color.DarkGray
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .size(16.dp)
                            )
                        }
                    }

                    var textBack by remember { mutableStateOf("") }
                    var textNext by remember { mutableStateOf("") }
                    var actionBack by remember { mutableStateOf({}) }
                    var actionNext by remember { mutableStateOf({}) }
                    if (pagerState.currentPage > 0) {
                        textBack = stringResource(R.string.welcome_pager_back)
                        actionBack = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    } else {
                        textBack = stringResource(R.string.welcome_skip)
                        actionBack = hide
                    }
                    if (pagerState.currentPage < pagerState.pageCount - 1) {
                        textNext = stringResource(R.string.welcome_pager_next)
                        actionNext = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    } else {
                        textNext = stringResource(R.string.welcome_start)
                        actionNext = hide
                    }

                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp, 8.dp),
                        Arrangement.SpaceBetween,
                        Alignment.Bottom
                    ) {
                        OutlinedButton(actionBack) {
                            Text(textBack)
                        }
                        Button(actionNext) {
                            Text(textNext)
                        }
                    }
                }
            }
    }
}