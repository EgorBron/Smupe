package net.blusutils.smupe.util

import androidx.core.net.toUri

fun checkIsLinkAndNotBlank(link: String): Boolean =
    link.isNotBlank() && checkIsLink(link)

fun checkIsLink(link: String): Boolean =
    try {link.toUri(); true} catch(_:Throwable) {false}