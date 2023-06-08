package com.shawn.film.application

data class CommandResult<T>(val data: T) {
    companion object {
        fun <D> of(data: D): CommandResult<D> {
            return CommandResult(data)
        }
    }
}