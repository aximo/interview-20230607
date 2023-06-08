package com.shawn.film.infrastructure

import com.shawn.film.domain.HallSeatMaintainStrategy
import com.shawn.film.domain.HallSystem
import com.shawn.film.domain.model.Hall
import com.shawn.film.domain.model.PlayPlan
import com.shawn.film.domain.model.Seat

class LocalHallSystem(private val hallSeatMaintainStrategy: HallSeatMaintainStrategy) : HallSystem {
    override fun book(hall: Hall, plan: PlayPlan, seats: List<Seat>) {
        hallSeatMaintainStrategy.lock(hall, plan, seats)
    }
}