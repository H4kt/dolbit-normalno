package dev.h4kt.dolbitnormalno.services.query

import dev.h4kt.dolbitnormalno.services.query.results.LookupResult

interface QueryService {

    suspend fun lookup(
        query: String
    ): LookupResult

}
