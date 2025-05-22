package dev.h4kt.pivosound.kordexExtensions.commands

import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.converters.impl.string
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.successEmbed
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import org.koin.core.component.inject
import kotlin.time.Duration

class CommandSeek : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:seek"

    class SeekCommandArguments : Arguments() {
        val position by string {
            name = Translations.Commands.Seek.Args.Position.name
            description = Translations.Commands.Seek.Args.Position.description
        }
    }

    override suspend fun setup() {
        publicSlashCommand(::SeekCommandArguments) {

            name = Translations.Commands.Seek.name
            description = Translations.Commands.Seek.description

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

                val position = try {
                    Duration.parse(arguments.position)
                } catch (ex: Exception) {
                    respond {
                        errorEmbed {
                            title = ":x: Invalid duration format"
                            description = "Should be in format of 1h2m3s or 1m2s or 1s"
                        }
                    }
                    return@action
                }

                val track = player.currentTrack
                    ?: run {
                        respond {
                            errorEmbed {
                                title = ":x: Nothing is playing"
                                description = "Perhaps you can ask me to play something?"
                            }
                        }
                        return@action
                    }

                if (track.info.duration < position) {
                    respond {
                        errorEmbed {
                            title = ":x: Invalid position"
                            description = "Position must be between 0s and ${track.info.duration}"
                        }
                    }
                    return@action
                }

                val positionBefore = track.position
                player.seek(position)

                if (positionBefore > position) {

                    respond {
                        successEmbed {
                            title = ":rewind: Rewound"
                            description = "Rewound track to $position"
                        }
                    }

                } else {

                    respond {
                        successEmbed {
                            title = ":fast_forward: Skipped ahead"
                            description = "Skipped ahead to $position"
                        }
                    }

                }

            }
        }
    }

}
