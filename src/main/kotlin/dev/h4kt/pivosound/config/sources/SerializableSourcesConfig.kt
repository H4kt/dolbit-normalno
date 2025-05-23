package dev.h4kt.pivosound.config.sources

import dev.h4kt.pivosound.config.ConfigFactory
import kotlinx.serialization.Serializable

@Serializable
data class SerializableSourcesConfig(
    override val youtube: SourcesConfig.YouTubeSourceConfig
) : SourcesConfig {

    companion object : ConfigFactory<SourcesConfig>("sources.conf") {

        override fun load(fileName: String): SourcesConfig {
            return deserialize<SerializableSourcesConfig>(fileName)
        }

    }

}
