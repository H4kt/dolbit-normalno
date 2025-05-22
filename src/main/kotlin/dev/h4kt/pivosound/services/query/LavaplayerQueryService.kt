package dev.h4kt.pivosound.services.query

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.h4kt.pivosound.services.query.results.LookupResult
import dev.h4kt.pivosound.types.AudioSource
import dev.h4kt.pivosound.types.PlayableMedia
import dev.kordex.core.koin.KordExKoinComponent
import dev.lavalink.youtube.YoutubeAudioSourceManager
import dev.lavalink.youtube.clients.MusicWithThumbnail
import org.koin.core.annotation.Single
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.milliseconds

@Single
class LavaplayerQueryService : QueryService, KordExKoinComponent {

    private val lavaplayer = DefaultAudioPlayerManager()
        .apply {

            registerSourceManager(
                SoundCloudAudioSourceManager.builder()
                    .withAllowSearch(true)
                    .build()
            )

            registerSourceManager(
                YoutubeAudioSourceManager(
                    /* allowSearch = */true,
                    /* ...clients = */*arrayOf(
                        MusicWithThumbnail()
                    )
                )
            )

        }

    override suspend fun lookup(
        source: AudioSource,
        term: String
    ): LookupResult = suspendCoroutine {

        val action = when (source) {
            AudioSource.YOUTUBE -> "ytmsearch:"
            AudioSource.SOUNDCLOUD -> "scsearch:"
        }

        lavaplayer.loadItem("$action$term", object : AudioLoadResultHandler {

            override fun trackLoaded(track: AudioTrack) {
                it.resume(LookupResult.Success(track.toPlayableMedia(source)))
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {

                val firstMatch = playlist.tracks.firstOrNull()

                val result = firstMatch
                    ?.let { track ->
                        LookupResult.Success(track.toPlayableMedia(source))
                    }
                    ?: LookupResult.NoResults

                it.resume(result)
            }

            override fun noMatches() {
                it.resume(LookupResult.NoResults)
            }

            override fun loadFailed(exception: FriendlyException) {
                it.resume(LookupResult.Error)
            }

        })
    }

    private fun AudioTrack.toPlayableMedia(
        source: AudioSource
    ): PlayableMedia.Track {
        return PlayableMedia.Track(
            id = identifier,
            title = info.title,
            author = info.author,
            duration = duration.milliseconds,
            url = info.uri,
            source = source
        )
    }

    private fun AudioPlaylist.toPlayableMedia(
        source: AudioSource
    ): PlayableMedia.Playlist {
        return PlayableMedia.Playlist(
            title = name,
            duration = tracks.fold(0L) { acc, it -> acc + it.duration }.milliseconds,
            tracks = tracks.map { it.toPlayableMedia(source) },
            url = null,
            source = source
        )
    }

}
