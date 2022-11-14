package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.DeferredPublicMessageInteractionResponseBehavior
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.voiceConnection
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.managers.unregisterVoiceConnection
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(KordVoice::class)
object CommandSeek : Command(
    name = "seek",
    description = "Requests bot to seek to given time",
    builder = {
        string("time", "Time to seek to") {
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
                        description = "I cannot seek while I'm not in a voice channel, kiddo"
                        color = Colors.ERROR
                    }
                }

                return
            }

        val track = player.currentTrack ?: run {

            response.respond {
                embed {
                    title = ":x: Nothing is playing"
                    description = "I cannot seek while I'm not playing anything, kiddo"
                    color = Colors.ERROR
                }
            }

            return
        }

        val timeStr = interaction.command.strings["time"]!!
        val time = Duration.parseOrNull(timeStr)?.inWholeMilliseconds ?: run {

            response.respond {
                embed {
                    title = ":x: Invalid time string"
                    description = "Specify time in a valid format like 1h2m3s"
                    color = Colors.ERROR
                }
            }

            return
        }

        if (time !in 0..track.duration) {

            response.respond {
                embed {
                    title = ":x: Invalid time"
                    description = "Seek time must be in range from 0s to ${track.duration.milliseconds})"
                    color = Colors.ERROR
                }
            }

            return
        }

        player.seek(time)

        response.respond {
            embed {
                title = ":white_check_mark: Successful"
                description = "Seeked track to ${time.milliseconds}"
                color = Colors.SUCCESS
            }
        }

    }

}
