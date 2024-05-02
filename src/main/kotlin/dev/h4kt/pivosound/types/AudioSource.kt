package dev.h4kt.pivosound.types

enum class AudioSource(
    val displayName: String
) {

    YOUTUBE("Youtube"),
    SOUNDCLOUD("Sound Cloud");

    companion object {
        fun choices() = entries.associate { it.displayName to it.name }
    }

}
