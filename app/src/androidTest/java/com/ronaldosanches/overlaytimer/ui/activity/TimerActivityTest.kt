package com.ronaldosanches.overlaytimer.ui.activity

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.ronaldosanches.overlaytimer.shared.Constants
import com.ronaldosanches.overlaytimer.ui.screens.TimerScreen
import com.ronaldosanches.overlaytimer.ui.theme.OverlayTheme
import com.ronaldosanches.overlaytimer.ui.theme.lightColors
import com.ronaldosanches.overlaytimer.ui.viewmodel.ITimerViewModel
import org.junit.Rule
import org.junit.Test

class TimerActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

//    composeTestRule.onRoot().printToLog("ComposeTree")

    @Test
    fun when_PIPMode_false_and_isPlaying_true_should_display_controls_on_screen() {
        val isPlaying = true
        val isPiPMode = false
        val colors = lightColors()
        val minutes = "35"
        val seconds = "47"
        val time = "$minutes:$seconds"
        composeTestRule.setContent {
            OverlayTheme {
                TimerScreen(viewModel = ViewModelMock(isPlaying, isPiPMode, time),
                    colors = colors, typography = OverlayTheme.typography, dimension = OverlayTheme.dimension)
            }
        }
        composeTestRule.onNode(hasTestTag(Constants.Tags.PIP_OFF_CONTAINER)).assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription("Pause"), true).assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription("Restart"), true).assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription("Enter Picture in Picture"), true).assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription(
            "Current time is $minutes minutes and $seconds seconds", ignoreCase = true),
            useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNode(hasStateDescription("Playing")).assertIsDisplayed()
    }

    @Test
    fun when_PIPMode_false_and_isPlaying_false_should_display_controls_on_screen() {
        val isPlaying = false
        val isPiPMode = false
        val colors = lightColors()
        val minutes = "35"
        val seconds = "47"
        val time = "$minutes:$seconds"
        composeTestRule.setContent {
            OverlayTheme {
                TimerScreen(viewModel = ViewModelMock(isPlaying, isPiPMode, time),
                    colors = colors, typography = OverlayTheme.typography, dimension = OverlayTheme.dimension)
            }
        }
        composeTestRule.onNode(hasTestTag(Constants.Tags.PIP_OFF_CONTAINER)).assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription("Play"), true).assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription("Restart"), true).assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription("Enter Picture in Picture"), true).assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription(
            "Current time is $minutes minutes and $seconds seconds", ignoreCase = true),
            useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNode(hasStateDescription("Paused")).assertIsDisplayed()
    }

    @Test
    fun when_PIPMode_true_and_isPlaying_false_should_not_display_controls_on_screen() {
        val isPlaying = false
        val isPiPMode = true
        val colors = lightColors()
        val minutes = "35"
        val seconds = "47"
        val time = "$minutes:$seconds"
        composeTestRule.setContent {
            OverlayTheme {
                TimerScreen(viewModel = ViewModelMock(isPlaying, isPiPMode, time),
                    colors = colors, typography = OverlayTheme.typography, dimension = OverlayTheme.dimension)
            }
        }
        composeTestRule.onNode(hasTestTag(Constants.Tags.PIP_ON_CONTAINER), true).assertIsDisplayed()
        composeTestRule.onNode(hasContentDescription(
            "Current time is $minutes minutes and $seconds seconds", ignoreCase = true),
            useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNode(hasStateDescription("Paused")).assertIsDisplayed()
    }

    @Test
    fun when_PIPMode_true_and_isPlaying_true_should_not_display_controls_on_screen() {
        val isPlaying = true
        val isPiPMode = true
        val colors = lightColors()
        val minutes = "35"
        val seconds = "47"
        val time = "$minutes:$seconds"
        composeTestRule.setContent {
            OverlayTheme {
                TimerScreen(
                    viewModel = ViewModelMock(isPlaying, isPiPMode, time),
                    colors = colors,
                    typography = OverlayTheme.typography,
                    dimension = OverlayTheme.dimension
                )
            }
        }
        composeTestRule.onNode(hasTestTag(Constants.Tags.PIP_ON_CONTAINER), true)
            .assertIsDisplayed()
        composeTestRule.onNode(
            hasContentDescription(
                "Current time is $minutes minutes and $seconds seconds", ignoreCase = true
            ),
            useUnmergedTree = true
        ).assertIsDisplayed()
        composeTestRule.onNode(hasStateDescription("Playing")).assertIsDisplayed()
    }
}

class ViewModelMock constructor(
    private val playing : Boolean,
    private val pipMode: Boolean,
    private val time: String = "01:39"
) : ITimerViewModel {
    override val currentTime: LiveData<String>
        get() = MutableLiveData(time)

    override val isCurrentlyPlaying: LiveData<Boolean>
        get() = MutableLiveData(playing)

    override val isPiPModeActive: LiveData<Boolean>
        get() = MutableLiveData(pipMode)

    override val activatePiP: LiveData<Boolean>
        get() = MutableLiveData(null)

    override fun onPlayClick() {
    }

    override fun onPauseClick() {
    }

    override fun onRestartClick() {
        TODO("Not yet implemented")
    }

    override fun isPiPActive(inPictureInPictureMode: Boolean) {
        TODO("Not yet implemented")
    }

    override fun activatePiP(playing: Boolean) {
        TODO("Not yet implemented")
    }
}