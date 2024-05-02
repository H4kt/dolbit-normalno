package dev.h4kt.pivosound.extensions

import dev.h4kt.pivosound.config.appearance.AppearanceConfig
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.MessageBuilder
import dev.kord.rest.builder.message.embed
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

context(MessageBuilder, KoinComponent)
inline fun errorEmbed(builder: EmbedBuilder.() -> Unit) {

    val appearance by inject<AppearanceConfig>()

    embed {
        apply(builder)
        color = appearance.colors.error
    }

}

context(MessageBuilder, KoinComponent)
inline fun infoEmbed(builder: EmbedBuilder.() -> Unit) {

    val appearance by inject<AppearanceConfig>()

    embed {
        apply(builder)
        color = appearance.colors.info
    }

}

context(MessageBuilder, KoinComponent)
inline fun successEmbed(builder: EmbedBuilder.() -> Unit) {

    val appearance by inject<AppearanceConfig>()

    embed {
        apply(builder)
        color = appearance.colors.success
    }

}
