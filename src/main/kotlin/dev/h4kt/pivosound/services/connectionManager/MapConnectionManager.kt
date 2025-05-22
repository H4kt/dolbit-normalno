package dev.h4kt.pivosound.services.connectionManager

import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Snowflake
import dev.kord.voice.VoiceConnection

@OptIn(KordVoice::class)
class MapConnectionManager : ConnectionManager {

    private val connectionByGuildId = mutableMapOf<Snowflake, VoiceConnection>()

    override fun setConnection(
        guildId: Snowflake,
        connection: VoiceConnection
    ) {
        connectionByGuildId[guildId] = connection
    }

    override fun getConnection(
        guildId: Snowflake
    ): VoiceConnection? = connectionByGuildId[guildId]

}
