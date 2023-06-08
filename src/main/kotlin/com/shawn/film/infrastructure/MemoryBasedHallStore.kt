package com.shawn.film.infrastructure

import com.shawn.film.domain.HallStore
import com.shawn.film.domain.model.Hall
import java.util.*

class MemoryBasedHallStore : HallStore {
    private val items = mutableMapOf<String, Hall>()
    override fun save(hall: Hall) {
        items[hall.id] = hall
    }

    override fun load(hallId: String): Optional<Hall> {
        return Optional.ofNullable(items[hallId])
    }
}