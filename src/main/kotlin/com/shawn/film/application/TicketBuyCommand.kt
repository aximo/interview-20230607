package com.shawn.film.application

import com.shawn.film.domain.model.Seat
import java.time.LocalDateTime
import java.time.LocalTime

data class TicketBuyCommand(val movieName: String, val expectTime: LocalDateTime, val hallId: String, val seats: List<Seat>)
