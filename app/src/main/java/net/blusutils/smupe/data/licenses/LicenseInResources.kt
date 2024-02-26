package net.blusutils.smupe.data.licenses

data class LicenseInResources(
    val title: Int,
    val copyright: Int,
    val link: Int,
    val text: Int,
    val stringCopyright: String? = null,
    val stringLink: String? = null
)