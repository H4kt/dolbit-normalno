package io.h4kt.pivosound.commands

import dev.kord.core.Kord
import dev.kord.core.entity.application.ChatInputCommandCommand
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.on
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import io.h4kt.pivosound.extensions.args
import kotlin.system.measureTimeMillis

abstract class Command(
    val name: String,
    val description: String,
    val builder: GlobalChatInputCreateBuilder.() -> Unit = {}
) {

    lateinit var handle: ChatInputCommandCommand

    abstract suspend fun GuildChatInputCommandInteractionCreateEvent.execute()

    suspend fun register(kord: Kord) {
        println("Registered command $name in ${measureTimeMillis { _register(kord) }} ms")
    }

    suspend fun _register(kord: Kord) {

        handle = kord.createGlobalChatInputCommand(name, description, builder)

        kord.on<GuildChatInputCommandInteractionCreateEvent> {

            if (interaction.invokedCommandId != handle.id) {
                return@on
            }

            val user = interaction.user
            val guild = interaction.guild.fetchGuild()
            val command = interaction.command

            val argsStr = command.options
                .map { (key, value) -> "$key:${value.value}" }
                .joinToString(" ")

            println("User ${user.tag} @ \"${guild.name}\" (guildId=${guild.id}) issued bot command: $name $argsStr")

            try {
                execute()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }

    }

}