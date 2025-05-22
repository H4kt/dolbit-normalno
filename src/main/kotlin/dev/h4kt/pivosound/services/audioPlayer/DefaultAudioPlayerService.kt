package dev.h4kt.pivosound.services.audioPlayer

import dev.h4kt.pivosound.services.audioPlayer.types.AudioPlayer
import dev.kord.common.entity.Snowflake
import org.koin.core.annotation.Single

@Single
class DefaultAudioPlayerService : AudioPlayerService {

    private val audioPlayerByGuildId = mutableMapOf<Snowflake, AudioPlayer>()

    override fun createAudioPlayer(
        guildId: Snowflake
    ): AudioPlayer {
        return AudioPlayer()
            .also {
                audioPlayerByGuildId[guildId] = it
            }
    }

    override fun unregisterAudioPlayer(
        guildId: Snowflake
    ) = audioPlayerByGuildId.remove(guildId)

    override fun getAudioPlayer(
        guildId: Snowflake
    ) = audioPlayerByGuildId[guildId]

}
