package net.blusutils.smupe.ui.image_sources

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.data.image_sources.CurrentApiDefParams
import net.blusutils.smupe.ui.misc.TwoLineText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiDefsSelectorFlyout(
    visibility: Boolean = false,
    visibilityHoist: (Boolean) -> Unit,
    showEditScreen: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    // misspelled, but it's ok, you should be confused here :)
    var curentApi by remember { mutableStateOf("") }
    var search by remember { mutableStateOf("") }
    var searchEnabled by remember { mutableStateOf(false) }

    val hide = {
        coroutineScope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if(!sheetState.isVisible) {
                visibilityHoist(false)
            }
        }
    }

    val performSearch = { query: String ->
        Log.d("APIDEF.performSearch", query)
        CurrentApiDefParams.currentSearchQuery = query
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { hide() },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val focusManager = LocalFocusManager.current
        val searchInner = { focusManager.clearFocus(); performSearch(search); Unit }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                Modifier.fillMaxWidth(.9f),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(showEditScreen) { Icon(Icons.Default.Edit, "") }
                Text(
                    stringResource(R.string.api_defs_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
                IconButton({}) { Icon(Icons.Default.Settings, "") }
            }
            OutlinedTextField(
                search,
                { search = it },
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
//                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) coroutineScope.launch { sheetState.expand() }
                    },
                shape = RoundedCornerShape(16.dp),
                enabled = searchEnabled,
                placeholder = { Text(stringResource(R.string.type_something)) },
                supportingText = { Text(stringResource(R.string.search_in_current_api)) },
                leadingIcon = { IconButton(searchInner) { Icon(Icons.Default.ImageSearch, "Search icon") } },
                trailingIcon = { if (search != "") IconButton({ search = "" }) { Icon(Icons.Default.Close, "Erase text in search field") } },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { searchInner() }),
                singleLine = true
            )

            HorizontalDivider(Modifier.padding(16.dp))

            LazyColumn(Modifier.padding(16.dp)) {

//                val stubItems = listOf("CaaS (Cats As A Service)", "My gallery")

                items(CurrentApiDefParams.dynamicRepos) {
                    val modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                    val onClick = {
                        curentApi = it.name
                        CurrentApiDefParams.currentApi = it
                        Log.d("APIDEF.onClick", "${CurrentApiDefParams.currentApi}")
                        searchEnabled = CurrentApiDefParams.currentApi != null
                                && CurrentApiDefParams.currentApi!!.search != null
                                && CurrentApiDefParams.currentApi!!.search!!.supported
                    }
                    val content = @Composable {
                        Row(
                            Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(it.icon, "${it.name} icon")
                            TwoLineText(it.name, it.description)
                        }
                    }
                    if (curentApi == it.name)
                        Card(onClick, modifier) { content() }
                    else OutlinedCard(onClick, modifier) { content() }
                }
            }
        }
    }

    if (visibility)
        LaunchedEffect(true) {
            sheetState.expand()
        }
}