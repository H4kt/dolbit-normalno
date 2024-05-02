package dev.h4kt.pivosound.types

enum class RepeatMode(
    val displayName: String
) {

    CURRENT_TRACK("Current track"),
    QUEUE("Queue"),
    NONE("None");

    companion object {
        fun choices() = RepeatMode.entries.associate { it.displayName to it.name }
    }

}
