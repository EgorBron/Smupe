package net.blusutils.smupe.ui.image_sources

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import net.blusutils.smupe.R
import net.blusutils.smupe.data.image_sources.ApiDefFileWrapper
import net.blusutils.smupe.data.image_sources.CurrentApiDefParams
import net.blusutils.smupe.data.image_sources.models.sources.LocalImageSource
import net.blusutils.smupe.data.image_sources.repository.LocalRepository
import net.blusutils.smupe.util.perms

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Preview
@Composable
fun LocalApiDefsEditScreen(src: LocalImageSource? = null, close: () -> Unit = {}) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    var force by remember { mutableStateOf(false) }
    var name by rememberSaveable { mutableStateOf("") }
    var iconUrl by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var apiLink by rememberSaveable { mutableStateOf("") }


    val act = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { dir ->
        if (dir != null) {
            apiLink = dir.toString()
        }
    }

    val storagePermission = rememberMultiplePermissionsState(perms)

    var source by remember { mutableStateOf<LocalRepository?>(null) }
    var storageAvailable by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(true) {
        storageAvailable =
            if (!storagePermission.allPermissionsGranted) {
                storagePermission.launchMultiplePermissionRequest()
                storagePermission.allPermissionsGranted
            } else true
    }

    val closeEntire = {
        if (name.isNotBlank() && apiLink.isNotBlank()) {
            if (force) close()
            source = LocalRepository(
                name = name,
                icon = iconUrl,
                description = description,
                apiLink = apiLink
            )
            if (CurrentApiDefParams.dynamicRepos.find { src?.name == it.name } != null) {
                CurrentApiDefParams.dynamicRepos.remove(CurrentApiDefParams.dynamicRepos.find { src!!.name == it.name })
            }
            CurrentApiDefParams.dynamicRepos.add(source!!)
            ApiDefFileWrapper.writeFile(
                ctx,
                "$name.json",
                source as LocalImageSource,
                LocalImageSource::class.java
            )
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
                        "Local source",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        force = true
                        closeEntire()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        OutlinedCard(
            Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {

            Card(Modifier.padding(8.dp)) {
                Text("Metadata", Modifier.padding(8.dp))
                TextField(name, { name = it },
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp), placeholder = { Text("Name") })
                TextField(iconUrl, { iconUrl = it },
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp), placeholder = { Text("Icon URL") })
                TextField(description, { description = it },
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp), placeholder = { Text("Description") })
            }
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                Text("Pick a source folder", Modifier.padding(16.dp))
                Button({ act.launch(null) }, Modifier.padding(16.dp)) {
                    Text("Choose directory")
                }
                Text(apiLink, Modifier.padding(16.dp))
            }
            Button({closeEntire()}, Modifier.padding(16.dp)) {
                Text(stringResource(R.string.save))
            }
        }
    }
}