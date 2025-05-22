package dev.h4kt.pivosound

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager
import dev.h4kt.pivosound.config.ConfigModule
import dev.h4kt.pivosound.config.discord.DiscordConfig
import dev.h4kt.pivosound.config.discord.SerializableDiscordConfig
import dev.h4kt.pivosound.config.sources.SerializableSourcesConfig
import dev.h4kt.pivosound.config.sources.SourcesConfig
import dev.h4kt.pivosound.kordexExtensions.VoiceStateWatcher
import dev.h4kt.pivosound.kordexExtensions.commands.CommandJoin
import dev.h4kt.pivosound.kordexExtensions.commands.CommandLeave
import dev.h4kt.pivosound.kordexExtensions.commands.CommandMove
import dev.h4kt.pivosound.kordexExtensions.commands.CommandPause
import dev.h4kt.pivosound.kordexExtensions.commands.CommandPlay
import dev.h4kt.pivosound.kordexExtensions.commands.CommandQueue
import dev.h4kt.pivosound.kordexExtensions.commands.CommandRepeat
import dev.h4kt.pivosound.kordexExtensions.commands.CommandResume
import dev.h4kt.pivosound.kordexExtensions.commands.CommandSeek
import dev.h4kt.pivosound.kordexExtensions.commands.CommandSkip
import dev.h4kt.pivosound.services.ServicesModule
import dev.h4kt.pivosound.services.query.LavaplayerQueryService
import dev.kordex.core.ExtensibleBot
import dev.kordex.core.utils.loadModule
import dev.lavalink.youtube.YoutubeAudioSourceManager
import dev.lavalink.youtube.clients.WebEmbeddedWithThumbnail
import org.koin.ksp.generated.module

suspend fun main() {

    val discordConfig: DiscordConfig = SerializableDiscordConfig.load()
    val sourcesConfig: SourcesConfig = SerializableSourcesConfig.load()

    val bot = ExtensibleBot(discordConfig.token) {

        hooks {
            beforeKoinSetup {

                loadModule(createdAtStart = true) {

                    includes(
                        ConfigModule().module,
                        ServicesModule().module
                    )

                    val lavaplayer: AudioPlayerManager = DefaultAudioPlayerManager()
                        .apply {

                            registerSourceManager(
                                SoundCloudAudioSourceManager.builder()
                                    .withAllowSearch(true)
                                    .build()
                            )

                            registerSourceManager(
                                YoutubeAudioSourceManager(
                                    /* allowSearch = */true,
                                    /* ...clients = */*arrayOf(
                                        WebEmbeddedWithThumbnail()
                                    )
                                    /* email = *//*sourcesConfig.youtube.username,
                                    *//* password = *//*sourcesConfig.youtube.password*/
                                )
                            )

                        }

                    single<AudioPlayerManager> { lavaplayer }
                    single { LavaplayerQueryService() }

                }

            }
        }

        devMode = true

        extensions {

            add(::VoiceStateWatcher)

            add(::CommandJoin)
            add(::CommandLeave)
            add(::CommandPlay)
            add(::CommandPause)
            add(::CommandResume)
            add(::CommandSeek)
            add(::CommandMove)
            add(::CommandSkip)
            add(::CommandQueue)
            add(::CommandRepeat)

        }

    }

    bot.start()

}
