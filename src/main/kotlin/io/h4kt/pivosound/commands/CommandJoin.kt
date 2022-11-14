package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.getSenderVoiceChannel
import io.h4kt.pivosound.extensions.newVoiceConnection
import io.h4kt.pivosound.extensions.voiceConnection

@OptIn(KordVoice::class)
object CommandJoin : Command(
    name = "join",
    description = "Request bot to join your voice channel"
) {

    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val response = interaction.deferPublicResponse()
        val channel = getSenderVoiceChannel()

        val connection = try {

            voiceConnection?.let {

                response.respond {
                    embed {
                        title = ":x: Already in a voice channel"
                        description = "I'm already connected to a voice channel"
                        color = Colors.ERROR
                    }
                }

                return
            } ?: newVoiceConnection(channel)

        } catch (ex: Exception) {

            response.respond {
                embed {
                    title = ":x: Unable to join"
                    description = "I'm unable to join your channel"
                    color = Colors.ERROR
                }
            }

            return
        }

        if (connection != null) {
            response.respond {
                embed {
                    title = ":white_check_mark: Connected"
                    description = "Connected to ${channel?.asChannel()?.name}"
                    color = Colors.SUCCESS
                }
            }
        } else {
            response.respond {
                embed {
                    title = ":x: Could not connect"
                    description = "You have to be in a voice channel for me to connect"
                    color = Colors.ERROR
                }
            }
        }

    }

}