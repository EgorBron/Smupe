package net.blusutils.smupe.util

import java.net.InetAddress

/**
 * Checks whether the internet is available.
 * @return true if the internet is available, false if any exception occurs.
 */
fun isInternetAvailable(): Boolean {
    try {
        val ipAddr = InetAddress.getByName("google.com")
        return !ipAddr.equals("")
    } catch (e: Exception) {
        return false
    }
}