package dev.h4kt.pivosound.services.query.results

import dev.h4kt.pivosound.types.PlayableMedia

sealed class LookupResult {

    data object Error : LookupResult()
    data object NoResults : LookupResult()

    data class Success(
        val media: PlayableMedia
    ) : LookupResult()

}
