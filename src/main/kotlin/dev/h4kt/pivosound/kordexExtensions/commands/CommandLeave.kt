package dev.h4kt.pivosound.kordexExtensions.commands

import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.kordex.core.utils.selfMember
import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.successEmbed
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import dev.kord.common.annotation.KordVoice
import org.koin.core.component.inject

class CommandLeave : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:leave"

    @OptIn(KordVoice::class)
    override suspend fun setup() {
        publicSlashCommand {

            name = Translations.Commands.Leave.name
            description = Translations.Commands.Leave.description

            action {

                val guild = guild
                    ?: return@action

                val player = audioPlayerService.getAudioPlayer(guild.id)
                    ?: run {
                        respond {
                            errorEmbed {
                                title = ":x: Not in a voice channel"
                            }
                        }
                        return@action
                    }

                val channel = guild
                    .selfMember()
                    .getVoiceStateOrNull()
                    ?.getChannelOrNull()
                    ?.asChannelOrNull()
                    ?: run {
                        respond {
                            errorEmbed {
                                title = ":x: Not in a voice channel"
                            }
                        }
                        return@action
                    }

                player.destroy()

                respond {
                    successEmbed {
                        title = ":white_check_mark: Left voice channel ${channel.name}"
                    }
                }

            }
        }
    }

}
