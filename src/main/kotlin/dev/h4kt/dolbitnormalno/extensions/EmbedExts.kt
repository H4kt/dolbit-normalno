package dev.h4kt.dolbitnormalno.extensions

import dev.h4kt.dolbitnormalno.config.appearance.AppearanceConfig
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.MessageBuilder
import dev.kord.rest.builder.message.embed
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

context(builderContext: MessageBuilder, koin: KoinComponent)
inline fun errorEmbed(builder: EmbedBuilder.() -> Unit) {

    val appearance by koin.inject<AppearanceConfig>()

    builderContext.embed {
        apply(builder)
        color = appearance.colors.error
    }

}

context(builderContext: MessageBuilder, koin: KoinComponent)
inline fun infoEmbed(builder: EmbedBuilder.() -> Unit) {

    val appearance by koin.inject<AppearanceConfig>()

    builderContext.embed {
        apply(builder)
        color = appearance.colors.info
    }

}

context(builderContext: MessageBuilder, koin: KoinComponent)
inline fun successEmbed(builder: EmbedBuilder.() -> Unit) {

    val appearance by koin.inject<AppearanceConfig>()

    builderContext.embed {
        apply(builder)
        color = appearance.colors.success
    }

}
