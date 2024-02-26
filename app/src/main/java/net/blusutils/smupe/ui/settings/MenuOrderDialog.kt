package net.blusutils.smupe.ui.settings

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore
import net.blusutils.smupe.ui.misc.CenteredRow
import net.blusutils.smupe.util.reorderList
import org.burnoutcrew.reorderable.*


@Composable
fun MenuOrderListItemElement(
    index: Int,
    icon: ImageVector,
    text: String,
    state: ReorderableState
) {
    CenteredRow(Modifier.draggedItem(state.offsetByIndex(index))) {
        ElevatedCard(
            Modifier
                .padding(0.dp, 8.dp)
                .fillMaxWidth()) {
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically
            ) {
                CenteredRow {
                    Icon(icon, null, Modifier.padding(4.dp, 0.dp))
                    Text(text, style = MaterialTheme.typography.bodyLarge)
                }
                Icon(Icons.Default.MoreVert, null, Modifier.padding(0.dp))
            }
        }
    }
}

data class MenuOrderListItem(
    var index: Int,
    val icon: ImageVector,
    val text: String,
    var composable: (@Composable (index:Int, state: ReorderableState) -> Unit)? = null
)

@Composable
fun MenuOrderDialog(hideDialog: () -> Unit = {}) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val settingsDs = context.settingsDataStore
    val settings by settingsDs.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val state = rememberReorderState()
    val saveText = stringResource(R.string.save)
    val shareText = stringResource(R.string.share)
    val openText = stringResource(R.string.open_in_browser)
    val favouriteText = stringResource(R.string.favorite)
    val apiText = stringResource(R.string.switch_api_def)
    var tasksList by remember { mutableStateOf(
            listOf(
                MenuOrderListItem(0, Icons.Default.SaveAlt, saveText),
                MenuOrderListItem(1, Icons.Default.Share, shareText),
                MenuOrderListItem(2, Icons.Default.OpenInBrowser, openText),
                MenuOrderListItem(3, Icons.Default.FavoriteBorder, favouriteText),
                MenuOrderListItem(4, Icons.Default.Api, apiText)
            ).map {
                it.composable = @Composable { index: Int, stateInner: ReorderableState ->
                    MenuOrderListItemElement(index, it.icon, it.text, stateInner)
                }
                it
            }
    )}

    var menuElementsOrder by rememberSaveable { mutableStateOf(tasksList.indices.joinToString("")) }

    val onChange = { startPos: ItemPosition, endPos: ItemPosition ->
        // I was implemented list reordering... But what did it cost?
        // Copy current tasks as mutable list
        val tasksListMutable = tasksList.toMutableList()
        // Swap element start position with other element
        val toSwap = tasksListMutable[startPos.index] // first
        tasksListMutable[startPos.index] = tasksListMutable[endPos.index]
        tasksListMutable[endPos.index] = toSwap
        // Create indices sequence
        var indices = ""
        tasksListMutable.forEach { indices += it.index }
        Log.d("MenuOrderDialog", indices)

        // Apply changes
        tasksList = tasksListMutable.toList()
        menuElementsOrder = indices
        // Apply changes to settings
        scope.launch {
            settingsDs.updateData {
                it.toBuilder().setActionMenuElementsOrder(indices).build()
            }
        }
        Unit // returns Unit
    }

    LaunchedEffect(true) {
        menuElementsOrder = settings.actionMenuElementsOrder
        tasksList = reorderList(tasksList, settings.actionMenuElementsOrder)
    }


    Dialog(onDismissRequest = hideDialog) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(32.dp, 32.dp, 32.dp, 16.dp)) {
                Text(
                    stringResource(R.string.settings_menu_elements_order),
                    Modifier.padding(0.dp, 8.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                HorizontalDivider(Modifier.padding(4.dp))
                CenteredRow {
                    LazyColumn(
                        state = state.listState,
                        modifier =
                        Modifier
                            .reorderable(state, onChange)
                            .detectReorderAfterLongPress(state),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        items(tasksList.size) { index ->
                            tasksList[index].composable?.let { it(index, state) }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
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