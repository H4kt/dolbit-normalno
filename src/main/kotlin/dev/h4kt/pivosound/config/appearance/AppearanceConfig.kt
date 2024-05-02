package dev.h4kt.pivosound.config.appearance

import dev.h4kt.pivosound.serializers.HexColor
import kotlinx.serialization.Serializable

interface AppearanceConfig {

    val colors: Colors

    @Serializable
    data class Colors(
        val success: HexColor,
        val error: HexColor,
        val info: HexColor
    )

}
