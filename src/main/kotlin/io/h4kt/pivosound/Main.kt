package io.h4kt.pivosound

import dev.kord.common.annotation.KordVoice
import io.h4kt.pivosound.commands.*
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent
import io.h4kt.pivosound.managers.audioPlayer
import io.h4kt.pivosound.managers.unregisterVoiceConnection

const val DISCORD_TOKEN = "MTAzNzM5MDU1MzcwNjM5NzgyNw.Gbnihx.FY9tuhuzGuF6EtESyeopdaS7-g_XZ_MTZ_0smg"

@OptIn(PrivilegedIntent::class, KordVoice::class)
suspend fun main() {

    val kord = Kord(token = DISCORD_TOKEN)

    CommandRepeat.register(kord)
    CommandVolume.register(kord)
    CommandQueue.register(kord)
    CommandLeave.register(kord)
    CommandJoin.register(kord)
    CommandPlay.register(kord)

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
