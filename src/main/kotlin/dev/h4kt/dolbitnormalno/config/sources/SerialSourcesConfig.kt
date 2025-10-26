package dev.h4kt.dolbitnormalno.config.sources

import dev.h4kt.dolbitnormalno.config.ConfigFactory
import kotlinx.serialization.Serializable

@Serializable
data class SerialSourcesConfig(
    override val youtube: SourcesConfig.YouTube
) : SourcesConfig {

    companion object : ConfigFactory<SourcesConfig>("sources.conf") {

        override fun load(fileName: String): SourcesConfig {
            return deserialize<SerialSourcesConfig>(fileName)
        }

    }

}
