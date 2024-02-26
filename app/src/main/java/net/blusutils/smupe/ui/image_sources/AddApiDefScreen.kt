package net.blusutils.smupe.ui.image_sources

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.blusutils.smupe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddApiDefScreen(openAddInternet: () -> Unit, openAddLocal: () -> Unit, close: () -> Unit) {
    Log.d("AddApiDefScreen", "AddApiDefScreen")
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(R.string.add_new_api),
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
            Text(stringResource(R.string.add_api_type_of_api), Modifier.padding(32.dp))
            Card({ openAddInternet() },
                Modifier
                    .fillMaxWidth(.9f)
                    .padding(16.dp)) {
                Text(stringResource(R.string.add_api_remote_api), Modifier.padding(32.dp))
            }
            Card({ openAddLocal() },
                Modifier
                    .fillMaxWidth(.9f)
                    .padding(16.dp)) {
                Text(stringResource(R.string.add_api_local_directory), Modifier.padding(32.dp))
            }
        }
    }
}
