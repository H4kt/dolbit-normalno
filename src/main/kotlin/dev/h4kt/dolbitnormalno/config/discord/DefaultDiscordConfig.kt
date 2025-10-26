package dev.h4kt.dolbitnormalno.config.discord

import org.koin.core.annotation.Single

@Single
class DefaultDiscordConfig : DiscordConfig by SerializableDiscordConfig.load()
