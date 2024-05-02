package dev.h4kt.pivosound.types

import kotlin.time.Duration

sealed class PlayableMedia {

    abstract val title: String
    abstract val duration: Duration
    abstract val url: String?

    data class Track(
        override val title: String,
        override val duration: Duration,
        override val url: String?,
        val id: String,
        val author: String,
        val source: AudioSource
    ) : PlayableMedia() {

        fun hyperlink(): String {
            return "[$title ($duration)]($url)"
        }

    }

    data class Playlist(
        override val title: String,
        override val duration: Duration,
        override val url: String?,
        val tracks: List<Track>,
        val source: AudioSource
    ) : PlayableMedia()

}
