package com.shawn.film.domain

import com.shawn.film.domain.model.Hall
import com.shawn.film.domain.model.PlayPlan
import com.shawn.film.domain.model.Seat

interface HallSeatMaintainStrategy {
    fun lock(hall: Hall, playPlan: PlayPlan, seats: List<Seat>)
    fun unlock(hall: Hall, playPlan: PlayPlan, seats: List<Seat>)
}