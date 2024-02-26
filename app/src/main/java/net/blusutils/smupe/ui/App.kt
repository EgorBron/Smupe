package net.blusutils.smupe.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jonathanfinerty.once.Once
import net.blusutils.smupe.data.image_sources.CurrentApiDefParams
import net.blusutils.smupe.data.image_sources.repository.Gallery
import net.blusutils.smupe.ui.about.AboutScreen
import net.blusutils.smupe.ui.about.WelcomeScreen
import net.blusutils.smupe.ui.favorites.FavesScreen
import net.blusutils.smupe.ui.image_sources.AddInternetApiDef
import net.blusutils.smupe.ui.image_sources.ApiDefsEditList
import net.blusutils.smupe.ui.image_sources.ApiDefsSelectorFlyout
import net.blusutils.smupe.ui.image_sources.LocalApiDefsEditScreen
import net.blusutils.smupe.ui.images.IndefiniteImageDisplay
import net.blusutils.smupe.ui.settings.SettingsMenu

@Composable
fun App() {
    val welcomeScreenShown = "welcomeScreenShown"
    val context = LocalContext.current

    val navController = rememberNavController()
    var shouldMainFlyoutBeVisible by remember { mutableStateOf(false) }
    var shouldApiDefsFlyoutBeVisible by remember { mutableStateOf(false) }

    val setMainFlyoutState = { x: Boolean -> shouldMainFlyoutBeVisible = x }
    val setApiDefFlyoutState = { x: Boolean -> shouldApiDefsFlyoutBeVisible = x }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        NavHost(
            navController = navController,
            startDestination =
            if (Once.beenDone(Once.THIS_APP_INSTALL, welcomeScreenShown))
                "imageView"
            else
                "welcome"
        ) {

            composable("welcome") {
                WelcomeScreen {
                    Once.markDone(welcomeScreenShown)
                    navController.navigate("imageView")
                }
            }

            composable("imageView") {
                Column(
                    Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
//                    SmupeImageDisplay(flyoutMainHoistingFunc, flyoutApiDefHoistingFunc) { cb ->
//                        CatApi.requestWithCallback {
//                            if (it.exception == null && it.response != null)
//                                cb(ImageRequest.Builder(context).data(it.response.url).crossfade(true).build())
//                        }
//                    }
//                    SmupeImageDisplay(
                      IndefiniteImageDisplay(
                          img = suspend {
                            CurrentApiDefParams.currentApi = CurrentApiDefParams.currentApi
                                ?:
                                if (CurrentApiDefParams.dynamicRepos.isEmpty())
                                        Gallery
                                else
                                    // TODO: instead of random use persistent storage
                                    CurrentApiDefParams.dynamicRepos.random()
                            val obj =
                                if (CurrentApiDefParams.currentSearchQuery.isNotBlank()
                                && CurrentApiDefParams.currentApi!!.search != null
                                && CurrentApiDefParams.currentApi!!.search!!.supported)
                                    CurrentApiDefParams.currentApi!!.searchAsync(CurrentApiDefParams.currentSearchQuery)
                                else
                                    CurrentApiDefParams.currentApi?.requestAsync()
                            Log.d("Smupe", "lnk: $obj")
                            obj
                        },
                        isApiDefShow = setApiDefFlyoutState,
                        isMainShow = setMainFlyoutState
                    )
//                    IndefiniteImageDisplay(
//                        isApiDefShow = flyoutApiDefHoistingFunc,
//                        isMainShow = flyoutMainHoistingFunc)
                }
            }

            composable("apiDefsEditList") {
                ApiDefsEditList(navController) {
                    navController.navigate("imageView")
                    shouldApiDefsFlyoutBeVisible = true
                }
            }

            composable("addInternetApi") {
                AddInternetApiDef {
                    navController.navigate("apiDefsEditList")
                }
            }

            composable("addLocalApi") {
                LocalApiDefsEditScreen {
                    navController.navigate("apiDefsEditList")
                }
            }

            composable("settings") {
                SettingsMenu {
                    navController.navigate("imageView")
                }
            }

            composable("favourites") {
                FavesScreen {
                    navController.navigate("imageView")
                }
            }

            composable("about") {
                AboutScreen {
                    navController.navigate("imageView")
                }
            }
        }

        if (shouldMainFlyoutBeVisible)
            MainModalFlyout(
                shouldMainFlyoutBeVisible,
                setMainFlyoutState,
                onFavesOpen = {
                    navController.navigate("favourites")
                },
                onApiDefsOpen = {
                    shouldApiDefsFlyoutBeVisible = true
                },
                onSettingsOpen = {
                    navController.navigate("settings")
                },
                onAboutOpen = {
                    navController.navigate("about")
                }
            )

        if (shouldApiDefsFlyoutBeVisible)
            ApiDefsSelectorFlyout(
                shouldApiDefsFlyoutBeVisible,
                setApiDefFlyoutState
            ) {
                shouldApiDefsFlyoutBeVisible = false
                navController.navigate("apiDefsEditList")
            }


    }
}