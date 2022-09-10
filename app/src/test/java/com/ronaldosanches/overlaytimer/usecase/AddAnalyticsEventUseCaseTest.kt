package com.ronaldosanches.overlaytimer.usecase

import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaldosanches.overlaytimer.shared.Tags
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class AddAnalyticsEventUseCaseTest {
    private lateinit var useCase: AddAnalyticsEventUseCase
    @MockK(relaxed = true) private lateinit var firebaseAnalytics: FirebaseAnalytics

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        useCase = AddAnalyticsEventUseCase(firebaseAnalytics)
    }

    @Test
    fun `when play click should emit play button event`() {
        //act
        useCase.onPlayClick(String())

        //assert
        verify { firebaseAnalytics.logEvent(Tags.PLAY_BUTTON, any()) }
    }

    @Test
    fun `when pause click should emit pause button event`() {
        //act
        useCase.onPauseClick(String())

        //assert
        verify { firebaseAnalytics.logEvent(Tags.PAUSE_BUTTON, any()) }
    }

    @Test
    fun `when restart click should emit restart button event`() {
        //act
        useCase.onRestartClick(String())

        //assert
        verify { firebaseAnalytics.logEvent(Tags.RESTART_BUTTON, any()) }
    }

    @Test
    fun `when PIP mode change should emit PIP mode change event`() {
        //act
        useCase.onPIPModeChange(false,String())

        //assert
        verify { firebaseAnalytics.logEvent(Tags.PIP_MODE, any()) }
    }
}