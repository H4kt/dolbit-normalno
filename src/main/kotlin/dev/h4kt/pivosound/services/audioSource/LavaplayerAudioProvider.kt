package dev.h4kt.pivosound.services.audioSource

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.h4kt.pivosound.types.PlayableMedia
import dev.kord.common.annotation.KordVoice
import dev.kord.voice.AudioFrame
import dev.lavalink.youtube.YoutubeAudioSourceManager
import dev.lavalink.youtube.clients.WebEmbeddedWithThumbnail
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(KordVoice::class)
class LavaplayerAudioProvider(
    override val track: PlayableMedia.Track
) : AudioProvider {

    private val lavaplayer = DefaultAudioPlayerManager()
        .apply {
            registerSourceManager(
                YoutubeAudioSourceManager(
                    /* allowSearch = */false,
                    /* ...clients = */*arrayOf(
                        WebEmbeddedWithThumbnail()
                    )
                )
            )
        }

    val handle = lavaplayer.createPlayer()

    override val position: Duration
        get() = handle.playingTrack?.position?.milliseconds ?: Duration.ZERO

    init {
        lavaplayer.loadItem(track.id, object : AudioLoadResultHandler {

            override fun trackLoaded(track: AudioTrack) {
                handle.playTrack(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                handle.playTrack(playlist.tracks.firstOrNull())
            }

            override fun noMatches() {
                handle.stopTrack()
            }

            override fun loadFailed(exception: FriendlyException) {
                handle.stopTrack()
            }
        })
    }

    override fun pause() {
        handle.isPaused = true
    }

    override fun resume() {
        handle.isPaused = false
    }

    override fun seek(duration: Duration) {
        handle.playingTrack.position = duration.inWholeMilliseconds
    }

    override fun close() {
        handle.destroy()
    }

    override fun onEnd(listener: () -> Unit) {
        handle.addListener {
            if (it is TrackEndEvent) {
                listener()
            }
        }
    }

    override suspend fun provide(): AudioFrame? {
        return handle.provide()?.data?.let(AudioFrame::fromData)
    }

}
