package net.blusutils.smupe.util

import java.security.MessageDigest

const val API_DEF_PREFIX = "API-DEF_"

fun calculateRoomResourceId(inputData: String): String {
    // Extract the normal ID from the input data
    val normalId = when {
        inputData.startsWith("https://")
                || inputData.startsWith("http://")
                || inputData.contains("./")
                || inputData.startsWith("/") -> {
            // Extract filename from link
            inputData.substringAfterLast("/")
        }
        else -> {
            // Use the input data as is
            inputData
        }
    }

    // Calculate the SHA-1 hash of the normal ID
    val digest = MessageDigest.getInstance("SHA-1")
    val hashBytes = digest.digest(normalId.toByteArray())
    val hashString = hashBytes.joinToString("") { String.format("%02x", it) }

    // Construct the resource ID
    return "$API_DEF_PREFIX-$hashString-$normalId"
}
