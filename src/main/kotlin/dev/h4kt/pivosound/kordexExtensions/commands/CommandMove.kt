package dev.h4kt.pivosound.kordexExtensions.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.int
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.successEmbed
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import dev.h4kt.pivosound.services.audioPlayer.types.results.MoveResult
import org.koin.core.component.inject

class CommandMove : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:move"

    class MoveCommandArguments : Arguments() {

        val from by int {
            name = "from"
            description = "Position to move from"
        }

        val to by int {
            name = "to"
            description = "Position to move to"
        }

    }

    override suspend fun setup() {
        publicSlashCommand(::MoveCommandArguments) {

            name = "move"
            description = "Move a track to another position"

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
