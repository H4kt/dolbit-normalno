package io.h4kt.pivosound.extensions

import kotlin.time.Duration

//fun Duration.formatElapsed(): String {
//
//    toComponents { days, hours, minutes, seconds, nanoseconds ->
//
//        var result = ""
//        result.appendIfAboveZero(days, ":")
//        result.appendIfAboveZero(hours, ":")
//        result.appendIfAboveZero(minutes, ":")
//        result += "$seconds"
//
//    }
//
//}

//private fun <T> String.appendIfAboveZero(
//    number: T,
//    suffix: String = ""
//) where T : Number, T : Comparable<T> = if (number > 0) {
//    "$this$number$suffix"
//} else {
//    this
//}