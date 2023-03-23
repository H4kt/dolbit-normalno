package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.hyperlink
import io.h4kt.pivosound.extensions.newVoiceConnection
import io.h4kt.pivosound.extensions.voiceConnection
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.managers.findTrack
import kotlin.time.Duration.Companion.milliseconds

@OptIn(KordVoice::class)
object CommandPlayNow : Command(
    name = "playnow",
    description = "Requests bot to instantly play an audio track from given source",
    builder = {
        string("youtube", "Plays an audio sourced from YouTube") {
            required = true
        }
    }
) {

    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val name = interaction.command.strings["youtube"]
        val query = "ytsearch: $name"

        val response = interaction.deferPublicResponse()

        val connection = voiceConnection
            ?: newVoiceConnection()
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

        val track = findTrack(query)
            ?: run {

                response.respond {
                    embed {
                        title = ":x: Not found"
                        description = "Found nothing by term $name"
                        color = Colors.ERROR
                    }
                }

                return
            }

        val player = connection.audioPlayer
            ?: run {

                response.respond {
                    embed {
                        title = ":x: Error"
                        description = "An internal error occurred"
                        color = Colors.ERROR
                    }
                }

                return
            }

        player.apply {

            currentTrack?.let {
                val current = it.makeClone().apply { position = it.position }
                queue.addFirst(current)
            }

            player.play(track)

        }

        response.respond {
            embed {
                title = ":white_check_mark: Playing right now"
                description = "${track.info.hyperlink} (${track.duration.milliseconds})"
                color = Colors.SUCCESS
            }
        }

    }

}