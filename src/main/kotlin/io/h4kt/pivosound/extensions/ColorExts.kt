package io.h4kt.pivosound.extensions

import dev.kord.common.Color

fun Color.Companion.fromHEX(hex: String): Color {

    val rawValue = if (hex.startsWith("#")) {
        hex.substring(1)
    } else {
        hex
    }

    if (rawValue.length != 6) {
        throw RuntimeException("Invalid hex value")
    }

    val (r, g, b) = rawValue.chunked(2).map { it.toInt(16) }

    return Color(r, g, b)
}