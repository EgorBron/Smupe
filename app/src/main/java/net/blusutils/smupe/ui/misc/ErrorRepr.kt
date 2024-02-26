package net.blusutils.smupe.ui.misc

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import net.blusutils.smupe.R

@Composable
fun ErrorRepr(sender: String, detail: String? = null, description: String? = null, tr: Throwable? = null){
    Text(stringResource(R.string.error_represent, sender))
    if (!tr?.message.isNullOrBlank()) {
        Log.d("ErrorReporter.$sender", tr!!.message!!)
        Text(tr.message!!)
    }
    if (!description.isNullOrBlank())
        Text(description)
    if (!detail.isNullOrBlank())
        Text(detail)
}