package dev.h4kt.pivosound.services.query

import dev.h4kt.pivosound.services.query.results.LookupResult

interface QueryService {

    suspend fun lookup(
        query: String
    ): LookupResult

}
