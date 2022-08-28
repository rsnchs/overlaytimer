package com.ronaldosanches.overlaytimer.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ronaldosanches.overlaytimer.getOrAwaitValue
import com.ronaldosanches.overlaytimer.usecase.GetTimeUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.channelFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class TimerViewModelTest {
    @get:Rule var rule : TestRule = InstantTaskExecutorRule()
    private lateinit var viewModel: TimerViewModel
    @MockK lateinit var getTimeUseCase: GetTimeUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TimerViewModel(getTimeUseCase)
    }

    @Test
    fun `on init should bind timer`() {
        //arrange
        coEvery { getTimeUseCase.startTime() } answers { channelFlow {  } }

        coVerify(exactly = 1) { getTimeUseCase.startTime() }
    }

    @Test
    fun `assert initial states`() {
        //assert
        assertEquals(false, viewModel.isPiPModeActive.getOrAwaitValue())
        assertEquals(false, viewModel.activatePiP.getOrAwaitValue())
        assertEquals(false, viewModel.isCurrentlyPlaying.getOrAwaitValue())
        assertEquals("00:00", viewModel.currentTime.getOrAwaitValue())
    }

    @Test
    fun `on click play should change playing state to true`() {
        //arrange
        coEvery { getTimeUseCase.startTime() } answers { channelFlow {  } }
        coEvery { getTimeUseCase.unpause() } returns Unit
        val expected = true

        //act
        viewModel.onPlayClick()

        //assert
        assertEquals(expected, viewModel.isCurrentlyPlaying.getOrAwaitValue())
        coVerify(exactly = 1) { getTimeUseCase.unpause() }
    }

    @Test
    fun `on click pause should change playing state to false`() {
        //arrange
        coEvery { getTimeUseCase.startTime() } answers { channelFlow {  } }
        coEvery { getTimeUseCase.pause() } returns Unit
        val expected = false

        //act
        viewModel.onPauseClick()

        //assert
        assertEquals(expected, viewModel.isCurrentlyPlaying.getOrAwaitValue())
        coVerify(exactly = 1) { getTimeUseCase.pause() }
    }

    @Test
    fun `on click restart should change playing state to false and restart timer`() {
        //arrange
        coEvery { getTimeUseCase.startTime() } answers { channelFlow {  } }
        coEvery { getTimeUseCase.restartTimer() } returns Unit
        coEvery { getTimeUseCase.pause() } returns Unit
        val expected = false
        val currentTimeInitial = "00:00"

        //act
        viewModel.onRestartClick()

        //assert
        assertEquals(expected, viewModel.isCurrentlyPlaying.getOrAwaitValue())
        assertEquals(currentTimeInitial, viewModel.currentTime.getOrAwaitValue())
        coVerify(exactly = 1) { getTimeUseCase.restartTimer() }
    }

    @Test
    fun `on sending pip activate should emit value`() {
        //arrange
        coEvery { getTimeUseCase.startTime() } answers { channelFlow {  } }
        val isPlaying = true

        //act
        viewModel.activatePiP(isPlaying)

        //assert
        assertEquals(isPlaying, viewModel.activatePiP.getOrAwaitValue())
    }

    @Test
    fun `on sending pip active should emit value`() {
        //arrange
        coEvery { getTimeUseCase.startTime() } answers { channelFlow {  } }
        val isActive = false

        //act
        viewModel.isPiPActive(isActive)

        //assert
        assertEquals(isActive, viewModel.isPiPModeActive.getOrAwaitValue())
    }
}