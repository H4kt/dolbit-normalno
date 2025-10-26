package dev.h4kt.dolbitnormalno.kordexExtensions.commands

import dev.h4kt.dolbitnormalno.extensions.errorEmbed
import dev.h4kt.dolbitnormalno.extensions.successEmbed
import dev.h4kt.dolbitnormalno.extensions.tryRegisterToTestGuild
import dev.h4kt.dolbitnormalno.generated.i18n.Translations
import dev.h4kt.dolbitnormalno.services.audioPlayer.AudioPlayerService
import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.converters.impl.int
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import org.koin.core.component.inject

class CommandRemove : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:remove"

    class RemoveCommandArguments : Arguments() {
        val position by int {
            name = Translations.Commands.Remove.Args.Position.name
            description = Translations.Commands.Remove.Args.Position.description
        }
    }

    override suspend fun setup() {
        publicSlashCommand(::RemoveCommandArguments) {

            name = Translations.Commands.Remove.name
            description = Translations.Commands.Remove.description

            tryRegisterToTestGuild()

            action {

                val guild = guild
                    ?: return@action

                val player = audioPlayerService.getAudioPlayer(guild.id)
                if (player == null) {
                    respond {
                        errorEmbed {
                            title = ":x: Not in a voice channel"
                        }
                    }
                    return@action
                }

                val index = arguments.position.dec()
                if (index !in player.queue.indices) {
                    respond {
                        errorEmbed {
                            title = ":x: Invalid position"
                            description = "Position must be between 0 and ${player.queue.size}"
                        }
                    }
                }

                val removedMedia = player.remove(index)

                respond {
                    successEmbed {
                        title = ":white_check_mark: Removed track"
                        description = removedMedia.hyperlink()
                        image = removedMedia.thumbnailUrl
                    }
                }

            }
        }
    }

}
