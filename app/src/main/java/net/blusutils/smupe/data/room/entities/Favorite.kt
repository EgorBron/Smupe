package net.blusutils.smupe.data.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey
    val id: String, // API_DEF-UID-API_ID
    val link: String,
    val title: String?,
    //@Embedded val meta: ImageMeta?
)