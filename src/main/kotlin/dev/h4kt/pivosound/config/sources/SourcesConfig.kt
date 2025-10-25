package dev.h4kt.pivosound.config.sources

import kotlinx.serialization.Serializable

interface SourcesConfig {

    @Serializable
    data class YouTube(
        val remoteCipherUrl: String,
        val remoteCipherPassword: String? = null
    )

    val youtube: YouTube

}
