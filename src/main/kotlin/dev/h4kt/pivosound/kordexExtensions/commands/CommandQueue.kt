package dev.h4kt.pivosound.kordexExtensions.commands

import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.infoEmbed
import dev.h4kt.pivosound.extensions.tryRegisterToTestGuild
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import dev.h4kt.pivosound.types.RepeatMode
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

class CommandQueue : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:queue"

    override suspend fun setup() {
        publicSlashCommand {

            name = Translations.Commands.Queue.name
            description = Translations.Commands.Queue.description

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
                val queue = player.queue

                if (currentTrack == null) {
                    respond {
                        errorEmbed {
                            title = ":x: Nothing is playing"
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
                        description = currentTrack.track.hyperlink()

                    }

                    val repeatIcon = when {
                        player.repeatMode == RepeatMode.QUEUE -> ":repeat:"
                        else -> ""
                    }

                    if (queue.isEmpty()) {
                        infoEmbed {
                            title = ":notepad_spiral: Queue $repeatIcon"
                            description = ":x: Queue is empty"
                        }
                        return@respond
                    }

                    val chunkSize = 10
                    val chunkedQueue = player.queue.chunked(chunkSize)

                    infoEmbed {
                        title = ":notepad_spiral: Queue $repeatIcon"
                        description = chunkedQueue.first()
                            .mapIndexed { index, track -> "${index.inc()}. ${track.hyperlink()}" }
                            .joinToString("\n")

                    }

                    chunkedQueue.drop(1).forEachIndexed { chunkIndex, chunk ->
                        infoEmbed {
                            description = chunk
                                .mapIndexed { index, track -> "${index.inc() + ((chunkIndex + 1) * chunkSize)}. ${track.hyperlink()}" }
                                .joinToString("\n")

                        }
                    }

                }

            }
        }
    }

}
