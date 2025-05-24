package dev.h4kt.pivosound.services.query.ytdlp.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("playlist")
data class PlaylistMetadata(
    val id: String,
    val title: String,
    val channel: String? = null,
    @SerialName("thumbnail")
    val thumbnailUrl: String? = null,
    val entries: List<PlaylistEntry?>
) : YtdlpOutput
