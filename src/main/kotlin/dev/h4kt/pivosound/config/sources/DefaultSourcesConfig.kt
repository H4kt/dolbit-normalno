package dev.h4kt.pivosound.config.sources

import org.koin.core.annotation.Single

@Single
class DefaultSourcesConfig : SourcesConfig by SerialSourcesConfig.load()
