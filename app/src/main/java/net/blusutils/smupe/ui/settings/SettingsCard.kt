package net.blusutils.smupe.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun SettingsCard(
    titleText: String,
    description: String? = null,
    icon: ImageVector? = null,
    upperText: String? = null,
    onClick: () -> Unit = {},
    cardModifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    chip: @Composable ()->Unit = {},
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Card(onClick=onClick, modifier = cardModifier.padding(top = 8.dp, bottom = 8.dp, start = 4.dp, end = 0.dp)) {
        ListItem(
            { Text(titleText); chip() },
            Modifier.clickable { onClick() },
            { if (upperText != null) Text(upperText) },
            { if (description != null) Text(description) },
            { if (icon != null) Icon(icon, null) },
            trailingContent = {
                content()
            },
        )
    }
//    Card(onClick=onClick, modifier = cardModifier.padding(8.dp)) {
//        Row(
//            rowModifier
//                .padding(24.dp)
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row (horizontalArrangement = Arrangement.Start,
//            verticalAlignment = Alignment.CenterVertically) {
//                if (icon != null)
//                    Icon(icon, "", Modifier.padding(start = 4.dp, end = 16.dp))
//                TwoLineText(titleText, description, descriptionModifier = Modifier.width(screenWidth/2.4f))
//            }
//            content()
//        }
//    }
}