package dev.h4kt.dolbitnormalno.kordexExtensions.commands

import dev.h4kt.dolbitnormalno.extensions.errorEmbed
import dev.h4kt.dolbitnormalno.extensions.successEmbed
import dev.h4kt.dolbitnormalno.extensions.tryRegisterToTestGuild
import dev.h4kt.dolbitnormalno.generated.i18n.Translations
import dev.h4kt.dolbitnormalno.services.audioPlayer.AudioPlayerService
import dev.h4kt.dolbitnormalno.services.connectionManager.ConnectionManager
import dev.kord.common.annotation.KordVoice
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.kordex.core.utils.selfMember
import org.koin.core.component.inject

class CommandLeave : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()
    private val connectionManager by inject<ConnectionManager>()

    override val name = "command:leave"

    @OptIn(KordVoice::class)
    override suspend fun setup() {
        publicSlashCommand {

            name = Translations.Commands.Leave.name
            description = Translations.Commands.Leave.description

            tryRegisterToTestGuild()

            action {

                val guild = guild
                    ?: return@action

                val player = audioPlayerService.getAudioPlayer(guild.id)
                val connection = connectionManager.getConnection(guild.id)

                val channel = guild
                    .selfMember()
                    .getVoiceStateOrNull()
                    ?.getChannelOrNull()
                    ?.asChannelOrNull()

                if (channel == null || connection == null) {
                    respond {
                        errorEmbed {
                            title = ":x: Not in a voice channel"
                        }
                    }
                    return@action
                }

                player?.destroy()
                connection.leave()

                audioPlayerService.unregisterAudioPlayer(guild.id)
                connectionManager.unregisterConnection(guild.id)

                respond {
                    successEmbed {
                        title = ":white_check_mark: Left voice channel ${channel.name}"
                    }
                }

            }
        }
    }

}
