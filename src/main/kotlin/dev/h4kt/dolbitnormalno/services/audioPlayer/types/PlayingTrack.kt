package dev.h4kt.dolbitnormalno.services.audioPlayer.types

import dev.h4kt.dolbitnormalno.services.audioSource.AudioProvider
import dev.h4kt.dolbitnormalno.types.PlayableMedia
import kotlin.time.Duration

class PlayingTrack(
    val track: PlayableMedia.Track,
    val audioProvider: AudioProvider
) {

    val position: Duration
        get() = audioProvider.position

}
