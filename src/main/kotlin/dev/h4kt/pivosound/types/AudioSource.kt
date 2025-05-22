package dev.h4kt.pivosound.types

import dev.h4kt.pivosound.generated.i18n.Translations
import dev.kordex.core.i18n.types.Key

enum class AudioSource(
    val displayNameKey: Key
) {

    YOUTUBE(Translations.Enum.AudioSource.youtube),
    SOUNDCLOUD(Translations.Enum.AudioSource.soundcloud),;

    companion object {
        fun choices() = entries.associate { it.displayNameKey to it.name }
    }

}
