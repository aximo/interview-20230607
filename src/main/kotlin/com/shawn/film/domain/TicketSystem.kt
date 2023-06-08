package com.shawn.film.domain

import com.shawn.film.domain.model.Hall
import com.shawn.film.domain.model.PlayPlan
import com.shawn.film.domain.model.Seat
import com.shawn.film.domain.model.Ticket
import java.time.LocalDateTime

interface TicketSystem {
    fun sell(hall: Hall, plan: PlayPlan, bookTime: LocalDateTime, seats: List<Seat>): List<Ticket>
}