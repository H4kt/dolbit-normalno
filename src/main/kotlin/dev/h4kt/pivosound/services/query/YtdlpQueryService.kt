package dev.h4kt.pivosound.services.query

import dev.h4kt.pivosound.services.query.results.LookupResult
import dev.h4kt.pivosound.types.PlayableMedia
import org.koin.core.annotation.Single
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Single
class YtdlpQueryService : QueryService {

    companion object {

        private const val EXTRACTOR_VIDEO = "youtube"
        private const val EXTRACTOR_PLAYLIST = "youtube:playlist"

        private const val SEPARATOR = "{separator}"
        private const val OUTPUT_FORMAT = "%(id)s$SEPARATOR%(title)s$SEPARATOR%(channel)s$SEPARATOR%(thumbnail)s$SEPARATOR%(duration_string)s$SEPARATOR%(extractor)s"

        private val urlRegex = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()!@:%_+.~#?&/=]*)".toRegex()

    }

    override suspend fun lookup(
        query: String
    ): LookupResult = suspendCoroutine { continuation ->

        val queryArgument = if (query.matches(urlRegex)) {
            query
        } else {
            "ytsearch1:$query"
        }

        val process = ProcessBuilder(
            "yt-dlp",
            "-s",
            queryArgument,
            "--print",
            OUTPUT_FORMAT,
            "--quiet",
            "--no-warnings"
        ).start()

        val outputLines = process.inputStream
            .reader()
            .readLines()

        val firstLine = outputLines.firstOrNull()
        if (firstLine == null) {
            continuation.resume(LookupResult.NoResults)
            return@suspendCoroutine
        }

        val media = parseOutputLine(firstLine)
        continuation.resume(LookupResult.Success(media))

    }

    private fun parseOutputLine(
        line: String
    ): PlayableMedia {

        val params = line.split(SEPARATOR)

        val (
            id,
            title,
            channel,
            thumbnailUrl,
            duration
        ) = params

        val extractor = params[5]

        return when (extractor) {
            EXTRACTOR_VIDEO -> {
                PlayableMedia.Track(
                    id = id,
                    title = title,
                    author = channel,
                    url = "https://youtube.com/watch?v=$id",
                    thumbnailUrl = thumbnailUrl,
                )
            }
            EXTRACTOR_PLAYLIST -> {
                PlayableMedia.Playlist(
                    id = id,
                    title = title,
                    author = channel,
                    url = "https://youtube.com/watch?v=$id",
                    thumbnailUrl = thumbnailUrl,
                    tracks = fetchPlaylistTracks(id)
                )
            }
            else -> error("Invalid extractor type: $extractor")
        }
    }

    private fun fetchPlaylistTracks(
        playlistId: String
    ): List<PlayableMedia.Track> {

        val process = ProcessBuilder(
            "yt-dlp",
            "-s",
            "https://www.youtube.com/playlist?list=$playlistId",
            "--print",
            OUTPUT_FORMAT,
            "--quiet",
            "--no-warnings"
        ).start()

        val outputLines = process.inputStream
            .reader()
            .readLines()

        return outputLines.map(::parseOutputLine)
            .filterIsInstance<PlayableMedia.Track>()
    }

}
