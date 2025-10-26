package dev.h4kt.dolbitnormalno.extensions

import dev.h4kt.dolbitnormalno.config.discord.DiscordConfig
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kordex.core.commands.application.ApplicationCommand
import org.koin.core.component.inject

fun <TEvent : InteractionCreateEvent> ApplicationCommand<TEvent>.tryRegisterToTestGuild() {
    val discordConfig by inject<DiscordConfig>()
    discordConfig.testGuild?.let { guild(it) }
}
