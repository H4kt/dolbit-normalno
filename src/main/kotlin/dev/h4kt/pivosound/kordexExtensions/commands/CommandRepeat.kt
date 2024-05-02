package dev.h4kt.pivosound.kordexExtensions.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.converters.impl.stringChoice
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.successEmbed
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import dev.h4kt.pivosound.services.query.LavaplayerQueryService
import dev.h4kt.pivosound.types.RepeatMode
import org.koin.core.component.inject

class CommandRepeat : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()
    private val lavaplayerQueryService by inject<LavaplayerQueryService>()

    override val name = "command:repeat"

    class RepeatCommandArguments : Arguments() {
        val mode by stringChoice {

            name = "mode"
            description = "Repeat mode"

            choices(RepeatMode.choices())
        }
    }

    override suspend fun setup() {
        publicSlashCommand(::RepeatCommandArguments) {

            name = "repeat"
            description = "Sets repeat mode for current server"

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

                val mode = RepeatMode.valueOf(arguments.mode)
                player.repeatMode = mode

                respond {
                    successEmbed {

                        when (mode) {
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
