package net.blusutils.smupe.ui.settings

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.Theme
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore
import net.blusutils.smupe.ui.misc.CenteredColumn

val dynColorAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@Composable
fun ThemeDialog(
    hideDialog: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val themePreference = settings.themePreference.number
    val dynamicColor = settings.dynamicColorEnabled

    val onChange = { value: Int ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setThemePreference(Theme.forNumber(value)).build()
            }
        }
        Unit
    }

    val onChecked = { value: Boolean ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setDynamicColorEnabled(value).build()
            }
        }
    }

    Dialog(onDismissRequest = hideDialog) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                Modifier.padding(32.dp, 32.dp, 32.dp, 16.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    stringResource(R.string.settings_theme_and_colors),
                    style = MaterialTheme.typography.headlineSmall
                )

                HorizontalDivider(Modifier.padding(4.dp))

                CenteredColumn(Modifier.padding(0.dp, 8.dp)) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(themePreference == 0, true){ onChange(0) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(themePreference == 0, { onChange(0) })
                            Text(stringResource(R.string.settings_system_theme))
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(themePreference == 1, true){ onChange(1) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(themePreference == 1, { onChange(1) })
                            Text(stringResource(R.string.settings_light_theme))
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(themePreference == 2, true){ onChange(2) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(themePreference == 2, { onChange(2) })
                            Text(stringResource(R.string.settings_dark_theme))
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable(dynColorAvailable) { onChecked(!dynamicColor) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(dynamicColor, { onChecked(it) }, enabled = dynColorAvailable)
                            Text(stringResource(R.string.settings_dynamic_theme))
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    TextButton(
                        onClick = hideDialog
                    ) {
                        Text(stringResource(R.string.apply_action))
                    }
                }
            }
        }
    }
}