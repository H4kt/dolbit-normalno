package io.h4kt.pivosound.queue

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import io.h4kt.pivosound.extensions.popOrNull
import java.util.*

class QueuedAudioPlayer(
    private val handle: AudioPlayer
) {

    var repeatMode = RepeatMode.NONE

    var isPaused: Boolean
        get() = handle.isPaused
        set(value) { handle.isPaused = value }

    var volume: Int
        get() = handle.volume
        set(value) { handle.volume = value }

    val currentTrack: AudioTrack?
        get() = handle.playingTrack

    val queue = LinkedList<AudioTrack>()

    init {

        handle.addListener(object : AudioEventAdapter() {

            override fun onTrackEnd(
                player: AudioPlayer,
                track: AudioTrack,
                endReason: AudioTrackEndReason
            ) {

                if (endReason != AudioTrackEndReason.FINISHED) {
                    return
                }

                if (repeatMode == RepeatMode.CURRENT_TRACK) {
                    player.playTrack(track.makeClone())
                } else {
                    nextTrack()
                }

            }

        })

    }

    fun provide() = handle.provide()

    fun enqueue(track: AudioTrack) {

        if (handle.playingTrack == null) {
            handle.playTrack(track)
        } else {
            queue += track
        }

    }

    fun play(track: AudioTrack) {
        handle.playTrack(track)
    }

    fun skip() {
        handle.stopTrack()
        nextTrack()
    }

    fun seek(time: Long) {
        currentTrack?.position = time
    }

    fun stop() = handle.stopTrack()

    fun destroy() {
        queue.clear()
        handle.destroy()
    }

    private fun nextTrack() {

        val next = queue.popOrNull() ?: return

        handle.playTrack(next)

        if (repeatMode == RepeatMode.QUEUE) {
            queue.add(next.makeClone())
        }

    }

}