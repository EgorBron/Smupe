package net.blusutils.smupe.util

import android.content.Context
import android.os.Build

/**
 * List of all permissions that are needed for the app.
 */
val Context.globalAppPermissions: List<String>
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(android.Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }