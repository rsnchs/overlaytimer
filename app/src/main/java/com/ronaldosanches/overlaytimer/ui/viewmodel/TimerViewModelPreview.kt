package com.ronaldosanches.overlaytimer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModelPreview(
    private val playing : Boolean,
    private val pipMode: Boolean,
) : ViewModel(), ITimerViewModel {
    override val currentTime: LiveData<String>
        get() = MutableLiveData("01:39")

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