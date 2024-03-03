package net.blusutils.smupe.util

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Start a plaintext share intent with the given data.
 * @param context The Android context to use.
 * @param chooserTitle The share dialog title.
 * @param subject The subject of the share message (available in e-mails).
 * @param message The message itself.
 */
fun sharePlainText(
    context: Context,
    chooserTitle: String,
    subject: String,
    message: String
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, message)
    }
    startChooser(context, intent, chooserTitle)
}

/**
 * Start an image file share intent with the given data.
 * @param context The Android context to use.
 * @param imageUri The URI object pointing to the image.
 * @param chooserTitle The share dialog title.
 * @param subject The subject of the share message (available in e-mails).
 * @param message The message itself.
 */
fun shareImage(
    context: Context,
    imageUri: Uri,
    chooserTitle: String,
    subject: String,
    message: String
) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, message)

    }
    startChooser(context, intent, chooserTitle)
}

/**
 * Start a chooser intent.
 * @param context The Android context to use.
 * @param intent The intent to start as chooser.
 * @param chooserTitle The chooser dialog title.
 */
private fun startChooser(
    context: Context,
    intent: Intent,
    chooserTitle: String
) {
    context.startActivity(
        Intent.createChooser(
            intent,
            chooserTitle
        )
    )
}

/**
 * Open a link.
 * @param link The link to open.
 */
fun Context.openLink(link: String) {
    this.openLink(Uri.parse(link))
}

/**
 * Open a link ([Uri] version).
 * @param link The link to open.
 */
fun Context.openLink(link: Uri) {
    this.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            link
        )
    )
}
