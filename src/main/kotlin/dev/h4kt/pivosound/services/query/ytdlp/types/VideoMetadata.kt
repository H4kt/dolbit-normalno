package dev.h4kt.pivosound.services.query.ytdlp.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("video")
data class VideoMetadata(
    val id: String,
    val title: String,
    val channel: String,
    @SerialName("thumbnail")
    val thumbnailUrl: String? = null,
    val duration: Int
) : YtdlpOutput
