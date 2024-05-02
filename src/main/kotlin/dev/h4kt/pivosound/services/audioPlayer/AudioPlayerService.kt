package dev.h4kt.pivosound.services.audioPlayer

import dev.h4kt.pivosound.services.audioPlayer.types.AudioPlayer
import dev.kord.common.entity.Snowflake

interface AudioPlayerService {

    fun createAudioPlayer(
        guildId: Snowflake
    ): AudioPlayer

    fun unregisterAudioPlayer(
        guildId: Snowflake
    ): AudioPlayer?

    fun getAudioPlayer(
        guildId: Snowflake
    ): AudioPlayer?

}
