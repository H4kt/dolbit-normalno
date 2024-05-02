package dev.h4kt.pivosound.services.audioPlayer.types

import dev.h4kt.pivosound.services.audioSource.AudioProvider
import dev.h4kt.pivosound.types.PlayableMedia
import kotlin.time.Duration

class PlayingTrack(
    val info: PlayableMedia.Track,
    val audioProvider: AudioProvider
) {

    val position: Duration
        get() = audioProvider.position

}
