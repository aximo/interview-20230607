package com.shawn.film.domain.model

import java.time.LocalDateTime

/**
 * which movie will be played from [startTime] to [endTime]
 */
data class PlayPlan(val movie: String, val startTime: LocalDateTime, val endTime: LocalDateTime) {



    /**
     * the given time is in between start time and end time
     */
    fun matched(time: LocalDateTime): Boolean {
        return time in startTime..endTime
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayPlan

        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = startTime.hashCode()
        result = 31 * result + endTime.hashCode()
        return result
    }
}
