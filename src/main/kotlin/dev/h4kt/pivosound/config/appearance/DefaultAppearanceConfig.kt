package dev.h4kt.pivosound.config.appearance

import org.koin.core.annotation.Single

@Single
class DefaultAppearanceConfig : AppearanceConfig by SerializableAppearanceConfig.load()
