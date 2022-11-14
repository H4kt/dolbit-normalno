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
object CommandMove : Command(
    name = "move",
    description = "Requests bot to move track to a position",
    builder = {

        int("from", "Position to move from") {
            required = true
        }

        int("to", "Position to move to") {
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

        val integers = interaction.command.integers
        val from = integers["from"]!!.toInt() - 1
        val to = integers["to"]!!.toInt() - 1

        if (from == to) {

            response.respond {
                embed {
                    title = ":x: Invalid position"
                    description = "Positions must not be equal"
                    color = Colors.ERROR
                }
            }

            return
        }

        val positionRange = 0..player.queue.lastIndex
        if (from !in positionRange || to !in positionRange) {

            response.respond {
                embed {
                    title = ":x: Invalid position"
                    description = "Position should be in range from 1 to ${positionRange.last + 1}"
                    color = Colors.ERROR
                }
            }

            return
        }

        val newIndex = if (from < to) {
            to - 1
        } else {
            to
        }

        val track = player.queue.run {

            val track = removeAt(from)
            add(newIndex, track)

            return@run track
        }

        response.respond {
            embed {
                title = ":white_check_mark: Successful"
                description = "Moved track ${track.info.hyperlink} from #${from + 1} to #${to + 1}"
                color = Colors.SUCCESS
            }
        }

    }

}