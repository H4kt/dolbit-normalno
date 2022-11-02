package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.newVoiceConnection
import io.h4kt.pivosound.extensions.voiceConnection
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.managers.findTrack

@OptIn(KordVoice::class)
object CommandPlay : Command(
    name = "play",
    description = "Request bot to play an audio track from given source",
    builder = {
        string("youtube", "Plays an audio sourced from YouTube")
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
                response.respond { content = "Nothing found" }
                return
            }

        connection.audioPlayer?.enqueue(track)

        response.respond {

            embed {
                title = ":white_check_mark: Added track"
                description = "[${track.info.title}](${track.info.uri})"
                color = Colors.SUCCESS
            }

        }

    }

}