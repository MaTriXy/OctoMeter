/*
 * Copyright (c) 2024. Ryan Wong
 * https://github.com/ryanw-mobile
 * Sponsored by RW MobiMedia UK Limited
 *
 */
package com.rwmobi.kunigami.domain.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun Instant.formatInstantWithoutSeconds(): String {
    val formatted = toString()
    // This assumes the format is always compliant with ISO-8601 extended format.
    return formatted.substring(0, formatted.lastIndexOf(':')) + "Z"
}

fun Instant.roundDownToHour(): Instant {
    val currentZone = TimeZone.currentSystemDefault()
    val currentLocalDateTime = toLocalDateTime(timeZone = currentZone)
    return LocalDateTime(
        year = currentLocalDateTime.year,
        month = currentLocalDateTime.month,
        dayOfMonth = currentLocalDateTime.dayOfMonth,
        hour = currentLocalDateTime.hour,
        minute = 0,
        second = 0,
        nanosecond = 0,
    ).toInstant(timeZone = currentZone)
}

fun Instant.roundDownToDay(): Instant {
    val currentZone = TimeZone.currentSystemDefault()
    val currentLocalDateTime = toLocalDateTime(timeZone = currentZone)
    return LocalDateTime(
        year = currentLocalDateTime.year,
        month = currentLocalDateTime.month,
        dayOfMonth = currentLocalDateTime.dayOfMonth,
        hour = 0,
        minute = 0,
        second = 0,
        nanosecond = 0,
    ).toInstant(timeZone = currentZone)
}
