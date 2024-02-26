package net.blusutils.smupe.data.image_sources

import net.blusutils.smupe.data.image_sources.models.sources.BaseImageSource
import net.blusutils.smupe.data.image_sources.repository.CatApi
import net.blusutils.smupe.data.image_sources.repository.Gallery

object CurrentApiDefParams {
    var currentApi: BaseImageSource? = null
    var currentSearchQuery = ""
    var dynamicRepos = mutableListOf<BaseImageSource>(CatApi, Gallery)
}