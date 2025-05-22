package dev.h4kt.pivosound.services.connectionManager

import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Snowflake
import dev.kord.voice.VoiceConnection

@OptIn(KordVoice::class)
interface ConnectionManager {

    fun setConnection(
        guildId: Snowflake,
        connection: VoiceConnection
    )

    fun getConnection(
        guildId: Snowflake
    ): VoiceConnection?

}
