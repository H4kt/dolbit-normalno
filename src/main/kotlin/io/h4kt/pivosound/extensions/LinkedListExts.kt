package io.h4kt.pivosound.extensions

import java.util.LinkedList

fun <E> LinkedList<E>.popOrNull(): E? {
    return if (isEmpty()) null else pop()
}