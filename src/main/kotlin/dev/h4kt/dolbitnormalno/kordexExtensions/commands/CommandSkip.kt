package dev.h4kt.dolbitnormalno.kordexExtensions.commands

import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.h4kt.dolbitnormalno.extensions.errorEmbed
import dev.h4kt.dolbitnormalno.extensions.successEmbed
import dev.h4kt.dolbitnormalno.extensions.tryRegisterToTestGuild
import dev.h4kt.dolbitnormalno.generated.i18n.Translations
import dev.h4kt.dolbitnormalno.services.audioPlayer.AudioPlayerService
import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.converters.impl.defaultingInt
import org.koin.core.component.inject

class CommandSkip : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:skip"

    class SkipCommandArguments : Arguments() {
        val count by defaultingInt {
            name = Translations.Commands.Skip.Args.Count.name
            description = Translations.Commands.Skip.Args.Count.description
            minValue = 1
            defaultValue = 1
        }
    }

    override suspend fun setup() {
        publicSlashCommand(::SkipCommandArguments) {

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

                if (arguments.count == 1) {
                    respond {
                        successEmbed {
                            title = ":white_check_mark: Skipped track"
                            description = currentTrack.track.hyperlink()
                        }
                    }
                } else {
                    respond {
                        successEmbed {
                            title = ":white_check_mark: Skipped ${arguments.count} tracks"
                        }
                    }
                }


                player.skip(arguments.count)

            }
        }
    }

}
