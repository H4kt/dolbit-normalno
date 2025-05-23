package dev.h4kt.pivosound.services.query

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import dev.h4kt.pivosound.config.sources.SourcesConfig
import dev.h4kt.pivosound.services.query.results.LookupResult
import dev.h4kt.pivosound.types.PlayableMedia
import java.io.File
import java.io.IOException
import java.security.GeneralSecurityException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class YouTubeApiQueryService(
    private val sourcesConfig: SourcesConfig
) : QueryService {

    companion object {
        private const val ID_KIND_VIDEO = "youtube#video"
        private const val ID_KIND_PLAYLIST = "youtube#playlist"
    }

    private val scopes = listOf("https://www.googleapis.com/auth/youtube.force-ssl")
    private val jsonFactory = JacksonFactory.getDefaultInstance()

    override suspend fun lookup(
        query: String
    ): LookupResult = suspendCoroutine { continuation ->

        val service = getService()

        val response = try {
            service.search()
                .list("snippet")
                .setQ(query)
                .setMaxResults(20)
                .execute()
        } catch (_: Exception) {
            continuation.resume(LookupResult.Error)
            return@suspendCoroutine
        }

        val firstResult = response.items.firstOrNull()
        if (firstResult == null) {
            continuation.resume(LookupResult.NoResults)
            return@suspendCoroutine
        }

        val media: PlayableMedia = when (firstResult.id.kind) {
            ID_KIND_PLAYLIST -> {
                PlayableMedia.Playlist(
                    id = firstResult.id.playlistId,
                    title = firstResult.snippet.title,
                    author = firstResult.snippet.channelTitle,
                    url = "https://youtube.com/playlist?list=${firstResult.id.playlistId}",
                    thumbnailUrl = firstResult.snippet.thumbnails.default.url,
                    tracks = try {
                        fetchPlaylistTracks(firstResult.id.playlistId)
                    } catch (_: Exception) {
                        continuation.resume(LookupResult.Error)
                        return@suspendCoroutine
                    },
                )
            }
            ID_KIND_VIDEO -> {
                PlayableMedia.Track(
                    id = firstResult.id.videoId,
                    title = firstResult.snippet.title,
                    author = firstResult.snippet.channelTitle,
                    url = "https://youtube.com/watch?v=${firstResult.id.videoId}",
                    thumbnailUrl = firstResult.snippet.thumbnails.default.url
                )
            }
            else -> {
                continuation.resume(LookupResult.NoResults)
                return@suspendCoroutine
            }
        }

        continuation.resume(LookupResult.Success(media))
    }

    private fun fetchPlaylistTracks(
        playlistId: String
    ): List<PlayableMedia.Track> {

        val tracks = mutableListOf<PlayableMedia.Track>()

        val service = getService()

        var nextPageToken: String? = null
        while (true) {

            val response = service.playlistItems()
                .list("snippet")
                .setPlaylistId(playlistId)
                .setMaxResults(50)
                .setPageToken(nextPageToken)
                .execute()

            tracks += response.items
                .filter { it.snippet.resourceId.kind == ID_KIND_VIDEO }
                .mapIndexed { index, item ->
                    PlayableMedia.Track(
                        id = item.snippet.resourceId.videoId,
                        title = item.snippet.title,
                        author = item.snippet.channelTitle,
                        url = "https://youtube.com/watch?v=${item.snippet.resourceId.videoId}&list=$playlistId&index=${index.inc()}",
                        thumbnailUrl = item.snippet.thumbnails.default.url
                    )
                }

            nextPageToken = response.nextPageToken

            if (nextPageToken == null) {
                break
            }

        }

        return tracks
    }

    @Throws(IOException::class)
    private fun authorize(httpTransport: NetHttpTransport): Credential {

        val clientSecretsStream = File(sourcesConfig.youtube.clientSecretsFile)
            .inputStream()

        val clientSecrets = clientSecretsStream.use {
            GoogleClientSecrets.load(jsonFactory, it.reader())
        }

        val flow = GoogleAuthorizationCodeFlow.Builder(
            httpTransport,
            jsonFactory,
            clientSecrets,
            scopes
        ).build()

        val credential = AuthorizationCodeInstalledApp(flow,LocalServerReceiver())
            .authorize("user")

        return credential
    }

    @Throws(GeneralSecurityException::class, IOException::class)
    private fun getService(): YouTube {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()
        val credential = authorize(httpTransport)
        return YouTube.Builder(httpTransport, jsonFactory, credential)
            .setApplicationName(sourcesConfig.youtube.applicationName)
            .build()
    }

}
