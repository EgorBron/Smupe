package net.blusutils.smupe.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.os.LocaleListCompat
import net.blusutils.smupe.R
import net.blusutils.smupe.ui.misc.CenteredColumn
import net.blusutils.smupe.util.languages

@Composable
fun LanguageDialog(hideLanguageDialog: () -> Unit) {

    var lang by remember { mutableStateOf("") }

    LaunchedEffect(true) {
        if (lang == "")
            lang = AppCompatDelegate.getApplicationLocales().toLanguageTags().split(",").firstOrNull()?:""
    }

    val applyChanges = {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(lang)
        )
        hideLanguageDialog()
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
                    stringResource(R.string.settings_language),
                    style = MaterialTheme.typography.headlineSmall
                )

                HorizontalDivider(Modifier.padding(4.dp))
                CenteredColumn(Modifier.padding(0.dp, 8.dp)) {
                    LazyColumn(horizontalAlignment = Alignment.Start) {
                        items(languages.toList()) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .selectable(lang == it.first, true){ lang = it.first },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(lang == it.first, { lang = it.first })
                                Text(it.second)
                            }
                        }
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