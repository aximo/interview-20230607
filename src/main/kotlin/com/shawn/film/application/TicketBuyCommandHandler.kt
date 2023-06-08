package com.shawn.film.application

import com.shawn.film.domain.HallStore
import com.shawn.film.domain.HallSystem
import com.shawn.film.domain.support.LockTemplate
import com.shawn.film.domain.TicketSystem
import com.shawn.film.domain.model.Hall
import com.shawn.film.domain.model.PlayPlan
import com.shawn.film.domain.model.Seat
import com.shawn.film.domain.model.Ticket
import com.shawn.film.domain.support.HallBizException
import com.shawn.film.domain.support.HallErrorCode
import com.shawn.film.domain.support.HallNotFoundException
import java.time.LocalDateTime

class TicketBuyCommandHandler(private val hallStore: HallStore,
                              private val hallSystem: HallSystem,
                              private val ticketSystem: TicketSystem,
                              private val lockTemplate: LockTemplate) {

    fun handle(command: TicketBuyCommand): CommandResult<List<Ticket>> {
        val hall = hallStore.load(command.hallId)
        if (hall.isEmpty) {
            throw HallNotFoundException(command.hallId)
        }

        if (hall.get().seats.size < command.seats.size) {
            throw HallBizException(hall.get(), HallErrorCode.HALL_SEAT_OVER_LIMIT)
        }

        val playPlan = hall.get().select(command.expectTime)
        if (playPlan.isEmpty) {
            throw HallBizException(hall.get(), HallErrorCode.HALL_PLAY_PLAN_NOT_FOUND)
        }

        if (playPlan.get().movie != command.movieName) {
            throw HallBizException(hall.get(), HallErrorCode.HALL_PLAY_PLAN_NOT_FOUND)
        }

        val locker = lockTemplate.tryLock()
        try {
            val tickets = doHandle(hall.get(), playPlan.get(), command.seats)
            return CommandResult.of(tickets)
        } catch (e: HallBizException) {
            throw e
        } catch (e: Exception) {
            throw HallBizException(hall.get(), HallErrorCode.BOOK_FAILED, e)
        } finally {
            locker.release()
        }
    }

    /**
     * this should be wrap in transaction template or use tcc or saga or outbox
     */
    private fun doHandle(hall: Hall, plan: PlayPlan, seats: List<Seat>): List<Ticket> {
        hallSystem.book(hall, plan, seats)
        return ticketSystem.sell(hall, plan, LocalDateTime.now(), seats)
    }
}