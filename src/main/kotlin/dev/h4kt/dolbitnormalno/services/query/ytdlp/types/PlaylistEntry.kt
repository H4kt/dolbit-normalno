package dev.h4kt.dolbitnormalno.services.query.ytdlp.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistEntry(
    val id: String,
    val title: String,
    val channel: String,
    @SerialName("thumbnail")
    val thumbnailUrl: String? = null,
    val duration: Int
)
