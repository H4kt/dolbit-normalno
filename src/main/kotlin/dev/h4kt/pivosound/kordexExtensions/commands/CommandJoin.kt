package dev.h4kt.pivosound.kordexExtensions.commands

import dev.h4kt.pivosound.extensions.errorEmbed
import dev.h4kt.pivosound.extensions.successEmbed
import dev.h4kt.pivosound.extensions.tryRegisterToTestGuild
import dev.h4kt.pivosound.generated.i18n.Translations
import dev.h4kt.pivosound.services.audioPlayer.AudioPlayerService
import dev.h4kt.pivosound.services.connectionManager.ConnectionManager
import dev.kord.common.annotation.KordVoice
import dev.kord.common.entity.Permission
import dev.kord.core.behavior.channel.connect
import dev.kord.core.entity.channel.VoiceChannel
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import org.koin.core.component.inject

class CommandJoin : Extension() {

    private val audioPlayerService by inject<AudioPlayerService>()
    private val connectionManager by inject<ConnectionManager>()

    override val name = "command:join"

    @OptIn(KordVoice::class)
    override suspend fun setup() {
        publicSlashCommand {

            name = Translations.Commands.Join.name
            description = Translations.Commands.Join.description

            tryRegisterToTestGuild()

            val selfId = kord.selfId

            action {

                val guild = guild
                    ?: return@action

                val member = user.fetchMemberOrNull(guild.id)
                    ?: return@action

                val voiceState = member.getVoiceStateOrNull()

                val channel = voiceState?.getChannelOrNull() as? VoiceChannel
                if (channel == null) {
                    respond {
                        errorEmbed {
                            title = ":x: You are not in a voice channel"
                            description = "Or I don't have permission to see the channel you're in"
                        }
                    }
                    return@action
                }

                val permissions = channel.getEffectivePermissions(selfId)
                if (Permission.Connect !in permissions) {
                    respond {
                        errorEmbed {
                            title = ":x: Unable to join"
                            description = "I don't have permission to join your channel"
                        }
                    }
                    return@action
                }

                val player = audioPlayerService.createAudioPlayer(guild.id)

                val connection = channel.connect {
                    audioProvider = player
                    selfDeaf = true
                }

                connectionManager.registerConnection(
                    guildId = guild.id,
                    connection = connection
                )

                respond {
                    successEmbed {
                        title = ":white_check_mark: Joined voice channel ${channel.asChannel().name}"
                    }
                }

            }
        }
    }

}
