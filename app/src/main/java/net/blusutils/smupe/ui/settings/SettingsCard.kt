package net.blusutils.smupe.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import net.blusutils.smupe.ui.misc.CenteredRow

@Composable
fun SettingsCard(
    titleText: String,
    description: String? = null,
    icon: ImageVector? = null,
    upperText: String? = null,
    onClick: () -> Unit = {},
    chip: @Composable ()->Unit = {},
    content: @Composable () -> Unit
) {
    Card(onClick=onClick, modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 0.dp)) {
        ListItem(
            {
                CenteredRow {
                    Text(titleText)
                    Box(Modifier.padding(4.dp, 0.dp)) {
                        chip()
                    }
                }
            },
            Modifier.clickable { onClick() },
            { if (upperText != null) Text(upperText) },
            { if (description != null) Text(description) },
            { if (icon != null) Icon(icon, null) },
            trailingContent = {
                content()
            }
        )
    }
}