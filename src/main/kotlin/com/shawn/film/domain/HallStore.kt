package com.shawn.film.domain

import com.shawn.film.domain.model.Hall
import java.util.*

interface HallStore {
    fun save(hall: Hall)
    fun load(hallId: String): Optional<Hall>
}