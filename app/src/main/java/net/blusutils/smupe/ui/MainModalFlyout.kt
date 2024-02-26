package net.blusutils.smupe.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.ui.misc.CardButton
import net.blusutils.smupe.ui.misc.TwoLineText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainModalFlyout(
    visibility: Boolean = false,
    visibilityHoist: (Boolean) -> Unit,
    onFavesOpen: () -> Unit = {},
    onApiDefsOpen: () -> Unit = {},
    onSettingsOpen: () -> Unit = {},
    onAboutOpen: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState(true)
    val stateHoist = { it: () -> Unit ->
        scope.launch {
            state.hide()
        }.invokeOnCompletion {
            if (!state.isVisible) {
                visibilityHoist(false)
                it()
            }
        }
        Unit
    }
    ModalBottomSheet(
        sheetState = state,
        onDismissRequest = { stateHoist {} },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(0f)
        ) {
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )

            HorizontalDivider(Modifier.padding(16.dp))

            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .zIndex(2f)
            ) {
                item {
                    CardButton(onClick = { stateHoist(onFavesOpen) }) {
                        Icon(Icons.Default.FavoriteBorder, null, Modifier.padding(4.dp, 0.dp, 16.dp, 0.dp))
                        TwoLineText(
                            stringResource(R.string.favorites_title),
                            stringResource(R.string.favorites_menu_description)
                        )
                    }
                }

                item {
                    CardButton(onClick = { stateHoist(onApiDefsOpen) }) {
                        Icon(Icons.Default.Api, null, Modifier.padding(4.dp, 0.dp, 16.dp, 0.dp))
                        TwoLineText(
                            stringResource(R.string.api_defs_title),
                            stringResource(R.string.api_defs_menu_description)
                        )
                    }
                }

                item {
                    CardButton(onClick = { stateHoist(onSettingsOpen) }) {
                        Icon(Icons.Default.Settings, null, Modifier.padding(4.dp, 0.dp, 16.dp, 0.dp))
                        TwoLineText(
                            stringResource(R.string.settings_title),
                            stringResource(R.string.settings_menu_description)
                        )
                    }
                }

                item {
                    CardButton(onClick = { stateHoist(onAboutOpen) }) {
                        Icon(Icons.Default.Info, null, Modifier.padding(4.dp, 0.dp, 16.dp, 0.dp))
                        TwoLineText(
                            stringResource(R.string.about_title),
                            stringResource(R.string.about_menu_description)
                        )
                    }
                }
            }
            HorizontalDivider(Modifier.padding(16.dp))
            Text(
                stringResource(R.string.copyright_footer),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp, 24.dp, 12.dp, 72.dp)
            )
        }
    }
    if (visibility) LaunchedEffect(true) { state.expand() }
}



