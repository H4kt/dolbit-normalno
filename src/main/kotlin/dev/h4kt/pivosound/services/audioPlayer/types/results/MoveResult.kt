package dev.h4kt.pivosound.services.audioPlayer.types.results

import dev.h4kt.pivosound.types.PlayableMedia

sealed class MoveResult {

    data class InvalidPosition(
        val validRange: IntRange
    ) : MoveResult()

    data class Success(
        val track: PlayableMedia.Track
    ) : MoveResult()

}
