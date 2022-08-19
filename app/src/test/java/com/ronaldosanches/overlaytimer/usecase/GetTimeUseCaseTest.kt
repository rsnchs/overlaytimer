package com.ronaldosanches.overlaytimer.usecase

import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class GetTimeUseCaseTest {
    private lateinit var useCase: GetTimeUseCase

    @Before
    fun setup() {
        useCase = GetTimeUseCase()
    }

    @Test
    fun `on bind timer and pause no events should be emitted`() = runTest {
        //act and assert
        useCase.startTime().test {
            expectNoEvents()
        }
    }

    @Test
    fun `on bind timer and unpause should emit time`() = runTest {
        //arrange
        val expected = listOf(0L, 1000L, 2000L)

        //act and assert
        useCase.startTime().test {
            useCase.unpause()
            assertEquals(expected[0], awaitItem())
            assertEquals(expected[1], awaitItem())
            assertEquals(expected[2], awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `on pause timer should stop emitting events`() = runTest {
        //arrange
        val expected = listOf(0L, 1000L, 2000L)

        //act and assert
        useCase.startTime().test {
            useCase.unpause()
            assertEquals(expected[0], awaitItem())
            useCase.pause()
            expectNoEvents()
        }
    }

    @Test
    fun `on restart timer should emit zero`() = runTest {
        //arrange
        val expected = listOf(0L, 1000L, 2000L)

        //act and assert
        useCase.startTime().test {
            useCase.unpause()
            assertEquals(expected[0], awaitItem())
            assertEquals(expected[1], awaitItem())
            assertEquals(expected[2], awaitItem())
            useCase.restartTimer()
            assertEquals(expected[0], awaitItem())
            assertEquals(expected[1], awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}