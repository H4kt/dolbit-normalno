package dev.h4kt.dolbitnormalno.services.connectionManager

import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Snowflake
import dev.kord.voice.VoiceConnection
import org.koin.core.annotation.Single

@Single
@OptIn(KordVoice::class)
class MapConnectionManager : ConnectionManager {

    private val connectionByGuildId = mutableMapOf<Snowflake, VoiceConnection>()

    override fun registerConnection(
        guildId: Snowflake,
        connection: VoiceConnection
    ) {
        connectionByGuildId[guildId] = connection
    }

    override fun getConnection(
        guildId: Snowflake
    ): VoiceConnection? = connectionByGuildId[guildId]

    override fun unregisterConnection(
        guildId: Snowflake
    ) {
        connectionByGuildId -= guildId
    }

}
