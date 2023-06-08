package com.shawn.film.domain.model

import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class Hall(
        /**
         * the hall id
         */
        val id: String,

        /**
         * all the seats in this hall
         */
        val seats: List<Seat>
) {
    private val plans: MutableList<PlayPlan> = mutableListOf()

    fun add(plan: PlayPlan): Boolean {
        if (plans.contains(plan)) return false

        if (hasConflict(plan)) {
            throw RuntimeException("can not add the play plan as already existed one plan")
        }

        this.plans.add(plan)
        return true
    }

    private fun hasConflict(plan: PlayPlan): Boolean {
        return this.plans.any {
            it.matched(plan.startTime)
                    || it.matched(plan.endTime)
                    || (plan.startTime < it.startTime && plan.endTime > it.endTime)
        }
    }

    fun remove(plan: PlayPlan): Boolean {
        return this.plans.remove(plan)
    }

    fun getPlayPlans(): List<PlayPlan> {
        return plans
    }

    /**
     * select the play plan at some time
     */
    fun select(time: LocalDateTime): Optional<PlayPlan> {
        return Optional.ofNullable(this.plans.firstOrNull { it.matched(time) })
    }
}