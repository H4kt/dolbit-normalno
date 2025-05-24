package dev.h4kt.pivosound.services.query.ytdlp

import dev.h4kt.pivosound.services.query.QueryService
import dev.h4kt.pivosound.services.query.results.LookupResult
import dev.h4kt.pivosound.services.query.ytdlp.types.PlaylistMetadata
import dev.h4kt.pivosound.services.query.ytdlp.types.VideoMetadata
import dev.h4kt.pivosound.services.query.ytdlp.types.YtdlpOutput
import dev.h4kt.pivosound.types.PlayableMedia
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.koin.core.annotation.Single
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.seconds

@Single
class YtdlpQueryService : QueryService {

    private val urlRegex = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()!@:%_+.~#?&/=]*)".toRegex()

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun lookup(
        query: String
    ): LookupResult = suspendCoroutine { continuation ->

        val isExactUrl = query.matches(urlRegex)

        val queryArgument = if (isExactUrl) {
            query
        } else {
            "ytsearch1:$query"
        }

        val process = ProcessBuilder(
            "yt-dlp",
            "-s",
            queryArgument,
            "--dump-single-json",
            "--quiet",
            "--no-warnings"
        ).start()

        val result = try {
            json.decodeFromStream<YtdlpOutput>(process.inputStream)
        } catch (ex: Exception) {
            ex.printStackTrace()
            continuation.resume(LookupResult.Error)
            return@suspendCoroutine
        }

        val media = when (result) {
            is VideoMetadata -> {
                PlayableMedia.Track(
                    id = result.id,
                    title = result.title,
                    author = result.channel,
                    duration = result.duration.seconds,
                    url = "https://youtube.com/watch?v=${result.id}",
                    thumbnailUrl = result.thumbnailUrl
                )
            }
            is PlaylistMetadata -> {

                if (result.entries.isEmpty()) {
                    continuation.resume(LookupResult.NoResults)
                    return@suspendCoroutine
                }

                if (!isExactUrl) {

                    val item = result.entries.first()
                    if (item == null) {
                        continuation.resume(LookupResult.NoResults)
                        return@suspendCoroutine
                    }

                    PlayableMedia.Track(
                        id = item.id,
                        title = item.title,
                        author = item.channel,
                        duration = item.duration.seconds,
                        url = "https://youtube.com/watch?v=${item.id}",
                        thumbnailUrl = item.thumbnailUrl
                    )

                } else {

                    PlayableMedia.Playlist(
                        id = result.id,
                        title = result.title,
                        author = result.channel ?: "unknown",
                        url = "https://youtube.com/playlist?list=${result.id}",
                        thumbnailUrl = result.thumbnailUrl,
                        tracks = result.entries
                            .filterNotNull()
                            .mapIndexed { index, item ->
                                PlayableMedia.Track(
                                    id = item.id,
                                    title = item.title,
                                    author = item.channel,
                                    duration = item.duration.seconds,
                                    url = "https://youtube.com/watch?v=${item.id}&list=${result.id}&index=${index.inc()}",
                                    thumbnailUrl = item.thumbnailUrl
                                )
                            }
                    )

                }

            }
        }

        continuation.resume(LookupResult.Success(media))

    }

}
