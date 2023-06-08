package com.shawn.film.infrastructure

import com.shawn.film.domain.support.LockTemplate
import com.shawn.film.domain.support.Locker

/**
 * this is a local lock, however, in most case, it should be implemented with redis
 */
class LocalLockTemplate : LockTemplate {
    override fun tryLock(): Locker {
        return DefaultLocker()
    }

}