package dev.h4kt.dolbitnormalno.config.sources

import org.koin.core.annotation.Single

@Single
class DefaultSourcesConfig : SourcesConfig by SerialSourcesConfig.load()
