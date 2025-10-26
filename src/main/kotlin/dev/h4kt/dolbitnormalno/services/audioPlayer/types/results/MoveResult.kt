package dev.h4kt.dolbitnormalno.services.audioPlayer.types.results

import dev.h4kt.dolbitnormalno.types.PlayableMedia

sealed class MoveResult {

    data class InvalidPosition(
        val validRange: IntRange
    ) : MoveResult()

    data class Success(
        val track: PlayableMedia.Track
    ) : MoveResult()

}
