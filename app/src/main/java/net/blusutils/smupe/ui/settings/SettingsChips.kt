package net.blusutils.smupe.ui.settings

import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SettingsChip(
    text: String
) {
   FilterChip(true, onClick = { /*TODO*/ }, label = {
       Text(
           text,
           style = MaterialTheme.typography.labelSmall
       )
   })
}

@Composable
fun BetaChip() = SettingsChip("BETA")

@Composable
fun TBDChip() = SettingsChip("TBD")