package com.shawn.film.infrastructure

import com.shawn.film.domain.TicketSystem
import com.shawn.film.domain.model.Hall
import com.shawn.film.domain.model.PlayPlan
import com.shawn.film.domain.model.Seat
import com.shawn.film.domain.model.Ticket
import java.time.LocalDateTime

class LocalTicketSystem : TicketSystem {

    override fun sell(hall: Hall, plan: PlayPlan, bookTime: LocalDateTime, seats: List<Seat>): List<Ticket> {
        return seats.map {
            Ticket(movie = plan.movie,
                    hallId = hall.id,
                    startTime = plan.startTime,
                    bookTime = bookTime,
                    seat = it)
        }
    }
}