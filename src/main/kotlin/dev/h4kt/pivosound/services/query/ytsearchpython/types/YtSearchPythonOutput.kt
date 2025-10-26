package dev.h4kt.pivosound.services.query.ytsearchpython.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface YtSearchPythonOutput {

    @Serializable
    @SerialName("video")
    data class Video(
        val id: String,
        val title: String,
        val channel: String,
        val thumbnail: String?,
        val duration: String
    ) : YtSearchPythonOutput


    @Serializable
    @SerialName("playlist")
    data class Playlist(
        val id: String,
        val title: String?,
        val channel: String?,
        val thumbnail: String?,
        val entries: List<Video>
    ) : YtSearchPythonOutput

}
