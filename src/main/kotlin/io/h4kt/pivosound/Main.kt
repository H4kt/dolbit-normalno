package io.h4kt.pivosound

import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import dev.kord.common.annotation.KordVoice
import io.h4kt.pivosound.commands.*
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import io.h4kt.pivosound.config.Config
import io.h4kt.pivosound.extensions.registerCommands
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.managers.unregisterVoiceConnection

@OptIn(PrivilegedIntent::class, KordVoice::class)
suspend fun main() {

    val kord = Kord(token = Config.Discord.token).apply {
        registerCommands(
            CommandJoin,
            CommandPlay,
            CommandPause,
            CommandResume,
            CommandVolume,
            CommandRepeat,
            CommandSkip,
            CommandSeek,
            CommandRemove,
            CommandMove,
            CommandQueue,
            CommandLeave
        )
    }

    kord.on<ReadyEvent> {
        println("Log in successful")
    }

    kord.on<VoiceStateUpdateEvent> {

        val old = old
        if (old?.userId != kord.selfId && state.userId != kord.selfId) {
            return@on
        }

        if ((old?.channelId != null && state.channelId == null) || (old?.channelId != null && state.channelId != null)) {
            kord.unregisterVoiceConnection(old.guildId)?.apply {
                audioPlayer?.destroy()
                leave()
            }
        }

    }

    kord.login {
        intents = Intents.nonPrivileged + Intent.MessageContent
    }

}
