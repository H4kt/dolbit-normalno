package dev.h4kt.dolbitnormalno.kordexExtensions.commands

import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.converters.impl.int
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.h4kt.dolbitnormalno.extensions.errorEmbed
import dev.h4kt.dolbitnormalno.extensions.successEmbed
import dev.h4kt.dolbitnormalno.extensions.tryRegisterToTestGuild
import dev.h4kt.dolbitnormalno.generated.i18n.Translations
import dev.h4kt.dolbitnormalno.services.audioPlayer.AudioPlayerService
import dev.h4kt.dolbitnormalno.services.audioPlayer.types.results.MoveResult
import org.koin.core.component.inject

class CommandMove : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:move"

    class MoveCommandArguments : Arguments() {

        val from by int {
            name = Translations.Commands.Move.Args.From.name
            description = Translations.Commands.Move.Args.From.description
        }

        val to by int {
            name = Translations.Commands.Move.Args.To.name
            description = Translations.Commands.Move.Args.To.description
        }

    }

    override suspend fun setup() {
        publicSlashCommand(::MoveCommandArguments) {

            name = Translations.Commands.Move.Args.From.name
            description = Translations.Commands.Move.Args.From.description

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

                val from = arguments.from.dec()
                val to = arguments.to.dec()

                if (from !in player.queue.indices) {
                    respond {
                        errorEmbed {
                            title = ":x: Invalid position"
                            description = "Position should be in range from 1 to ${player.queue.size}"
                        }
                    }
                }

                val result = player.move(
                    from = from,
                    to = to
                )

                when (result) {
                    is MoveResult.InvalidPosition ->
                        respond {
                            errorEmbed {
                                title = ":x: Invalid position"
                                description = "Position should be in range from ${result.validRange.first.inc()} to ${result.validRange.last.inc()}"
                            }
                        }
                    is MoveResult.Success ->
                        respond {
                            successEmbed {
                                title = ":white_check_mark: Successful"
                                description = "Moved track ${result.track.hyperlink()} from #${from + 1} to #${to + 1}"
                            }
                        }
                }

            }
        }
    }

}
