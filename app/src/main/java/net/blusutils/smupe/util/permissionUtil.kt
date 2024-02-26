package net.blusutils.smupe.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Context.checkPermission(permission: String) =
    ContextCompat.checkSelfPermission(
        this,
        permission
    ) == PackageManager.PERMISSION_GRANTED

fun Activity.requestPermissions(vararg permissions: String, checkRationale: Boolean = true) {
    val p = permissions.filter {
        shouldShowRequestPermissionRationale(it) || !checkRationale
    }
    if (!p.none())
        ActivityCompat.requestPermissions(
            this,
            p.toTypedArray(),
            123
        )
}

val perms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    listOf(android.Manifest.permission.READ_MEDIA_IMAGES)
} else {
    listOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE)
}