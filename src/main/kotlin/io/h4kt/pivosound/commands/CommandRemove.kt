package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.hyperlink
import io.h4kt.pivosound.extensions.voiceConnection
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.managers.unregisterVoiceConnection

@OptIn(KordVoice::class)
object CommandRemove : Command(
    name = "remove",
    description = "Requests bot to remove track from the queue",
    builder = {
        int("position", "Position to remove track at") {
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
                        title = ":x: Not connected to a voice channel"
                        description = "I cannot remove a track while I'm not playing anything, kiddo"
                        color = Colors.ERROR
                    }
                }

                return
            }

        val maxPosition = player.queue.lastIndex
        val position = interaction.command.integers["position"]!!.toInt()

        if (position > maxPosition) {

            response.respond {
                embed {
                    title = ":x: Invalid position"
                    description = "There is no track at requested position ($position > $maxPosition)"
                    color = Colors.ERROR
                }
            }

            return
        }

        val track = player.queue.removeAt(position)

        response.respond {
            embed {
                title = ":white_check_mark: Successful"
                description = "Removed track ${track.info.hyperlink}"
                color = Colors.SUCCESS
            }
        }

    }

}