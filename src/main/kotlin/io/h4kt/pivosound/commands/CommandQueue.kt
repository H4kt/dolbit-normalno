package io.h4kt.pivosound.commands

import io.h4kt.pivosound.config.Colors
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.rest.builder.message.modify.embed
import io.h4kt.pivosound.extensions.hyperlink
import io.h4kt.pivosound.extensions.voiceConnection
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.queue.RepeatMode
import kotlin.time.Duration

@OptIn(KordVoice::class)
object CommandQueue : Command(
    name = "queue",
    description = "Shows a list of currently queued tracks"
) {

    override suspend fun GuildChatInputCommandInteractionCreateEvent.execute() {

        val response = interaction.deferPublicResponse()

        val player = voiceConnection?.audioPlayer
        val queue = player?.queue ?: emptyList()
        val currentTrack = player?.currentTrack

        val repeatTrackSign: String
        val repeatQueueSign: String

        when(player?.repeatMode) {
            RepeatMode.CURRENT_TRACK -> {
                repeatTrackSign = ":repeat_one:"
                repeatQueueSign = ""
            }
            RepeatMode.QUEUE -> {
                repeatTrackSign = ""
                repeatQueueSign = ":repeat:"
            }
            else -> {
                repeatTrackSign = ""
                repeatQueueSign = ""
            }
        }

        response.respond {

            embed {
                title = ":mirror_ball: Currently playing $repeatTrackSign"
                description = currentTrack?.info?.hyperlink ?: ":x: Nothing is currently playing"
                color = Colors.INFO
            }

            embed {

                title = ":notepad_spiral: Queue $repeatQueueSign"

                description = if (queue.isNotEmpty()) {
                    queue.foldIndexed("") { index, acc, it ->
                        "$acc${index + 1}. ${it.info.hyperlink}\n"
                    }
                } else {
                    ":x: Queue is empty"
                }

                color = Colors.INFO

            }

        }

    }

}