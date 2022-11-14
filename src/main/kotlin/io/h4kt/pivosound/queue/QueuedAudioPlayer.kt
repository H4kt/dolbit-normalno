package io.h4kt.pivosound.queue

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import java.util.*

class QueuedAudioPlayer(
    private val handle: AudioPlayer
) {

    var repeatMode = RepeatMode.NONE

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
                    player.playTrack(track)
                    return
                }

                val next = queue.pop() ?: return
                player.playTrack(next)

                if (repeatMode == RepeatMode.QUEUE) {
                    queue.add(next)
                }

            }

        })

    }

    fun provide() = handle.provide()

    fun destroy() {
        queue.clear()
        handle.destroy()
    }

    fun enqueue(track: AudioTrack) {

        if (handle.playingTrack == null) {
            handle.playTrack(track)
        } else {
            queue += track
        }

    }

}