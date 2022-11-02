package io.h4kt.pivosound

import io.h4kt.pivosound.commands.*
import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.Intents
import dev.kord.gateway.PrivilegedIntent

const val DISCORD_TOKEN = "MTAzNzM5MDU1MzcwNjM5NzgyNw.Gbnihx.FY9tuhuzGuF6EtESyeopdaS7-g_XZ_MTZ_0smg"

@OptIn(PrivilegedIntent::class)
suspend fun main() {

    val kord = Kord(token = DISCORD_TOKEN)

    CommandRepeat.register(kord)
    CommandVolume.register(kord)
    CommandQueue.register(kord)
    CommandLeave.register(kord)
    CommandJoin.register(kord)
    CommandPlay.register(kord)

    kord.login {
        intents = Intents.nonPrivileged + Intent.MessageContent
    }

}
