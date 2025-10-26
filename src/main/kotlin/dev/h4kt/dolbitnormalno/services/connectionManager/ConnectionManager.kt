package dev.h4kt.dolbitnormalno.services.connectionManager

import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Snowflake
import dev.kord.voice.VoiceConnection

@OptIn(KordVoice::class)
interface ConnectionManager {

    fun registerConnection(
        guildId: Snowflake,
        connection: VoiceConnection
    )

    fun getConnection(
        guildId: Snowflake
    ): VoiceConnection?

    fun unregisterConnection(
        guildId: Snowflake
    )

}
