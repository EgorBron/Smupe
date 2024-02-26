package net.blusutils.smupe.data.image_sources.models.sources

class SearchInfo(
    supported: Boolean,
    val url: String,
    path: String,
    val idPath: String,
) : SupportingApiFeature(supported, path)