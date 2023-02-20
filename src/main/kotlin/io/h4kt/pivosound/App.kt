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
import io.h4kt.pivosound.managers.unregisterVoiceConnection
import kotlinx.coroutines.flow.count
import kotlin.time.Duration.Companion.milliseconds

@OptIn(PrivilegedIntent::class, KordVoice::class)
suspend fun main() {

    val kord = Kord(token = Config.Discord.token).apply {
        registerCommands(
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
    }

    kord.on<VoiceStateUpdateEvent> {

        val old = old ?: return@on

        if (old.userId != kord.selfId && state.userId != kord.selfId) {
            return@on
        }

        val isDisconnected = old.channelId != null && state.channelId == null
        if (isDisconnected) {
            kord.unregisterVoiceConnection(old.guildId)?.apply {
                audioPlayer?.destroy()
                leave()
            }
        }

    }

    kord.on<VoiceStateUpdateEvent> {

        val old = old ?: return@on
        val channel = old.getChannelOrNull() ?: return@on

        val memberCount = channel.voiceStates.count()
        if (memberCount > 1) {
            return@on
        }

        kord.unregisterVoiceConnection(old.guildId)?.apply {
            audioPlayer?.destroy()
            leave()
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
