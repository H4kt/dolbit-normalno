package io.h4kt.pivosound.extensions

import dev.kord.core.entity.interaction.InteractionCommand

val InteractionCommand.args: Map<String, Any>
    get() = HashMap<String, Any>().apply {

        strings.forEach { (key, value) -> this[key] = value }
        booleans.forEach { (key, value) -> this[key] = value }
        integers.forEach { (key, value) -> this[key] = value }

    }