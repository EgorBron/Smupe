package net.blusutils.smupe.data.licenses

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

data class License(
    val title: String,
    val copyright: String,
    val link: String,
    val text: String
) {
    companion object {
        @Composable
        fun fromLicenseInResources(license: LicenseInResources): License {
            return License(
                stringResource(license.title),
                license.stringCopyright ?: stringResource(license.copyright),
                license.stringLink ?: stringResource(license.link),
                stringResource(license.text)
            )
        }
    }
}

