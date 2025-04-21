package org.neopool.project.core.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Formats a timestamp (in seconds) to a human-readable string. Used UTC timezone.
 *
 * @param timestamp The timestamp in seconds to format.
 * @return A string representing the formatted date and time.
 */
fun formatTimestamp(timestamp: Long): String {
    val instant = Instant.fromEpochSeconds(timestamp)
    val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
    return "${localDateTime.date} ${localDateTime.time}"
}