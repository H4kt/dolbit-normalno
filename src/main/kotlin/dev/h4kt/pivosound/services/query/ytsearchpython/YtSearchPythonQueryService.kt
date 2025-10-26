package dev.h4kt.pivosound.services.query.ytsearchpython

import dev.h4kt.pivosound.services.query.QueryService
import dev.h4kt.pivosound.services.query.results.LookupResult
import dev.h4kt.pivosound.services.query.ytsearchpython.types.YtSearchPythonOutput
import dev.h4kt.pivosound.types.PlayableMedia
import kotlinx.serialization.json.Json
import org.graalvm.polyglot.Source
import org.graalvm.python.embedding.GraalPyResources
import org.koin.core.annotation.Single
import org.slf4j.LoggerFactory
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.measureTime

@Single(createdAtStart = true)
class YtSearchPythonQueryService : QueryService {

    private val logger = LoggerFactory.getLogger(javaClass)

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val ctx = GraalPyResources.contextBuilder()
        .allowAllAccess(true)
        .build()

    private val searchYoutube by lazy {
        ctx.getBindings("python").getMember("search_youtube")
    }

    init {
        setup()
        warmup()
    }

    private fun setup() {
        val scriptStream = javaClass.classLoader.getResourceAsStream("python/youtube_meta.py")

        scriptStream.reader().use {
            ctx.eval(Source.newBuilder("python", it, "youtube_meta.py").build())
        }
    }

    private fun warmup() {
        logger.debug("Warming up GraalPython...")

        val duration = measureTime {
            try {
                searchYoutube.execute("logic warm it up", 1)
            } catch (_: Exception) {}
        }

        logger.debug("GraalPython warmup finished in $duration")
    }

    override suspend fun lookup(
        query: String
    ): LookupResult = suspendCoroutine { continuation ->
        continuation.resume(youtubeSearch(query))
    }

    private fun youtubeSearch(query: String): LookupResult {

        val rawResult = try {
            searchYoutube.execute(query, 1).asString()
        } catch (ex: Exception) {
            logger.error("Youtube search failed", ex)
            return LookupResult.Error
        }

        val result = try {
            println(rawResult)
            json.decodeFromString<List<YtSearchPythonOutput>>(rawResult).firstOrNull()
        } catch (ex: Exception) {
            logger.error("Failed to parse youtube_meta.py output", ex)
            return LookupResult.Error
        }

        if (result == null) {
            return LookupResult.NoResults
        }

        val media = when (result) {
            is Video -> result.toTrack()
            is Playlist -> result.toPlaylist()
        }

        return LookupResult.Success(media)
    }

    private fun YtSearchPythonOutput.Video.toTrack() = PlayableMedia.Track(
        id = id,
        title = title,
        author = channel,
        duration = duration.parseDuration(),
        url = "https://youtube.com/watch?v=${id}",
        thumbnailUrl = thumbnail
    )

    private fun YtSearchPythonOutput.Playlist.toPlaylist() = PlayableMedia.Playlist(
        id = id,
        title = title ?: "Unknown",
        author = channel ?: "Unknown",
        url = "https://youtube.com/playlist?list=${id}",
        thumbnailUrl = thumbnail,
        tracks = entries.map { it.toTrack() }
    )

    private fun String.parseDuration(): Duration {

        val units = listOf("s", "m", "h", "d", "w")
        val sections = split(":").asReversed()

        if (sections.size > units.size) {
            error("Unable to parse duration from $this")
        }

        val mapped = sections
            .mapIndexed { index, section ->
                "$section${units[index]}"
            }
            .asReversed()
            .joinToString("")

        return Duration.parse(mapped)
    }

}
