package dev.h4kt.pivosound.kordexExtensions.commands

import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.successEmbed
import dev.h4kt.pivosound.extensions.tryRegisterToTestGuild
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import dev.h4kt.pivosound.services.connectionManager.ConnectionManager
import dev.h4kt.pivosound.services.query.LavaplayerQueryService
import dev.h4kt.pivosound.services.query.results.LookupResult
import dev.h4kt.pivosound.types.AudioSource
import dev.h4kt.pivosound.types.PlayableMedia
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.channel.connect
import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.converters.impl.string
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import org.koin.core.component.inject

class CommandPlay : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()
    private val connectionManager by inject<ConnectionManager>()
    private val lavaplayerQueryService by inject<LavaplayerQueryService>()

    override val name = "command:play"

    class PlayCommandArguments : Arguments() {

//        val source by enumChoice<AudioSource> {
//
//            name = Translations.Commands.Play.Args.Source.name
//            typeName = Translations.Commands.Play.Args.Source.typeName
//            description = Translations.Commands.Play.Args.Source.description
//
//            choices(AudioSource.choices())
//        }

        val query by string {
            name = Translations.Commands.Play.Args.Query.name
            description = Translations.Commands.Play.Args.Query.description
        }

    }

    @OptIn(KordVoice::class)
    override suspend fun setup() {
        publicSlashCommand(::PlayCommandArguments) {

            name = Translations.Commands.Play.name
            description = Translations.Commands.Play.description

            tryRegisterToTestGuild()

            action {

                val guild = guild
                    ?: return@action

//                val audioSource = when (arguments.source) {
//                    AudioSource.YOUTUBE,
//                    AudioSource.SOUNDCLOUD -> lavaplayerQueryService
//                }

                val result = lavaplayerQueryService.lookup(
                    source = AudioSource.YOUTUBE,
                    term = arguments.query
                )

                val (media) = when (result) {
                    LookupResult.Error -> {
                        respond {
                            errorEmbed {
                                title = ":x: An error occurred while searching for song"
                                description = "Please try again in a moment"
                            }
                        }
                        return@action
                    }
                    LookupResult.NoResults -> {
                        respond {
                            errorEmbed {
                                title = ":x: No results found"
                                description = "Maybe you should be more specific?"
                            }
                        }
                        return@action
                    }
                    is LookupResult.Success -> result
                }

                var player = audioPlayerService.getAudioPlayer(guild.id)
                if (player == null) {

                    val channel = user.asMemberOrNull(guild.id)
                        ?.getVoiceStateOrNull()
                        ?.getChannelOrNull()
                        ?: run {
                            respond {
                                errorEmbed {
                                    title = ":x: Not in a voice channel"
                                    description = "Perhaps you can join one"
                                }
                            }
                            return@action
                        }

                    player = audioPlayerService.createAudioPlayer(guild.id)

                    val connection = channel.connect {
                        audioProvider = player
                        selfDeaf = true
                    }

                    connectionManager.registerConnection(
                        guildId = guild.id,
                        connection = connection
                    )

                }

                if (player.queue.isEmpty()) {

                    respond {
                        successEmbed {
                            title = ":white_check_mark: Now playing"
                            description = "[${media.title} (${media.duration})](${media.url})"
                        }
                    }

                } else {

                    respond {
                        successEmbed {
                            title = ":white_check_mark: Added to queue"
                            description = "[${media.title} (${media.duration})](${media.url})"
                        }
                    }

                }

                when (media) {
                    is PlayableMedia.Playlist -> media.tracks.forEach(player::queue)
                    is PlayableMedia.Track -> player.queue(media)
                }

            }
        }
    }

}
