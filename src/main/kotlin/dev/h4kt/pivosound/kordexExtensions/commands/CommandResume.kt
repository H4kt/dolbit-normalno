package dev.h4kt.pivosound.kordexExtensions.commands

import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.successEmbed
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import org.koin.core.component.inject

class CommandResume : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:resume"

    override suspend fun setup() {
        publicSlashCommand {

            name = Translations.Commands.Resume.name
            description = Translations.Commands.Resume.description

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

                player.resume()

                respond {
                    successEmbed {
                        title = ":arrow_forward: Playback resumed"
                    }
                }

            }
        }
    }

}
