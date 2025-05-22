package dev.h4kt.pivosound.config.discord

import dev.kord.common.entity.Snowflake

interface DiscordConfig {
    val token: String
    val testGuild: Snowflake?
}
