package dev.h4kt.pivosound.config.discord

import org.koin.core.annotation.Single

@Single
class DefaultDiscordConfig : DiscordConfig by SerializableDiscordConfig.load()
