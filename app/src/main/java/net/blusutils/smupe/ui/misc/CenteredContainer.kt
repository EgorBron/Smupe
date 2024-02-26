package net.blusutils.smupe.ui.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun CenteredColumn(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier,
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        content()
    }
}

@Composable
fun CenteredRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Row(
        modifier,
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun CenteredContainer(reversed: Boolean = false, columnModifier: Modifier = Modifier, rowModifier: Modifier = Modifier, content: @Composable () -> Unit) {
    if (!reversed)
        CenteredColumn(columnModifier) {
            CenteredRow(rowModifier) {
                content()
            }
        }
    else
        CenteredRow(rowModifier) {
            CenteredColumn(columnModifier) {
                content()
            }
        }
}