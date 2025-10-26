package dev.h4kt.dolbitnormalno.config.discord

import dev.kord.common.entity.Snowflake

interface DiscordConfig {
    val token: String
    val testGuild: Snowflake?
}
