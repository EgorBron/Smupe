package net.blusutils.smupe.ui.about

import android.graphics.Typeface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.blusutils.smupe.R
import net.blusutils.smupe.data.licenses.License
import net.blusutils.smupe.data.repos.LicensesRepo.licenses
import net.blusutils.smupe.util.openLink


@Composable
fun LicensesList(closeDialog: () -> Unit) {
    var showLicenseDetails by remember { mutableStateOf<License?>(null) }
    if (showLicenseDetails != null) {
        LicenseDialog(showLicenseDetails!!) {
            showLicenseDetails = null
        }
    } else Dialog(closeDialog) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(.8f)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    stringResource(R.string.view_3rd_party_licenses),
                    Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Card(
                    Modifier
                        .padding(4.dp, 16.dp)
                        .fillMaxHeight(.9f)
                ) {
                    LazyColumn {
                        items(licenses) {
                            val license = License.fromLicenseInResources(it.license)
                            ListItem(
                                headlineContent = { Text(it.title) },
                                Modifier.clickable { showLicenseDetails = license },
                                supportingContent = { Text(license.title) }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    TextButton(
                        onClick = closeDialog
                    ) {
                        Text(stringResource(R.string.dismiss_dialog))
                    }
                }
            }
        }
    }
}

@Composable
fun LicenseDialog(license: License, closeDialog: () -> Unit) {
    val ctx = LocalContext.current

    Dialog(closeDialog) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(.8f)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
            ) {
                Column(
                    Modifier.padding(24.dp)
                ) {
                    Text(
                        license.title,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    ClickableText(
                        buildAnnotatedString { append(stringResource(R.string.view_license_file)) },
                        style = MaterialTheme.typography.bodySmall.copy(MaterialTheme.colorScheme.primary)
                    ) {
                        ctx.openLink(license.link)
                    }

                    Column {
                        Text(
                            license.text.format(license.copyright),
                            Modifier
                                .padding(4.dp, 16.dp)
                                .fillMaxHeight(.9f)
                                .verticalScroll(rememberScrollState()),
                            style = MaterialTheme.typography.bodyMedium
                                .copy(
                                    fontFeatureSettings = "thum",
                                    fontFamily = FontFamily(
                                        Typeface.MONOSPACE
                                    )
                                )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            TextButton(
                                onClick = closeDialog
                            ) {
                                Text(stringResource(R.string.dismiss_dialog))
                            }
                        }
                    }
                }
            }
        }
    }
}