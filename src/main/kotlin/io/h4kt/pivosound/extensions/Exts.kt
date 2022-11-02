@file:OptIn(KordVoice::class)

package io.h4kt.pivosound.extensions

import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.channel.BaseVoiceChannelBehavior
import dev.kord.core.behavior.channel.connect
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.voice.AudioFrame
import dev.kord.voice.AudioProvider
import dev.kord.voice.VoiceConnection
import io.h4kt.pivosound.managers.createAudioPlayer
import io.h4kt.pivosound.managers.getVoiceConnection
import io.h4kt.pivosound.managers.registerAudioPlayer
import io.h4kt.pivosound.managers.registerVoiceConnection

val GuildChatInputCommandInteractionCreateEvent.voiceConnection: VoiceConnection?
    get() = kord.getVoiceConnection(interaction.guildId)

suspend fun GuildChatInputCommandInteractionCreateEvent.getSenderVoiceChannel(): BaseVoiceChannelBehavior? {
    return interaction.user
        .asMemberOrNull()
        .getVoiceStateOrNull()
        ?.getChannelOrNull()
}

suspend fun GuildChatInputCommandInteractionCreateEvent.newVoiceConnection(
    channel: BaseVoiceChannelBehavior?
): VoiceConnection? {

    if (channel == null) {
        return null
    }

    val player = createAudioPlayer()

    return channel
        .connect {
            audioProvider = AudioProvider { AudioFrame.fromData(player.provide()?.data) }
            selfDeaf = true
        }
        .apply {
            kord.registerVoiceConnection(interaction.guildId, this)
            registerAudioPlayer(player)
        }
}

suspend fun GuildChatInputCommandInteractionCreateEvent.newVoiceConnection() = newVoiceConnection(getSenderVoiceChannel())