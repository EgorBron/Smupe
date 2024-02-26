package net.blusutils.smupe.ui.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.blusutils.smupe.R
import net.blusutils.smupe.data.proto_datastore.SettingsProtobufSerializer
import net.blusutils.smupe.data.proto_datastore.settingsDataStore

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SettingsMenu(
    closeSettings: () -> Unit = {}
) {

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var shouldThemeDialogBeShown by remember {
        mutableStateOf(false)
    }
    var shouldLanguageDialogBeShown by remember {
        mutableStateOf(false)
    }
    var shouldMenuOrderDialogBeShown by remember {
        mutableStateOf(false)
    }
    var shouldTextTemplateDialogBeShown by remember {
        mutableStateOf(false)
    }

    val showThemeDialog = { shouldThemeDialogBeShown = true }
    val showLanguageDialog = { shouldLanguageDialogBeShown = true }
    val showMenuOrderDialog = { shouldMenuOrderDialogBeShown = true }
    val showTextTemplateDialog = { shouldTextTemplateDialogBeShown = true }

    val hideThemeDialog = { shouldThemeDialogBeShown = false }
    val hideLanguageDialog = { shouldLanguageDialogBeShown = false }
    val hideMenuOrderDialog = { shouldMenuOrderDialogBeShown = false }
    val hideTextTemplateDialog = { shouldTextTemplateDialogBeShown = false }

    Scaffold(
        snackbarHost = {SnackbarHost(snackbarHostState)},
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = closeSettings) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { pad ->
        if (shouldThemeDialogBeShown)
            ThemeDialog(hideThemeDialog)
        if (shouldLanguageDialogBeShown)
            LanguageDialog(hideLanguageDialog)
        if (shouldMenuOrderDialogBeShown)
            MenuOrderDialog(hideMenuOrderDialog)
        if (shouldTextTemplateDialogBeShown)
            TextTemplateDialog(hideTextTemplateDialog)

        Column(modifier = Modifier.padding(pad)) {
            Surface(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp, 16.dp)
                ) {

                    stickyHeader {
                        Card(
                            Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                stringResource(R.string.settings_appearance),
                                Modifier.padding(8.dp),
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            HorizontalDivider(Modifier.padding(8.dp))
                        }
                    }
                    item { AppThemeCard(showThemeDialog) }
                    item { LanguageCard(showLanguageDialog) }


                    stickyHeader {
                        Card(
                            Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                stringResource(R.string.settings_app_controls),
                                Modifier.padding(8.dp),
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            HorizontalDivider(Modifier.padding(8.dp))
                        }
                    }

//                    item { ShowButtonControlsCard() }
//                    item { ReverseControlsCard() }
                    item { ActionSwipeToSaveCard() }
                    item { MenuElementsCard(showMenuOrderDialog) }


                    stickyHeader {
                        Card(
                            Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                stringResource(R.string.settings_behaviour),
                                Modifier.padding(8.dp),
                                style = MaterialTheme.typography.headlineSmall,
                            )
                            HorizontalDivider(Modifier.padding(8.dp))
                        }
                    }

//                    item { CacheCapacityCard() }
                    item { AutoSavesCard() }
                    item { PreferSimpleShareCard() }
                    item { ShareTextTemplateCard(showTextTemplateDialog) }
                    item { LockOrientationCard() }
//                    item { AdultModeCard() }
//                    item { ProxyServerCard() }
//                    item { BumpFavesCard() }
                    item { EnableCrashReportsCard() }
                }
            }
        }
    }
}



@Composable
private fun AppThemeCard(showDialog: () -> Unit) {
    SettingsCard(
        stringResource(R.string.settings_app_theme),
        stringResource(R.string.settings_app_theme_description),
        Icons.Default.Palette,
        onClick = showDialog
    ) {
        IconButton(onClick = showDialog) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null)
        }
    }
}

@Composable
private fun LanguageCard(showDialog: () -> Unit) {
    SettingsCard(
        stringResource(R.string.settings_language),
        stringResource(R.string.settings_language_description),
        Icons.Default.Translate,
        onClick = showDialog
    ) {
        IconButton(onClick = showDialog) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null)
        }
    }
}




@Composable
private fun ShowButtonControlsCard() {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val showButtonControls = settings.buttonsShown
    val onChange = { value: Boolean ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setButtonsShown(value).build()
            }
        }
        Unit
    }
    SettingsCard(
        stringResource(R.string.settings_show_button_controls),
        stringResource(R.string.settings_show_button_controls_description),
        Icons.Default.TouchApp,
        onClick = { onChange(!showButtonControls) }
    ) {
        Switch(
            checked = showButtonControls,
            onCheckedChange = onChange
        )
    }
}

@Composable
private fun ReverseControlsCard() {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val reverseControls = settings.controlsReversed
    val onChange = { value: Boolean ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setControlsReversed(value).build()
            }
        }
        Unit
    }
    SettingsCard(
        stringResource(R.string.settings_reverse_image_controls),
        stringResource(R.string.settings_reverse_image_controls_description),
        Icons.Default.CropRotate,
        onClick = { onChange(!reverseControls) }
    ) {
        Switch(
            checked = reverseControls,
            onCheckedChange = onChange
        )
    }
}

@Composable
private fun ActionSwipeToSaveCard() {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val swipeToSave = settings.actionSwipeIsSave
    val onChange = { value: Boolean ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setActionSwipeIsSave(value).build()
            }
        }
        Unit
    }

    SettingsCard(
        stringResource(R.string.settings_action_swipe_to_save),
        stringResource(R.string.settings_action_swipe_to_save_description),
        Icons.Default.SwipeRight,
        onClick = { onChange(!swipeToSave) },
        chip = { TBDChip() }
    ) {
        Switch(
            checked = swipeToSave,
            onCheckedChange = onChange,
            enabled = false
        )
    }
}

@Composable
private fun AutoSavesCard() {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val autoSaves = settings.autoSavesEnabled
    val onChange = { value: Boolean ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setAutoSavesEnabled(value).build()
            }
        }
        Unit
    }

    SettingsCard(
        stringResource(R.string.settings_auto_saves),
        stringResource(R.string.settings_auto_saves_description),
        Icons.Default.SaveAlt,
        onClick = { onChange(!autoSaves) },
        chip = { TBDChip() }
    ) {
        Switch(
            checked = autoSaves,
            onCheckedChange = onChange,
            enabled = false
        )
    }
}

@Composable
private fun MenuElementsCard(showDialog: () -> Unit) {
    SettingsCard(
        stringResource(R.string.settings_menu_elements_order),
        stringResource(R.string.settings_menu_elements_order_description),
        Icons.AutoMirrored.Default.List,
        onClick = { showDialog() }
    ) {
        IconButton(onClick = { showDialog() }) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null)
        }
    }
}

//@Composable
//private fun CacheCapacityCard() {
//
//    val scope = rememberCoroutineScope()
//    val ctx = LocalContext.current
//    val settingsStore = ctx.settingsDataStore
//    val settings by settingsStore.data.collectAsState(
//        SettingsProtobufSerializer.defaultValue)
//
//    val cacheValue = settings.cacheCapacityKB
//    var cacheInputValue by rememberSaveable { mutableStateOf("") }
//    val onChange = { value: String ->
//        cacheInputValue = value
//        if (value != "" && value.isDigitsOnly()) {
//            scope.launch {
//                settingsStore.updateData {
//                    it.toBuilder().setCacheCapacityKB(value.toLong()).build()
//                }
//            }
//        }
//        Unit
//    }
//
//    SettingsCard(
//        stringResource(R.string.settings_cache_capacity),
//        stringResource(R.string.settings_cache_capacity_description),
//        Icons.Default.Cached
//    ) {
//        TextField(
//            textStyle = MaterialTheme.typography.labelSmall,
//            value = cacheInputValue.ifBlank { cacheValue.toString() },
//            keyboardOptions =
//            KeyboardOptions(
//                keyboardType = KeyboardType.Number
//            ),
//            onValueChange = onChange
//        )
//    }
//}

@Composable
private fun PreferSimpleShareCard() {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val preferSimpleShare = settings.isSimpleSharePreferred
    val onChange = { value: Boolean ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setIsSimpleSharePreferred(value).build()
            }
        }
        Unit
    }

    SettingsCard(
        stringResource(R.string.settings_prefer_simple_share),
        stringResource(R.string.settings_prefer_simple_share_description),
        Icons.Default.Textsms,
        onClick = { onChange(!preferSimpleShare) }
    ) {
        Switch(
            checked = preferSimpleShare,
            onCheckedChange = onChange
        )
    }
}

@Composable
private fun ShareTextTemplateCard(showDialog: () -> Unit) {
    SettingsCard(
        stringResource(R.string.settings_share_template),
        stringResource(R.string.settings_share_template_description),
        Icons.Default.EditNote,
        onClick = showDialog
    ) {
        IconButton(onClick = showDialog) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null)
        }
    }
}

@Composable
private fun LockOrientationCard() {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val orientationLocked = settings.orientationLocked
    val onChange = { value: Boolean ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setOrientationLocked(value).build()
            }
        }
        Unit
    }

    SettingsCard(
        stringResource(R.string.settings_lock_orientation),
        stringResource(R.string.settings_lock_orientation_description),
        Icons.Default.PhonelinkLock,
        onClick = { onChange(!orientationLocked) }
    ) {
        Switch(
            checked = orientationLocked,
            onCheckedChange = onChange
        )
    }
}

@Composable
private fun AdultModeCard() {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val adultMode = settings.isAdultModeEnabled
    val onChange = { value: Boolean ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setIsAdultModeEnabled(value).build()
            }
        }
        Unit
    }

    SettingsCard(
        stringResource(R.string.settings_adult_mode),
        stringResource(R.string.settings_adult_mode_description),
        Icons.Default.NoAccounts,
        onClick = { onChange(!adultMode) }
    ) {
        Switch(
            checked = adultMode,
            onCheckedChange = onChange
        )
    }
}

@Composable
private fun ProxyServerCard() {
    SettingsCard(
        stringResource(R.string.settings_proxy_server),
        stringResource(R.string.settings_proxy_server_description),
        Icons.Default.ElectricalServices,
        chip = { TBDChip() }
    ) {
        IconButton(onClick = {TODO()}) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null)
        }
    }
}

@Composable
private fun BumpFavesCard() {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val bumpFaves = settings.bumps.isBumpEnabled
    val onChange = { value: Boolean ->
//        scope.launch {
//            settingsStore.updateData {
//                it.toBuilder().setIsInternationalModeEnabled(value).build()
//            }
//        }
        Unit
    }

    SettingsCard(
        stringResource(R.string.settings_bump_faves),
        stringResource(R.string.settings_bump_faves_description),
        Icons.Default.CloudUpload,
        onClick = { onChange(!bumpFaves) },
        chip = { TBDChip() }
    ) {
//        Switch(
//            checked = bumpFaves || (isSettingsInitialized() && settingsModel.isLocalHostBumpEnabled),
//            onCheckedChange = onChange
//        )
        IconButton(onClick = {TODO()}) {
            Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null)
        }
    }
}

@Composable
private fun EnableCrashReportsCard() {
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current
    val settingsStore = ctx.settingsDataStore
    val settings by settingsStore.data.collectAsState(
        SettingsProtobufSerializer.defaultValue)

    val enableCrashReports = settings.crashReportsEnabled
    val onChange = { value: Boolean ->
        scope.launch {
            settingsStore.updateData {
                it.toBuilder().setCrashReportsEnabled(value).build()
            }
        }
        Unit
    }


    SettingsCard(
        stringResource(R.string.settings_send_crash_logs),
        stringResource(R.string.settings_send_crash_logs_description),
        Icons.Default.BugReport,
        onClick = { onChange(!enableCrashReports) },
        chip = { TBDChip() }
    ) {
        Switch(
            checked = enableCrashReports,
            onCheckedChange = onChange
        )
    }
}