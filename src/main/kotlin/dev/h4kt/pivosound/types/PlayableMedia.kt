package dev.h4kt.pivosound.types

sealed interface PlayableMedia {

    val id: String
    val title: String
    val author: String
    val url: String
    val thumbnailUrl: String?

    fun hyperlink(): String {
        return "[$author - $title]($url)"
    }

    data class Track(
        override val id: String,
        override val title: String,
        override val author: String,
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
    ) : PlayableMedia

}
