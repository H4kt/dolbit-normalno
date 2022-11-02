package io.h4kt.pivosound.extensions

import dev.kord.common.Color
import dev.kord.common.kColor
import java.awt.Color as AwtColor

fun Color.Companion.fromHEX(hex: String) = AwtColor.decode(hex).kColor