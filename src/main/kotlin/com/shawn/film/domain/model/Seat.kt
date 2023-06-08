package com.shawn.film.domain.model

/**
 * use an object nor num, as we maybe apply some ext attributes in seat, such as vip or something
 */
data class Seat(val num: Int)