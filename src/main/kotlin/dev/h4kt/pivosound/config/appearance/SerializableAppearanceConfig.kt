package dev.h4kt.pivosound.config.appearance

import dev.h4kt.pivosound.config.ConfigFactory
import kotlinx.serialization.Serializable

@Serializable
data class SerializableAppearanceConfig(
    override val colors: AppearanceConfig.Colors
) : AppearanceConfig {

    companion object : ConfigFactory<AppearanceConfig>("appearance.conf") {

        override fun load(path: String): AppearanceConfig {
            return deserialize<SerializableAppearanceConfig>(path)
        }

    }

}
