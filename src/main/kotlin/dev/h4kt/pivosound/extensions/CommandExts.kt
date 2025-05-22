package dev.h4kt.pivosound.extensions

import dev.h4kt.pivosound.config.discord.DiscordConfig
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kordex.core.commands.application.ApplicationCommand
import org.koin.core.component.inject

fun <E : InteractionCreateEvent> ApplicationCommand<E>.tryRegisterToTestGuild() {
    val discordConfig by inject<DiscordConfig>()
    discordConfig.testGuild?.let { guild(it) }
}
