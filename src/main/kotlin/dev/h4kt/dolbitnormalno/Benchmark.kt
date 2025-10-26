package dev.h4kt.dolbitnormalno

import dev.h4kt.dolbitnormalno.services.query.QueryService
import dev.h4kt.dolbitnormalno.services.query.ytdlp.YtdlpQueryService
import dev.h4kt.dolbitnormalno.services.query.ytsearchpython.YtSearchPythonQueryService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.time.Duration
import kotlin.time.measureTime

suspend fun main() = coroutineScope {

    val runs = 10
    val durations = mutableMapOf<QueryService, Duration>()

    val services = listOf(YtdlpQueryService(), YtSearchPythonQueryService())

    val query = "nothing,nowhere. ornament"

    val jobs = services.map { service ->
        async {
            repeat(runs) {
                val duration = measureTime {
                    service.lookup(query)
                }

                durations[service] = (durations[service] ?: Duration.ZERO) + duration
            }
        }
    }

    jobs.awaitAll()

    durations.forEach { (service, duration) ->
        println("${service.javaClass.simpleName} has average lookup duration of ${duration / runs}")
    }

}
