package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.getSenderVoiceChannel
import io.h4kt.pivosound.extensions.newVoiceConnection
import io.h4kt.pivosound.extensions.voiceConnection
import io.h4kt.pivosound.managers.audioPlayer

@OptIn(KordVoice::class)
object CommandVolume : Command(
    name = "volume",
    description = "Changes bot playback volume",
    builder = {
        int("volume", "New volume to set") {
            repeat(10) { choice("$it", it.toLong()) }
            required = true
        }
    }
) {

    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val response = interaction.deferPublicResponse()

        val player = voiceConnection
            ?.audioPlayer
            ?: run {

                response.respond {
                    embed {
                        title = ":x: Not in a voice channel"
                        description = "You have to be in a voice channel for me to join"
                        color = Colors.ERROR
                    }
                }

                return
            }

        player.volume = interaction.command.integers["volume"]!!.toInt()

        response.respond {
            embed {
                title = ":white_check_mark: Changed volume"
                description = "Volume set to ${player.volume}"
                color = Colors.SUCCESS
            }
        }

    }

}