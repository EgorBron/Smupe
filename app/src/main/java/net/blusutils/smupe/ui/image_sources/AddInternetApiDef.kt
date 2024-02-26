package net.blusutils.smupe.ui.image_sources

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.blusutils.smupe.R
import net.blusutils.smupe.ui.misc.CenteredColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInternetApiDef(close: () -> Unit) {

    var showModeDialog by rememberSaveable { mutableStateOf(false) }
    var mode by rememberSaveable { mutableIntStateOf(0) }

    if (showModeDialog)
        Dialog(onDismissRequest = { showModeDialog = false }) {
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
                        "Select mode",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    HorizontalDivider(Modifier.padding(4.dp))

                    CenteredColumn(Modifier.padding(0.dp, 8.dp)) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Row( // TODO move radiobuttons to composable
                                Modifier
                                    .fillMaxWidth()
                                    .selectable(mode == 0, true) { mode = 0 },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(mode == 0, { mode = 0 })
                                Text("Resource directly on link")
                            }
                            Row(
                                Modifier.fillMaxWidth()
                                    .selectable(mode == 1, true){ mode = 1 },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(mode == 1, { mode = 1 })
                                Text("Resource is a plain text link")
                            }
                            Row(
                                Modifier.fillMaxWidth()
                                    .selectable(mode == 2, true){ mode = 2 },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(mode == 2, { mode = 2 })
                                Text("Resource directly on link")
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        TextButton(
                            onClick = { showModeDialog = false },
                        ) {
                            Text(stringResource(R.string.apply_action))
                        }
                    }
                }
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Add new API",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = close) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        Column(Modifier.padding(it)) {
            when (mode) {
                0 -> AddDirectLinkApi(close)
                1 -> AddPlainTextApi(close)
                2 -> AddJsonApi(close)
            }
        }
    }
}

@Composable
fun AddDirectLinkApi(close: () -> Unit) {

}

@Composable
fun AddPlainTextApi(close: () -> Unit) {

}

@Composable
fun AddJsonApi(close: () -> Unit) {

}