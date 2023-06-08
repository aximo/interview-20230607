package com.shawn.film.infrastructure

import com.shawn.film.domain.HallSeatMaintainStrategy
import com.shawn.film.domain.model.Hall
import com.shawn.film.domain.model.PlayPlan
import com.shawn.film.domain.model.PlayPlanBitMap
import com.shawn.film.domain.model.Seat
import com.shawn.film.domain.support.HallBizException
import com.shawn.film.domain.support.HallErrorCode
import java.time.ZoneId

class LocalHallSeatMaintainStrategy : HallSeatMaintainStrategy {
    private val bitmaps = mutableMapOf<String, PlayPlanBitMap>()
    override fun lock(hall: Hall, playPlan: PlayPlan, seats: List<Seat>) {
        val bitmap = initCacheIfNecessary(hall, playPlan)
        seats.forEach {
            if (bitmap.hasLocked(it)) {
                throw HallBizException(hall, HallErrorCode.SEAT_ALREADY_BOOKED)
            }
            bitmap.lock(it)
        }
    }

    override fun unlock(hall: Hall, playPlan: PlayPlan, seats: List<Seat>) {
        val bitmap = initCacheIfNecessary(hall, playPlan)
        seats.forEach {
            bitmap.unlock(it)
        }
    }

    private fun initCacheIfNecessary(hall: Hall, playPlan: PlayPlan): PlayPlanBitMap {
        val key = buildCacheKey(hall, playPlan)
        return if (bitmaps.containsKey(key)) {
            requireNotNull(bitmaps[key])
        } else {
            PlayPlanBitMap(playPlan).also { bitmaps[key] = it }
        }
    }

    private fun buildCacheKey(hall: Hall, playPlan: PlayPlan): String {
        return hall.id + "#" + playPlan.startTime.atZone(ZoneId.systemDefault()).toEpochSecond()
    }
}