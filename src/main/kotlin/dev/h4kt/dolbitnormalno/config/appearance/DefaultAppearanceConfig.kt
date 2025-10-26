package dev.h4kt.dolbitnormalno.config.appearance

import org.koin.core.annotation.Single

@Single
class DefaultAppearanceConfig : AppearanceConfig by SerializableAppearanceConfig.load()
