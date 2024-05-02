package dev.h4kt.pivosound.services.audioPlayer.types

import dev.h4kt.pivosound.services.audioPlayer.types.results.MoveResult
import dev.h4kt.pivosound.services.audioSource.LavaplayerAudioProvider
import dev.h4kt.pivosound.types.AudioSource
import dev.h4kt.pivosound.types.PlayableMedia
import dev.h4kt.pivosound.types.RepeatMode
import dev.kord.common.annotation.KordVoice
import dev.kord.voice.AudioFrame
import java.util.*
import kotlin.time.Duration
import dev.kord.voice.AudioProvider as KordAudioProvider

@OptIn(KordVoice::class)
class AudioPlayer : KordAudioProvider {

    private val _queue = LinkedList<PlayableMedia.Track>()

    val queue: List<PlayableMedia.Track>
        get() = _queue.toList()

    var currentTrack: PlayingTrack? = null
        private set

    var repeatMode: RepeatMode = RepeatMode.NONE

    override suspend fun provide(): AudioFrame? {
        return currentTrack?.audioProvider?.provide()
    }

    fun play(track: PlayableMedia.Track) {

        val audioProvider = when (track.source) {
            AudioSource.YOUTUBE,
            AudioSource.SOUNDCLOUD ->
                LavaplayerAudioProvider(track)
                    .apply { onEnd(::onTrackEnd) }
        }

        currentTrack = PlayingTrack(
            info = track,
            audioProvider = audioProvider
        )

    }

    fun queue(track: PlayableMedia.Track) {

        if (currentTrack == null) {
            play(track)
        } else {
            _queue += track
        }

    }

    fun pause() {
        currentTrack?.audioProvider?.pause()
    }

    fun resume() {
        currentTrack?.audioProvider?.resume()
    }

    fun move(
        from: Int,
        to: Int
    ): MoveResult {

        if (from !in _queue.indices) {
            return MoveResult.InvalidPosition(_queue.indices)
        }

        val newIndex = when {
            from < to -> to.dec()
            else -> to
        }

        val track = _queue.removeAt(from)
        _queue.add(newIndex, track)

        return MoveResult.Success(track)
    }

    fun skip() {
        onTrackEnd()
    }

    fun seek(duration: Duration) {
        currentTrack?.audioProvider?.seek(duration)
    }

    fun destroy() {
        currentTrack?.audioProvider?.close()
    }

    private fun onTrackEnd() {

        val currentTrack = currentTrack
        if (currentTrack != null) {

            if (repeatMode == RepeatMode.CURRENT_TRACK) {
                play(currentTrack.info)
                return
            }

            if (repeatMode == RepeatMode.QUEUE) {
                _queue.push(currentTrack.info)
            }

        }

        val next = _queue.poll()

        currentTrack?.audioProvider?.close()
        play(next)

    }

}
