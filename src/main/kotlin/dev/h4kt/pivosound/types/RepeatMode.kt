package dev.h4kt.pivosound.types

import dev.h4kt.pivosound.generated.i18n.Translations
import dev.kordex.core.i18n.types.Key

enum class RepeatMode(
    val displayNameKey: Key
) {

    CURRENT_TRACK(Translations.Enum.RepeatMode.currentTrack),
    QUEUE(Translations.Enum.RepeatMode.queue),
    NONE(Translations.Enum.RepeatMode.none);

    companion object {
        fun choices() = RepeatMode.entries.associate { it.displayNameKey to it.name }
    }

}
