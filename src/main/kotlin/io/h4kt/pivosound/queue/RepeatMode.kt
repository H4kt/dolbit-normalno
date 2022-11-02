package io.h4kt.pivosound.queue

enum class RepeatMode(
    val displayName: String
) {
    CURRENT_TRACK("Current track"),
    QUEUE("Queue"),
    NONE("None")
}