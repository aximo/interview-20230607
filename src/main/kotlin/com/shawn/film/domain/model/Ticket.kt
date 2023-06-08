package com.shawn.film.domain.model

import java.time.LocalDateTime

class Ticket(val movie:String,
             val hallId: String,
             val startTime: LocalDateTime,
             val bookTime: LocalDateTime,
             val seat: Seat)