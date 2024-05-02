package dev.h4kt.pivosound.services.audioSource

import dev.h4kt.pivosound.types.PlayableMedia
import dev.kord.common.annotation.KordVoice
import kotlin.time.Duration
import dev.kord.voice.AudioProvider as KordAudioProvider

@OptIn(KordVoice::class)
interface AudioProvider : KordAudioProvider {

    val track: PlayableMedia.Track
    val position: Duration

    fun pause()
    fun resume()

    fun seek(duration: Duration)

    fun close()

    fun onEnd(listener: () -> Unit)

}
