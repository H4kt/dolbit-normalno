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
object CommandStop : Command(
    name = "stop",
    description = "Requests bot to stop the playback and clear the queue"
) {

    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val response = interaction.deferPublicResponse()

        val player = voiceConnection
            ?.audioPlayer
            ?: run {

                response.respond {
                    embed {
                        title = ":x: Not connected to a voice channel"
                        description = "I cannot stop the playback while I'm not playing anything, kiddo"
                        color = Colors.ERROR
                    }
                }

                return
            }

        player.stop()
        player.queue.clear()

        response.respond {
            embed {
                title = ":white_check_mark: Stopped"
                description = "Playback has been stopped & queue has been cleared"
                color = Colors.SUCCESS
            }
        }

    }

}