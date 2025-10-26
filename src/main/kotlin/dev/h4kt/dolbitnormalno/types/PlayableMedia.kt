package dev.h4kt.dolbitnormalno.types

import kotlin.time.Duration

sealed interface PlayableMedia {

    val id: String
    val title: String
    val author: String
    val duration: Duration
    val url: String
    val thumbnailUrl: String?

    fun hyperlink(): String {
        return "[$author - $title ($duration)]($url)"
    }

    data class Track(
        override val id: String,
        override val title: String,
        override val author: String,
        override val duration: Duration,
        override val url: String,
        override val thumbnailUrl: String?
    ) : PlayableMedia

    data class Playlist(
        override val id: String,
        override val title: String,
        override val author: String,
        override val url: String,
        override val thumbnailUrl: String?,
        val tracks: List<Track>
    ) : PlayableMedia {

        override val duration: Duration
            get() = tracks.fold(Duration.ZERO) { duration, track -> duration + track.duration }

    }

}
