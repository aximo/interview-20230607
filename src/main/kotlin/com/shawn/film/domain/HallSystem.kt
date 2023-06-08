package com.shawn.film.domain

import com.shawn.film.domain.model.Hall
import com.shawn.film.domain.model.PlayPlan
import com.shawn.film.domain.model.Seat

/**
 * maybe a third system
 */
interface HallSystem {
    fun book(hall: Hall, plan: PlayPlan, seats: List<Seat>)
}