package dev.h4kt.pivosound.services.query

import dev.h4kt.pivosound.services.query.results.LookupResult
import dev.h4kt.pivosound.types.AudioSource
import dev.kord.common.annotation.KordVoice

interface QueryService {

    suspend fun lookup(
        source: AudioSource,
        term: String
    ): LookupResult

}
