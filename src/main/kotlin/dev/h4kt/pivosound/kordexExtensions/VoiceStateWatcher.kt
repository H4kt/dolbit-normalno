package dev.h4kt.pivosound.kordexExtensions

import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kordex.core.commands.events.PublicSlashCommandInvocationEvent
import dev.kordex.core.extensions.Extension
import org.koin.core.component.inject

class VoiceStateWatcher : Extension() {

//    sealed interface VoiceEvent {
//
//        data class Connected(
//            val channelId: Snowflake
//        ) : VoiceEvent
//
//        data class Disconnected(
//            val channelId: Snowflake
//        ) : VoiceEvent
//
//        data class MuteStateChanged(
//            val isMuted: Boolean
//        ) : VoiceEvent
//
//    }

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "watcher:voice-state"

    override suspend fun setup() {

        bot.on<PublicSlashCommandInvocationEvent> {

        }

        bot.on<VoiceStateUpdateEvent> {

            val oldState = old
            val newState = state

            if (state.userId != bot.kordRef.selfId) {
                return@on
            }

            when {
//                // Connected to channel
//                oldState == null && newState != null -> {}
                // Disconnected from channel
                oldState?.channelId != null && newState.channelId == null -> {
                    audioPlayerService.unregisterAudioPlayer(state.guildId)
                }
                // Muted
                oldState?.isMuted == false && newState.isMuted -> {
                    audioPlayerService.getAudioPlayer(state.guildId)?.pause()
                }
                // Unmuted
                oldState?.isMuted == true && !newState.isMuted -> {
                    audioPlayerService.getAudioPlayer(state.guildId)?.resume()
                }
            }

        }

    }

}
