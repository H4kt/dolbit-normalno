package io.h4kt.pivosound

import dev.kord.common.annotation.KordVoice
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import io.h4kt.pivosound.commands.*
import io.h4kt.pivosound.config.Config
import io.h4kt.pivosound.extensions.registerCommands
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.managers.getVoiceConnection
import io.h4kt.pivosound.managers.unregisterVoiceConnection
import kotlinx.coroutines.flow.count
import kotlin.time.Duration.Companion.milliseconds

@OptIn(PrivilegedIntent::class, KordVoice::class)
suspend fun main() {

    val kord = Kord(token = Config.Discord.token)

    kord.registerCommands(
        CommandJoin,
        CommandPlay,
        CommandPlayNow,
        CommandPause,
        CommandResume,
        CommandVolume,
        CommandRepeat,
        CommandSkip,
        CommandSeek,
        CommandRemove,
        CommandMove,
        CommandQueue,
        CommandLeave,
        CommandStop
    )

    kord.on<VoiceStateUpdateEvent> {

        val oldState = old
            ?: return@on

        if (oldState.userId != kord.selfId && state.userId != kord.selfId) {
            return@on
        }

        val isDisconnected = oldState.channelId != null && state.channelId == null
        if (!isDisconnected) {
            return@on
        }

        kord.unregisterVoiceConnection(oldState.guildId)?.apply {
            audioPlayer?.destroy()
            leave()
        }

    }

    kord.on<VoiceStateUpdateEvent> {

        val old = old
            ?: return@on

        val channel = state.getChannelOrNull()
            ?: return@on

        val memberCount = channel.voiceStates.count()
        if (memberCount > 1) {
            return@on
        }

        kord.unregisterVoiceConnection(old.guildId)?.apply {
            audioPlayer?.destroy()
            leave()
        }

    }

    kord.on<VoiceStateUpdateEvent> {

        val oldState = old
            ?: return@on

        val channelId = state.channelId
            ?: return@on

        if (oldState.userId != kord.selfId && state.userId != kord.selfId) {
            return@on
        }

        val audioPlayer = kord.getVoiceConnection(channelId)
            ?.audioPlayer
            ?: return@on

        when {
            !oldState.isMuted && state.isMuted -> // If became muted
                audioPlayer.isPaused = true

            oldState.isMuted && !state.isMuted -> // If became unmuted
                audioPlayer.isPaused = false
        }

    }

    val start = System.currentTimeMillis()

    kord.on<ReadyEvent> {
        val duration = (System.currentTimeMillis() - start).milliseconds
        println("Successfully authorized in $duration")
    }

    kord.login {
        intents = Intents.nonPrivileged + Intent.MessageContent
    }

}
