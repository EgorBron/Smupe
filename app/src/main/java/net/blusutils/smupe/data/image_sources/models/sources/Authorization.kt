package net.blusutils.smupe.data.image_sources.models.sources

data class Authorization(
    val type: AuthType,
    val payload: String
)