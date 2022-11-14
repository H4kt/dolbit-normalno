package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.DeferredPublicMessageInteractionResponseBehavior
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.hyperlink
import io.h4kt.pivosound.extensions.voiceConnection
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.managers.unregisterVoiceConnection

@OptIn(KordVoice::class)
object CommandSkip : Command(
    name = "skip",
    description = "Requests bot to skip current track"
) {

    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val response = interaction.deferPublicResponse()

        val player = voiceConnection
            ?.audioPlayer
            ?: run {

                response.respond {
                    embed {
                        title = ":x: Not connected to a voice channel"
                        description = "I cannot skip a track when I'm not in a voice channel, kiddo"
                        color = Colors.ERROR
                    }
                }

                return
            }

        val track = player.currentTrack ?: run {

            response.respond {
                embed {
                    title = ":x: Nothing is playing"
                    description = "I cannot skip a track when I'm not playing it, kiddo"
                    color = Colors.ERROR
                }
            }

            return
        }

        player.skip()

        response.respond {
            embed {
                title = ":white_check_mark: Skipped"
                description = "Skipped ${track.info.hyperlink}"
                color = Colors.SUCCESS
            }
        }

    }

}
