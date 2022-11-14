package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.voiceConnection
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.managers.unregisterVoiceConnection

@OptIn(KordVoice::class)
object CommandLeave : Command(
    name = "leave",
    description = "Requests bot to leave current voice channel"
) {

    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val response = interaction.deferPublicResponse()

        val connection = voiceConnection
            ?: run {

                response.respond {
                    embed {
                        title = ":x: Not connected to a voice channel"
                        description = "I cannot disconnect from somewhere I haven't connected to, dumbo"
                        color = Colors.ERROR
                    }
                }

                return
            }

        connection.run {
            audioPlayer?.destroy()
            kord.unregisterVoiceConnection(interaction.guildId)
            leave()
        }

        response.respond {
            embed {
                title = ":white_check_mark: Disconnected"
                description = "I've left the current voice channel"
                color = Colors.SUCCESS
            }
        }

    }

}