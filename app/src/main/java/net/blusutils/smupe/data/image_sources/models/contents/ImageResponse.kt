package net.blusutils.smupe.data.image_sources.models.contents

open class ImageResponse(
    open val id: String,
    open val source: String,
    override val url: String?,
    val title: String? = null,
    val tags: List<String>? = null,
    val author: String? = null,
    val blurHash: String? = null,
    override var occurredException: Exception? = null,
) : BaseImageContent(url, occurredException)