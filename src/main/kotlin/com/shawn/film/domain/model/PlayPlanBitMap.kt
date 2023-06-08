package com.shawn.film.domain.model

/**
 * for one play plan, if one seat is selled, it should be marked as true
 */
class PlayPlanBitMap(val plan: PlayPlan) {
    private val status: MutableMap<Int, Boolean> = mutableMapOf()
    fun lock(seat: Seat) {
        this.status[seat.num] = true
    }

    fun unlock(seat: Seat) {
        this.status[seat.num] = false
    }

    fun hasLocked(seat: Seat): Boolean {
        return this.status[seat.num] ?: false
    }
}