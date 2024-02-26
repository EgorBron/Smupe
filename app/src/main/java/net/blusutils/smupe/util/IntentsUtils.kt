package net.blusutils.smupe.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun sharePlainText(
    context: Context,
    chooserTitle: String,
    subject: String,
    summary: String
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    startChooser(context, intent, chooserTitle)
}

fun shareImage(
    context: Context,
    imageUri: Uri,
    chooserTitle: String,
    subject: String,
    summary: String
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)

    }
    startChooser(context, intent, chooserTitle)
}

private fun startChooser(context: Context, intent: Intent, chooserTitle: String) {
    context.startActivity(
        Intent.createChooser(
            intent,
            chooserTitle
        )
    )
}

fun Context.openLink(link: String) {
    this.openLink(Uri.parse(link))
}

fun Context.openLink(link: Uri) {
    this.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            link
        )
    )
}
