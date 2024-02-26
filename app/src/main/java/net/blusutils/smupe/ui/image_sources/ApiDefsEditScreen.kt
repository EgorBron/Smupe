package net.blusutils.smupe.ui.image_sources

import androidx.compose.runtime.Composable
import net.blusutils.smupe.data.image_sources.models.sources.BaseImageSource
import net.blusutils.smupe.data.image_sources.models.sources.InternetImageSource
import net.blusutils.smupe.data.image_sources.models.sources.LocalImageSource

@Composable
fun ApiDefsEditScreen(src: BaseImageSource? = null, close: () -> Unit = {}) {
    when (src) {
        is InternetImageSource? -> JsonApiDefsEditScreen(src, close)
        is LocalImageSource? -> LocalApiDefsEditScreen(src, close)
    }
}
