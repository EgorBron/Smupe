package net.blusutils.smupe.ui.settings

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SettingsChip(
    text: String
) {
   FilterChip(true, onClick = { /*TODO*/ }, label = {
       Text(text)
   })
}

@Composable
fun BetaChip() = SettingsChip("BETA")

@Composable
fun TBDChip() = SettingsChip("ToBeDone")