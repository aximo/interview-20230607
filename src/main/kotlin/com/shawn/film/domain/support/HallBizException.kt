package com.shawn.film.domain.support

import com.shawn.film.domain.model.Hall

class HallBizException(val hall: Hall,
                       val errorCode: HallErrorCode,
                       override val cause: Throwable? = null) : RuntimeException("operation fail for hall ${hall.id}, reason: ${errorCode.name}")