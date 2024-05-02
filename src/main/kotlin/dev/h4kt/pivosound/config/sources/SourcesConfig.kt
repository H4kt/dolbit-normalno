package dev.h4kt.pivosound.config.sources

import kotlinx.serialization.Serializable

interface SourcesConfig {

    @Serializable
    data class UsernamePasswordCredentials(
        val username: String? = null,
        val password: String? = null
    )

    val youtube: UsernamePasswordCredentials

}
