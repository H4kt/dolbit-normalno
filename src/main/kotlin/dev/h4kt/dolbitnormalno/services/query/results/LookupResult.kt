package dev.h4kt.dolbitnormalno.services.query.results

import dev.h4kt.dolbitnormalno.types.PlayableMedia

sealed class LookupResult {

    data object Error : LookupResult()
    data object NoResults : LookupResult()

    data class Success(
        val media: PlayableMedia
    ) : LookupResult()

}
