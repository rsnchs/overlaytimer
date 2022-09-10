package com.ronaldosanches.overlaytimer.usecase

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.ronaldosanches.overlaytimer.shared.Tags
import javax.inject.Inject

class AddAnalyticsEventUseCase @Inject constructor(
    private val analytics: FirebaseAnalytics
) {

    private fun addEvent(name: String, params: Bundle) {
        analytics.logEvent(name, params)
    }

    fun onPlayClick(currentTime: String?) {
        addEvent(Tags.PLAY_BUTTON, bundleOf(Tags.CURRENT_TIME to currentTime))
    }

    fun onPauseClick(currentTime: String?) {
        addEvent(Tags.PAUSE_BUTTON, bundleOf(Tags.CURRENT_TIME to currentTime))
    }

    fun onRestartClick(currentTime: String?) {
        addEvent(Tags.RESTART_BUTTON, bundleOf(Tags.CURRENT_TIME to currentTime))
    }

    fun onPIPModeChange(inPictureInPictureMode: Boolean, currentTime: String?) {
        addEvent(Tags.PIP_MODE, bundleOf(
            Tags.STATE to inPictureInPictureMode,
            Tags.CURRENT_TIME to currentTime
        ))
    }
}
