package dev.h4kt.pivosound.config.discord

import dev.h4kt.pivosound.config.ConfigFactory
import dev.kord.common.entity.Snowflake
import kotlinx.serialization.Serializable

@Serializable
data class SerializableDiscordConfig(
    override val token: String,
    override val testGuild: Snowflake? = null
) : DiscordConfig {

    companion object : ConfigFactory<DiscordConfig>("discord.conf") {

        override fun load(path: String): DiscordConfig {
            return deserialize<SerializableDiscordConfig>(path)
        }

    }

}
