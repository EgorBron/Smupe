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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import net.blusutils.smupe.R

@Composable
fun MenuCard(
    onClick: () -> Unit,
    upperText: Int,
    bottomText: Int,
    icon: ImageVector,
    iconDescription: String? = null
) {
    Card(onClick = onClick, modifier = Modifier.padding(vertical = 8.dp)) {
        ListItem(
            modifier = Modifier.padding(8.dp),
            headlineContent = { Text(stringResource(upperText)) },
            supportingContent = { Text(stringResource(bottomText)) },
            leadingContent = { Icon(icon, iconDescription) },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        )
    }
}

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
                    .padding(36.dp)
                    .zIndex(2f)
            ) {
                item {
                    MenuCard(
                        onClick = { stateHoist(onFavesOpen) },
                        upperText = R.string.favorites_title,
                        bottomText = R.string.favorites_menu_description,
                        icon = Icons.Default.FavoriteBorder
                    )
                }

                item {
                    MenuCard(
                        onClick = { stateHoist(onApiDefsOpen) },
                        upperText = R.string.api_defs_title,
                        bottomText = R.string.api_defs_menu_description,
                        icon = Icons.Default.Api
                    )
                }

                item {
                    MenuCard(
                        onClick = { stateHoist(onSettingsOpen) },
                        upperText = R.string.settings_title,
                        bottomText = R.string.settings_menu_description,
                        icon = Icons.Default.Settings
                    )
                }

                item {
                    MenuCard(
                        onClick = { stateHoist(onAboutOpen) },
                        upperText = R.string.about_title,
                        bottomText = R.string.about_menu_description,
                        icon = Icons.Default.Info
                    )
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



