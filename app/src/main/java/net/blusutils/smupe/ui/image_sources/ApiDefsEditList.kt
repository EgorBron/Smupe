package net.blusutils.smupe.ui.image_sources

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import net.blusutils.smupe.R
import net.blusutils.smupe.data.image_sources.ApiDefFileWrapper.apiDefDirectory
import net.blusutils.smupe.data.image_sources.CurrentApiDefParams
import net.blusutils.smupe.data.image_sources.models.sources.BaseImageSource
import net.blusutils.smupe.data.image_sources.models.sources.BuiltinSource
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiDefsEditList(navigator: NavHostController, close: () -> Unit) {
    val ctx = LocalContext.current

    var currentSource by remember { mutableStateOf<BaseImageSource?>(null) }
    var tmpSrc by remember { mutableStateOf<BaseImageSource?>(null) }
    var forceOpen by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val openEdit = { it: BaseImageSource ->
        currentSource = it
    }

    val handleDelete = {
        showDeleteDialog = false
        tmpSrc?.let {
            Log.d("ApiDefsEditList", "Deleting $it")
            CurrentApiDefParams.dynamicRepos.remove(it)
            CurrentApiDefParams.currentApi = CurrentApiDefParams.dynamicRepos.random()
            File(ctx.apiDefDirectory, "${it.name}.json").delete()
            tmpSrc = null
        }
        Unit
    }

    Log.d("ApiDefsEditList...", "$forceOpen, $currentSource, ${forceOpen || currentSource != null}")

    // Yeah, yet another BAD hack! I love you, Compose!
    AnimatedVisibility(forceOpen || currentSource != null) {
        Log.d("ApiDefsEditList", "Opening edit")
        AddApiDefScreen({ navigator.navigate("addInternetApi") }, { navigator.navigate("addLocalApi") }) {
            currentSource = null
            forceOpen = false
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Default.WarningAmber, null, Modifier.scale(1.2f)) },
            dismissButton = { TextButton(onClick = handleDelete) { Text("Yes") } },
            confirmButton = { Button({
                showDeleteDialog = false
            }) { Text("No") } },
            title = {
                Text("Are you sure?")
            },
            text = {
                Text("You are about to delete this source.\nThis action cannot be undone!")
            }
        )
    }

    AnimatedVisibility(!(forceOpen || currentSource != null)) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            stringResource(R.string.list_of_api_definitions),
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
                    },
                    actions = {
                        IconButton(onClick = { forceOpen = true }) {
                            Icon(Icons.Default.Add, null)
                        }
                    }
                )
            }
        ) {
            if (showDeleteDialog) Text("waiting")
            LazyColumn(Modifier.padding(it)) {
                items(CurrentApiDefParams.dynamicRepos) {
                    if (it !is BuiltinSource)
                        Card({ openEdit(it) },  Modifier.padding(16.dp)) {
                            ListItem(
                                { Text(it.name) },
                                Modifier.clickable { openEdit(it) },
                                {},
                                { it.description?.let { Text(it) } },
                                { it.icon?.let {
                                    AsyncImage(it, null)
                                } },
                                trailingContent = {
                                    var menuExpanded by remember { mutableStateOf(false) }
                                    tmpSrc = it

                                    Icon(Icons.Default.MoreVert, null,
                                        Modifier
                                            .padding(0.dp)
                                            .clickable { menuExpanded = true }
                                    )
                                    DropdownMenu(menuExpanded, { menuExpanded = false }) {
                                        DropdownMenuItem({
                                            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Edit, "")
                                                Text("Edit")
                                            }
                                        }, onClick = { openEdit(it) })
                                        DropdownMenuItem({
                                            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.Delete, "")
                                                Text("Remove")
                                            }
                                        }, onClick = { showDeleteDialog = true })
                                    }
                                },
                            )
                        }
                }
            }
        }
    }
}