package dev.h4kt.dolbitnormalno.kordexExtensions.commands

import dev.h4kt.dolbitnormalno.extensions.errorEmbed
import dev.h4kt.dolbitnormalno.extensions.successEmbed
import dev.h4kt.dolbitnormalno.extensions.tryRegisterToTestGuild
import dev.h4kt.dolbitnormalno.generated.i18n.Translations
import dev.h4kt.dolbitnormalno.services.audioPlayer.AudioPlayerService
import dev.h4kt.dolbitnormalno.types.RepeatMode
import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.application.slash.converters.impl.enumChoice
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import org.koin.core.component.inject

class CommandRepeat : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:repeat"

    class RepeatCommandArguments : Arguments() {
        val mode by enumChoice<RepeatMode> {

            name = Translations.Commands.Repeat.Args.Mode.name
            typeName = Translations.Commands.Repeat.Args.Mode.typeName
            description = Translations.Commands.Repeat.Args.Mode.description

            choices(RepeatMode.choices())
        }
    }

    override suspend fun setup() {
        publicSlashCommand(::RepeatCommandArguments) {

            name = Translations.Commands.Repeat.name
            description = Translations.Commands.Repeat.description

            tryRegisterToTestGuild()

            action {

                val guild = guild
                    ?: return@action

                val player = audioPlayerService.getAudioPlayer(guild.id)
                    ?: run {
                        respond {
                            errorEmbed {
                                title = ":x: Not in a voice channel"
                                description = "Perhaps you can join one and ask me to play something"
                            }
                        }
                        return@action
                    }

                player.repeatMode = arguments.mode

                respond {
                    successEmbed {

                        when (arguments.mode) {
                            RepeatMode.CURRENT_TRACK -> {
                                title = ":white_check_mark: Set repeat mode to"
                                description = ":repeat_one: Current track"
                            }
                            RepeatMode.QUEUE -> {
                                title = ":white_check_mark: Set repeat mode to"
                                description = ":repeat: Queue"
                            }
                            RepeatMode.NONE -> {
                                title = ":white_check_mark: Repeat disabled"
                            }
                        }

                    }
                }

            }
        }
    }

}
