package io.h4kt.pivosound.extensions

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo

val AudioTrackInfo.hyperlink: String
    get() = "[$title]($uri)"