package com.shawn.film.application

import com.shawn.film.domain.HallStore
import com.shawn.film.domain.HallSystem
import com.shawn.film.domain.TicketSystem
import com.shawn.film.domain.model.Hall
import com.shawn.film.domain.model.PlayPlan
import com.shawn.film.domain.model.Seat
import com.shawn.film.domain.support.HallBizException
import com.shawn.film.domain.support.HallErrorCode
import com.shawn.film.domain.support.HallNotFoundException
import com.shawn.film.domain.support.LockTemplate
import com.shawn.film.infrastructure.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.fail

internal class TicketBuyCommandHandlerTest {


    private lateinit var ticketBuyCommandHandler: TicketBuyCommandHandler


    @BeforeEach
    fun setup() {

        val hall001 = Hall(id = "h001", seats = (1..4).map { Seat(it) }).also {
            it.add(PlayPlan(movie = "movie_a", startTime = LocalDateTime.parse("2023-06-07T07:00:00"), endTime = LocalDateTime.parse("2023-06-07T08:50:00")))
            it.add(PlayPlan(movie = "movie_b", startTime = LocalDateTime.parse("2023-06-07T09:00:00"), endTime = LocalDateTime.parse("2023-06-07T10:50:00")))
            it.add(PlayPlan(movie = "movie_a", startTime = LocalDateTime.parse("2023-06-07T12:00:00"), endTime = LocalDateTime.parse("2023-06-07T13:50:00")))
            it.add(PlayPlan(movie = "movie_a", startTime = LocalDateTime.parse("2023-06-07T14:00:00"), endTime = LocalDateTime.parse("2023-06-07T14:50:00")))
        }

        val hall002 = Hall(id = "h002", seats = (1..10).map { Seat(it) }).also {
            it.add(PlayPlan(movie = "movie_a", startTime = LocalDateTime.parse("2023-06-07T07:00:00"), endTime = LocalDateTime.parse("2023-06-07T08:50:00")))
            it.add(PlayPlan(movie = "movie_b", startTime = LocalDateTime.parse("2023-06-07T09:00:00"), endTime = LocalDateTime.parse("2023-06-07T10:50:00")))
            it.add(PlayPlan(movie = "movie_a", startTime = LocalDateTime.parse("2023-06-07T12:00:00"), endTime = LocalDateTime.parse("2023-06-07T13:50:00")))
            it.add(PlayPlan(movie = "movie_a", startTime = LocalDateTime.parse("2023-06-07T14:00:00"), endTime = LocalDateTime.parse("2023-06-07T14:50:00")))
        }

        val hallStore: HallStore = MemoryBasedHallStore().also {
            it.save(hall001)
            it.save(hall002)
        }

        val hallSystem: HallSystem = LocalHallSystem(LocalHallSeatMaintainStrategy())
        val ticketSystem: TicketSystem = LocalTicketSystem()
        val lockTemplate: LockTemplate = LocalLockTemplate()
        this.ticketBuyCommandHandler = TicketBuyCommandHandler(hallStore = hallStore,
                hallSystem = hallSystem,
                ticketSystem = ticketSystem,
                lockTemplate = lockTemplate)
    }

    @Test
    fun `happy path`() {
        val tickets = ticketBuyCommandHandler.handle(
                TicketBuyCommand(
                        movieName = "movie_b", expectTime = LocalDateTime.parse("2023-06-07T10:00:00"),
                        hallId = "h001",
                        seats = listOf(Seat(2)))
        )
        assertEquals(1, tickets.data.size)
        assertEquals("movie_b", tickets.data.first().movie)
    }

    @Test
    fun `fail when repeat book`() {
        val tickets = ticketBuyCommandHandler.handle(
                TicketBuyCommand(
                        movieName = "movie_b", expectTime = LocalDateTime.parse("2023-06-07T10:00:00"),
                        hallId = "h001",
                        seats = listOf(Seat(2)))
        )
        assertEquals(1, tickets.data.size)
        assertEquals("movie_b", tickets.data.first().movie)

        try {
            ticketBuyCommandHandler.handle(
                    TicketBuyCommand(
                            movieName = "movie_b", expectTime = LocalDateTime.parse("2023-06-07T10:00:00"),
                            hallId = "h001",
                            seats = listOf(Seat(2)))
            )
            fail()
        }catch (e: HallBizException) {
            assertEquals(e.errorCode, HallErrorCode.SEAT_ALREADY_BOOKED)
        }
    }

    @Test
    fun `no hall found exception`() {
        assertThrows<HallNotFoundException> {
            ticketBuyCommandHandler.handle(
                    TicketBuyCommand(
                            movieName = "movie_c", expectTime = LocalDateTime.parse("2023-06-07T10:00:00"),
                            hallId = "h003",
                            seats = listOf(Seat(2)))
            )
        }
    }

    @Test
    fun `seat is more than hall capacity`() {
        try {
            ticketBuyCommandHandler.handle(
                    TicketBuyCommand(
                            movieName = "movie_b", expectTime = LocalDateTime.parse("2023-06-07T10:00:00"),
                            hallId = "h001",
                            seats = (1..10).map { Seat(it) })
            )
            fail()
        } catch (e: HallBizException) {
            assertEquals(e.errorCode, HallErrorCode.HALL_SEAT_OVER_LIMIT)
        }
    }
}