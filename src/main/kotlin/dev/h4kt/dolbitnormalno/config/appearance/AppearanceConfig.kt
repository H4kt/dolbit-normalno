package dev.h4kt.dolbitnormalno.config.appearance

import dev.h4kt.dolbitnormalno.serializers.HexColor
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
