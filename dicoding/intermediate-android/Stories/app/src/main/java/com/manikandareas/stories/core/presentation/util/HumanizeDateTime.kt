package com.manikandareas.stories.core.presentation.util

import java.time.Duration
import java.time.ZonedDateTime

fun ZonedDateTime.toHumanizedTime(): String {
    val now = ZonedDateTime.now(this.zone)
    val duration = Duration.between(this, now)

    return when {
        duration.seconds < 60 -> "Just now"
        duration.toMinutes() < 60 -> "${duration.toMinutes()} minutes ago"
        duration.toHours() < 24 -> "${duration.toHours()} hours ago"
        duration.toDays() < 7 -> "${duration.toDays()} days ago"
        duration.toDays() < 30 -> "${duration.toDays() / 7} weeks ago"
        duration.toDays() < 365 -> "${duration.toDays() / 30} months ago"
        else -> "${duration.toDays() / 365} years ago"
    }
}