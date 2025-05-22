package dev.h4kt.pivosound.kordexExtensions.commands

import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.successEmbed
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import dev.kord.common.annotation.KordVoice
import dev.kord.core.behavior.channel.connect
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import org.koin.core.component.inject

class CommandJoin : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()

    override val name = "command:join"

    @OptIn(KordVoice::class)
    override suspend fun setup() {
        publicSlashCommand {

            name = Translations.Commands.Join.name
            description = Translations.Commands.Join.description

            action {

                val guild = guild
                    ?: return@action

                val member = user.fetchMemberOrNull(guild.id)
                    ?: return@action

                val voiceState = member.getVoiceState()

                val channel = voiceState.getChannelOrNull()
                    ?: run {
                        respond {
                            errorEmbed {
                                title = ":x: You are not in a voice channel"
                            }
                        }
                        return@action
                    }

                val player = audioPlayerService.createAudioPlayer(guild.id)

                channel.connect {
                    audioProvider = player
                    selfDeaf = true
                }

                respond {
                    successEmbed {
                        title = ":white_check_mark: Joined voice channel ${channel.asChannel().name}"
                    }
                }

            }
        }
    }

}
