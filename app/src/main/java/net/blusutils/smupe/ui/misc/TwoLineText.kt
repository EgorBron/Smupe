package net.blusutils.smupe.ui.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TwoLineText(
    title: String,
    description: String? = null,
    titleModifier: Modifier = Modifier,
    descriptionModifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start
) {
    Row(horizontalArrangement = horizontalArrangement) {
        Column {
            Text(
                title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = titleModifier,
                textAlign = textAlign
            )
            if (description != null)
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = descriptionModifier,
                    textAlign = textAlign
                )
        }
    }
}