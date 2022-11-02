@file:OptIn(KordVoice::class)

package io.h4kt.pivosound.managers

import io.h4kt.pivosound.queue.QueuedAudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.kord.common.annotation.KordVoice
import dev.kord.voice.VoiceConnection
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private val PLAYER_BY_CONN = HashMap<VoiceConnection, QueuedAudioPlayer>()

private val playerManager = DefaultAudioPlayerManager().apply {
    AudioSourceManagers.registerRemoteSources(this)
}

val VoiceConnection.audioPlayer: QueuedAudioPlayer?
    get() = PLAYER_BY_CONN[this]

fun VoiceConnection.registerAudioPlayer(
    player: QueuedAudioPlayer
) {
    PLAYER_BY_CONN[this] = player
}

suspend fun findTrack(query: String) = suspendCoroutine {
    playerManager.loadItem(query, object : AudioLoadResultHandler {

        override fun trackLoaded(track: AudioTrack) {
            it.resume(track)
        }

        override fun playlistLoaded(playlist: AudioPlaylist) {
            it.resume(playlist.tracks.first())
        }

        override fun noMatches() {
            it.resume(null)
        }

        override fun loadFailed(exception: FriendlyException?) {
            it.resume(null)
        }

    })
}

fun createAudioPlayer() = QueuedAudioPlayer(playerManager.createPlayer())