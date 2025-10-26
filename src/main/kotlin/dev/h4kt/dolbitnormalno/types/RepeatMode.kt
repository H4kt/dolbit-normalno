package dev.h4kt.dolbitnormalno.types

import dev.h4kt.dolbitnormalno.generated.i18n.Translations
import dev.kordex.core.commands.application.slash.converters.ChoiceEnum
import dev.kordex.core.i18n.types.Key

enum class RepeatMode(
    override val readableName: Key
) : ChoiceEnum {

    CURRENT_TRACK(Translations.Enum.RepeatMode.currentTrack),
    QUEUE(Translations.Enum.RepeatMode.queue),
    NONE(Translations.Enum.RepeatMode.none);

    companion object {
        fun choices() = RepeatMode.entries.associateBy { it.readableName }
    }

}
