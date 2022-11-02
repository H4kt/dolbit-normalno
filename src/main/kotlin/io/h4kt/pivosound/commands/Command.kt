package io.h4kt.pivosound.commands

import dev.kord.core.Kord
import dev.kord.core.entity.application.ChatInputCommandCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder

abstract class Command(
    val name: String,
    val description: String,
    val builder: GlobalChatInputCreateBuilder.() -> Unit = {}
) {

    lateinit var handle: ChatInputCommandCommand

    abstract suspend fun GuildChatInputCommandInteractionCreateEvent.execute()

    suspend fun register(kord: Kord) {

        handle = kord.createGlobalChatInputCommand(name, description, builder)

        kord.on<GuildChatInputCommandInteractionCreateEvent> {

            if (interaction.invokedCommandId != handle.id) {
                return@on
            }

            execute()

        }

    }

}