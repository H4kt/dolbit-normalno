package dev.h4kt.pivosound.config.sources

import kotlinx.serialization.Serializable

interface SourcesConfig {

    val youtube: YouTubeSourceConfig

    @Serializable
    data class YouTubeSourceConfig(
        val applicationName: String,
        val clientSecretsFile: String
    )

}
