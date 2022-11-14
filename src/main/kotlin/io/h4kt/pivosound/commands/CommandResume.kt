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
object CommandResume : Command(
    name = "resume",
    description = "Request bot to resume the playback"
) {

    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val response = interaction.deferPublicResponse()

        val player = voiceConnection
            ?.audioPlayer
            ?: run {

                response.respond {
                    embed {
                        title = ":x: Not connected to a voice channel"
                        description = "I cannot resume while I'm not playing anything, kiddo"
                        color = Colors.ERROR
                    }
                }

                return
            }

        if (player.isPaused) {

            response.respond {
                embed {
                    title = ":white_check_mark: Resumed"
                    description = "Playback has been resumed"
                    color = Colors.SUCCESS
                }
            }

            player.isPaused = false

        } else {

            response.respond {
                embed {
                    title = ":x: Not on pause"
                    description = "How can I resume already running playback?!"
                    color = Colors.ERROR
                }
            }

        }

    }

}