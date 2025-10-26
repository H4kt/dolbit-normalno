package dev.h4kt.pivosound

import dev.h4kt.pivosound.config.ConfigModule
import dev.h4kt.pivosound.config.discord.DiscordConfig
import dev.h4kt.pivosound.config.discord.SerializableDiscordConfig
import dev.h4kt.pivosound.kordexExtensions.VoiceStateWatcher
import dev.h4kt.pivosound.kordexExtensions.commands.CommandJoin
import dev.h4kt.pivosound.kordexExtensions.commands.CommandLeave
import dev.h4kt.pivosound.kordexExtensions.commands.CommandMove
import dev.h4kt.pivosound.kordexExtensions.commands.CommandPause
import dev.h4kt.pivosound.kordexExtensions.commands.CommandPlay
import dev.h4kt.pivosound.kordexExtensions.commands.CommandQueue
import dev.h4kt.pivosound.kordexExtensions.commands.CommandRemove
import dev.h4kt.pivosound.kordexExtensions.commands.CommandRepeat
import dev.h4kt.pivosound.kordexExtensions.commands.CommandResume
import dev.h4kt.pivosound.kordexExtensions.commands.CommandSeek
import dev.h4kt.pivosound.kordexExtensions.commands.CommandSkip
import dev.h4kt.pivosound.kordexExtensions.commands.CommandTest
import dev.h4kt.pivosound.services.ServicesModule
import dev.kord.common.entity.PresenceStatus
import dev.kordex.core.ExtensibleBot
import dev.kordex.core.utils.loadModule
import org.koin.ksp.generated.module

suspend fun main() {

    val discordConfig: DiscordConfig = SerializableDiscordConfig.load()

    val bot = ExtensibleBot(discordConfig.token) {

        hooks {
            beforeKoinSetup {
                loadModule(createdAtStart = true) {
                    includes(
                        ConfigModule().module,
                        ServicesModule().module
                    )
                }
            }
        }

        extensions {

            add(::VoiceStateWatcher)

//            add(::CommandTest)

            add(::CommandJoin)
            add(::CommandLeave)
            add(::CommandPlay)
            add(::CommandPause)
            add(::CommandResume)
            add(::CommandSeek)
            add(::CommandMove)
            add(::CommandSkip)
            add(::CommandQueue)
            add(::CommandRemove)
            add(::CommandRepeat)

        }

        presence {
            status = PresenceStatus.Online
            listening("your commands")
        }

    }

    bot.start()

}
