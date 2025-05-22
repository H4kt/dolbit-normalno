package dev.h4kt.pivosound.types

import dev.h4kt.pivosound.generated.i18n.Translations
import dev.kordex.core.commands.application.slash.converters.ChoiceEnum
import dev.kordex.core.i18n.types.Key

enum class AudioSource(
    override val readableName: Key
) : ChoiceEnum {

    YOUTUBE(Translations.Enum.AudioSource.youtube),
    SOUNDCLOUD(Translations.Enum.AudioSource.soundcloud);

    companion object {

        fun choices() = entries.associateBy { it.readableName }

        fun fromDisplayName(
            displayName: String
        ) = entries.find { it.readableName.translate() == displayName }

    }

}
