package net.blusutils.smupe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import net.blusutils.smupe.ui.SmupeTheme

class ErrorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val thread = intent.extras?.getString("thread") ?: "unknown"
        val exception = intent.extras?.getString("exception") ?: "unknown"

        setContent {
            SmupeTheme {
                AlertDialog(
                    onDismissRequest = {
                        this.finish()
                    },
                    icon = { Icon(Icons.Default.BugReport, "Bug icon", Modifier.scale(1.2f)) },
                    dismissButton = { TextButton(onClick = { this.finish() }) { Text("Exit") } },
                    confirmButton = { Button({}) { Text("Report") } },
                    title = {
                        Text(getString(R.string.well_that_s_a_bug))
                    },
                    text = {
                        Column(
                            Modifier
                                .fillMaxHeight(.5f)
                                .padding(4.dp)) {
                            Text(getString(R.string.unhandled_error_dialog_message))
                            Text("~~~~~~")
                            Text("Unhandled exception occurred in $thread thread.")
                            Text("The exception stacktrace shown below:")
                            Card(
                                Modifier
                                    .padding(4.dp)
                                    .fillMaxHeight(.9f)
                            ) {
                                Text(exception,
                                    Modifier
                                        .verticalScroll(rememberScrollState())
                                        .padding(8.dp))
                            }
                        }
                    }
                )
            }
        }
    }
}