package org.example.project.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun formatTime(iso: String): String {

    val localDateTime = LocalDateTime.parse(iso)

    return localDateTime.toCustomString()

}

fun currentTime(): String {
    val now = Clock.System.now()

    val localDateTime = now.toLocalDateTime(TimeZone.of("Europe/Moscow"))

    return localDateTime.toCustomString()
}

fun LocalDateTime.toCustomString(): String {
    val customFormat = LocalDateTime.Format {
        day(); char('.'); monthNumber(); char('.'); yearTwoDigits(1970)
        char(' '); hour(); char(':'); minute()
    }

    val result = this.format(customFormat)
    return result
}