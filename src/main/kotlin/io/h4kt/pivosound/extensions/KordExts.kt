package io.h4kt.pivosound.extensions

import dev.kord.core.Kord
import io.h4kt.pivosound.commands.Command

suspend fun Kord.registerCommands(vararg commands: Command) {
    commands.forEach { it.register(this) }
}
