package net.blusutils.smupe.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore
import net.blusutils.smupe.ui.misc.CenteredColumn
import net.blusutils.smupe.util.formatByKeywords

@Preview
@Composable
fun TextTemplateDialog(hideDialog: () -> Unit = {}) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val settingsDs = context.settingsDataStore
    val settings by settingsDs.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val defaultTemplate = stringResource(R.string.share_dialog)
    var template by remember { mutableStateOf(defaultTemplate) }

    LaunchedEffect(true) {
        if (settings.shareTemplate != "" && settings.shareTemplate != defaultTemplate)
            template = settings.shareTemplate
    }

    val applyChanges = {
        scope.launch {
            settingsDs.updateData {
                it.toBuilder().setShareTemplate(template).build()
            }
        }.invokeOnCompletion { hideDialog() }
        Unit
    }

    Dialog(onDismissRequest = applyChanges) {
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
                    stringResource(R.string.settings_share_template),
                    style = MaterialTheme.typography.headlineSmall
                )

                HorizontalDivider(Modifier.padding(4.dp))
                CenteredColumn(Modifier.padding(0.dp, 8.dp)) {
                    Column(horizontalAlignment = Alignment.Start) {

                        Text(
                            "Tip: you can put some data to your template! " +
                                    "When you'll open share dialog, everything between {curly brackets} will be replaced with such data!\n" +
                                    "{link} will be replaced with link to image\n" +
                                    "{app} will be \"Smupe!\"\n" +
                                    "(more soon)",
                                style = MaterialTheme.typography.bodySmall
                        )
                        HorizontalDivider()
                        Text(
                            template.formatByKeywords(
                                mapOf(
                                    "app" to "Smupe!",
                                    "link" to "https://smupe.vercel.app/cdn/images/example.png",
                                    "api" to "Smupe! API"
                                ),
                                false
                            )
                        )
                        HorizontalDivider()
                        TextField(value = template, onValueChange = { template = it })
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    TextButton(
                        onClick = applyChanges
                    ) {
                        Text(stringResource(R.string.apply_action))
                    }
                }
            }
        }
    }
}