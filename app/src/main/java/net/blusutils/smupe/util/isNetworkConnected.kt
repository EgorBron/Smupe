package net.blusutils.smupe.util

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat
import java.net.InetAddress


/**
 * Checks network connectivity status using Android's ConnectivityManager
 * @param ctx Context to use
 * @return True if connected, false otherwise
 */
@Deprecated(
    "Deprecated since Android marked way to get connectivity as obsolete",
    level = DeprecationLevel.WARNING
)
fun isNetworkConnected(ctx: Context): Boolean {
    val cm = ContextCompat.getSystemService(ctx, ConnectivityManager::class.java)
    return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
}

fun isInternetAvailable(): Boolean {
    try {
        val ipAddr = InetAddress.getByName("google.com")
        return !ipAddr.equals("")
    } catch (e: Exception) {
        return false
    }
}