package net.blusutils.smupe.ui.about

import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.blusutils.smupe.R
import net.blusutils.smupe.data.licenses.License
import net.blusutils.smupe.data.licenses.LicenseInResources
import net.blusutils.smupe.data.licenses.LicensesRepo
import net.blusutils.smupe.util.openLink


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(close: () -> Unit = {}) {
    val context = LocalContext.current
    var version: String? = null
    var versionBuild: Int? = null
    try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        version = pInfo.versionName
        versionBuild = pInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    var showEasterEgg by remember { mutableStateOf(false) }
    var shouldDialogBeShown by remember { mutableStateOf(false) }
    var shouldAllLicensesDialogBeShown by remember { mutableStateOf(false) }

    if (showEasterEgg)
        EasterEgg { showEasterEgg = false }

    if (shouldAllLicensesDialogBeShown)
        LicensesList { shouldAllLicensesDialogBeShown = false }

    if (shouldDialogBeShown)
        LicenseDialog(
            License.fromLicenseInResources(
                LicenseInResources(
                    LicensesRepo.apache,
                    R.string.copyright,
                    R.string.license,
                    LicensesRepo.apache_text
                )
            )
        ) { shouldDialogBeShown = false }

    Scaffold(
        Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                title = {},
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
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .padding(vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            Arrangement.SpaceBetween,
            Alignment.CenterHorizontally
        ) {
            Column(
                Modifier.padding(vertical = 64.dp),
                Arrangement.spacedBy(16.dp),
                Alignment.CenterHorizontally,
            ) {
                Column(
                    Modifier,
                    Arrangement.spacedBy(8.dp),
                    Alignment.CenterHorizontally
                ) {
                    var clicks by remember { mutableIntStateOf(0) }
                    ElevatedCard(
                        {
                            if (clicks < 5) clicks++ else {
                                showEasterEgg = true; clicks = 0
                            }
                        },
                        Modifier.size(108.dp, 108.dp),
                        colors = CardDefaults.let {
                            if (clicks < 5) cardColors(
                                containerColor = Color(0xFF110011),
                            ) else cardColors()
                        },
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            if (clicks < 5) {
                                Image(painterResource(R.drawable.smupe_icon_background), null)
                                Image(painterResource(R.drawable.smupe_icon_foreground), null)
                            } else {
                                Column {
                                    Text("???")
                                    Icon(Icons.Default.Egg, null)
                                    Text("???")
                                }
                            }
                        }
                    }
                    Text(
                        if (clicks < 5) stringResource(R.string.app_name)
                        else stringResource(R.string.egg_what),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    version?.let {
                        versionBuild?.let {
                            Text(
                                if (clicks < 5) "v$version ($versionBuild)"
                                else stringResource(R.string.egg_how),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                    Text(
                        if (clicks < 5) stringResource(R.string.slogan)
                        else stringResource(R.string.egg_wait)
                    )
                    Text(
                        if (clicks < 5) stringResource(R.string.credits_created_by, stringResource(R.string.creator_name))
                        else stringResource(R.string.egg_no_way)
                    )
                }
                val appWebSite = stringResource(R.string.app_website)
                val appGitHub = stringResource(R.string.app_github)
                val appBugTracker = stringResource(R.string.bug_tracker)
                val appPrivacy = stringResource(R.string.privacy_policy)
                val appForkLink = stringResource(R.string.fork_link)

                Column(
                    Modifier,
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Row(
                        Modifier,
                        Arrangement.spacedBy(8.dp)
                    ) {
                        TooltipBox(
                            TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(stringResource(R.string.read_privacy)) } },
                            state = rememberTooltipState()
                        ) {
                            FloatingActionButton({ context.openLink(appPrivacy) }) {
                                Icon(Icons.Default.PrivacyTip, null)
                            }
                        }

                        TooltipBox(
                            TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(stringResource(R.string.visit_website)) } },
                            state = rememberTooltipState()
                        ) {
                            FloatingActionButton({ context.openLink(appWebSite) }) {
                                Icon(Icons.Default.Web, null)
                            }
                        }

                        TooltipBox(
                            TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(stringResource(R.string.star_on_github)) } },
                            state = rememberTooltipState()
                        ) {
                            FloatingActionButton({ context.openLink(appGitHub) }) {
                                Icon(Icons.Default.Star, null)
                            }
                        }
                    }
                    Row(
                        Modifier.padding(vertical = 8.dp),
                        Arrangement.spacedBy(8.dp)
                    ) {
                        TooltipBox(
                            TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(stringResource(R.string.report_a_bug)) } },
                            state = rememberTooltipState()
                        ) {
                            FloatingActionButton({ context.openLink(appBugTracker) }) {
                                Icon(Icons.Default.BugReport, null)
                            }
                        }

                        TooltipBox(
                            TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text(stringResource(R.string.contribute)) } },
                            state = rememberTooltipState()
                        ) {
                            FloatingActionButton({ context.openLink(appForkLink) }) {
                                Icon(Icons.Default.ForkRight, null)
                            }
                        }
                    }
                }
            }

            Column(
                Modifier,
                Arrangement.spacedBy(4.dp),
                Alignment.CenterHorizontally
            ) {
                Text(
                    stringResource(R.string.copyright_footer),
                    style = MaterialTheme.typography.bodySmall
                )
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)) {
                    ClickableText(
                        AnnotatedString(stringResource(R.string.view_license)),
                        style = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.primary)
                    ) {
                        shouldDialogBeShown = true
                    }
                    ClickableText(
                        AnnotatedString(stringResource(R.string.view_3rd_party_licenses)),
                        style = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.primary)
                    ) {
                        shouldAllLicensesDialogBeShown = true
                    }
                }
            }
        }
    }
}