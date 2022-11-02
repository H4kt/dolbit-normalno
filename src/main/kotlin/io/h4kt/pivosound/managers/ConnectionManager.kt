@file:OptIn(KordVoice::class)

package io.h4kt.pivosound.managers

import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.voice.VoiceConnection

private val CONNECTION_BY_GUILD = HashMap<Snowflake, VoiceConnection>()

fun Kord.getVoiceConnection(
    guildId: Snowflake
) = CONNECTION_BY_GUILD[guildId]

fun Kord.registerVoiceConnection(
    guildId: Snowflake,
    connection: VoiceConnection
) {
    CONNECTION_BY_GUILD[guildId] = connection
}

fun Kord.unregisterVoiceConnection(
    guildId: Snowflake
) = CONNECTION_BY_GUILD.remove(guildId)
