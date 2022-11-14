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
object CommandPause : Command(
    name = "pause",
    description = "Request bot to pause the playback"
) {

    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val response = interaction.deferPublicResponse()

        val player = voiceConnection
            ?.audioPlayer
            ?: run {

                response.respond {
                    embed {
                        title = ":x: Not connected to a voice channel"
                        description = "I cannot pause while I'm not playing anything, kiddo"
                        color = Colors.ERROR
                    }
                }

                return
            }

        if (!player.isPaused) {

            response.respond {
                embed {
                    title = ":white_check_mark: Paused"
                    description = "Playback has been paused"
                    color = Colors.SUCCESS
                }
            }

            player.isPaused = true

        } else {

            response.respond {
                embed {
                    title = ":x: Already on pause"
                    description = "How can I pause already paused playback?!"
                    color = Colors.ERROR
                }
            }

        }

    }

}