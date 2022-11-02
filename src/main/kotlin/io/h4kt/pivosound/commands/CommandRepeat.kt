package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.voiceConnection
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.queue.RepeatMode

object CommandRepeat : Command(
    name = "repeat",
    description = "Changes repeat mode",
    builder = {
        string("mode", "New mode to set") {
            required = true
            RepeatMode.values().forEach { choice(it.displayName, it.name) }
        }
    }
) {

    @OptIn(KordVoice::class)
    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val response = interaction.deferPublicResponse()

        val player = voiceConnection
            ?.audioPlayer
            ?: run {

                response.respond {
                    embed {
                        title = ":x: Not connected to a voice channel"
                        description = "I cannot change repeat mode while outside of a voice channel"
                        color = Colors.ERROR
                    }
                }

                return
            }

        player.repeatMode = RepeatMode.valueOf(interaction.command.strings["mode"]!!)

        response.respond {
            embed {
                title = ":white_check_mark: Now repeating"
                description = player.repeatMode.displayName
                color = Colors.SUCCESS
            }
        }

    }

}