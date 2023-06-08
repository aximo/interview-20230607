package com.shawn.film.domain.support

interface LockTemplate {
    fun tryLock(): Locker
}