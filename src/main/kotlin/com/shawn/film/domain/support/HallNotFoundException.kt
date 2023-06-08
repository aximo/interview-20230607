package com.shawn.film.domain.support

class HallNotFoundException(val hallId: String) : RuntimeException("operation fail for hall ${hallId}, reason: ${HallErrorCode.HALL_NOT_FOUND.name}") {
    val errorCode: HallErrorCode = HallErrorCode.HALL_NOT_FOUND
}