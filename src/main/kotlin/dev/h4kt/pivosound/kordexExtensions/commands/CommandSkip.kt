package dev.h4kt.pivosound.kordexExtensions.commands

import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.successEmbed
import dev.h4kt.pivosound.extensions.tryRegisterToTestGuild
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import org.koin.core.component.inject

class CommandSkip : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:skip"

    override suspend fun setup() {
        publicSlashCommand {

            name = Translations.Commands.Skip.name
            description = Translations.Commands.Skip.description

            tryRegisterToTestGuild()

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

                val currentTrack = player.currentTrack
                if (currentTrack == null) {
                    respond {
                        errorEmbed {
                            title = ":x: Nothing is playing"
                            description = "Perhaps you can ask me to play something?"
                        }
                    }
                    return@action
                }

                respond {
                    successEmbed {
                        title = ":white_check_mark: Skipped track"
                        description = currentTrack.track.hyperlink()
                    }
                }

                player.skip()

            }
        }
    }

}
