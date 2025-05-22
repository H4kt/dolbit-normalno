package dev.h4kt.pivosound.kordexExtensions.commands

import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.infoEmbed
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import dev.h4kt.pivosound.types.RepeatMode
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

class CommandQueue : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:queue"

    override suspend fun setup() {
        publicSlashCommand {

            name = Translations.Commands.Queue.name
            description = Translations.Commands.Queue.description

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
                val queue = player.queue

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

                    infoEmbed {

                        val repeatIcon = when {
                            player.repeatMode == RepeatMode.CURRENT_TRACK -> ":repeat_one:"
                            else -> ""
                        }

                        title = ":loud_sound: Current track @ ${currentTrack.position.inWholeSeconds.seconds} $repeatIcon"
                        description = currentTrack.info.hyperlink()

                    }

                    infoEmbed {

                        val repeatIcon = when {
                            player.repeatMode == RepeatMode.CURRENT_TRACK -> ":repeat:"
                            else -> ""
                        }

                        title = ":notepad_spiral: Queue $repeatIcon"
                        description = when {
                            queue.isNotEmpty() ->
                                queue.mapIndexed { index, track -> "${index.inc()}. ${track.hyperlink()}" }
                                    .joinToString("\n")
                            else -> ":x: Queue is empty"
                        }

                    }

                }

            }
        }
    }

}
