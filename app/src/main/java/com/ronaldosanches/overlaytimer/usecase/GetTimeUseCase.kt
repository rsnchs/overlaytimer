package com.ronaldosanches.overlaytimer.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTimeUseCase @Inject constructor() {

    private var isPaused = true

    private var currentTime = ZERO

    private companion object {
        const val ZERO = 0L
        const val ONE_SECOND = 1000L
    }
    suspend fun startTime() : Flow<Long> = channelFlow {
        while(isActive) {
            if(!isPaused) {
                send(currentTime)
                currentTime += ONE_SECOND
            }
            kotlinx.coroutines.delay(ONE_SECOND)
        }
    }

    fun restartTimer() {
        currentTime = ZERO
    }

    fun pause() {
        isPaused = true
    }

    fun unpause() {
        isPaused = false
    }
}