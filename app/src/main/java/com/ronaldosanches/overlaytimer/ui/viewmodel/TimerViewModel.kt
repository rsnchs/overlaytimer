package com.ronaldosanches.overlaytimer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ronaldosanches.overlaytimer.shared.toSecondsMinutesFormatted
import com.ronaldosanches.overlaytimer.usecase.GetTimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val timer : GetTimeUseCase,
) : ViewModel(), ITimerViewModel {

    private companion object {
        const val TIME_ZERO = "00:00"
    }

    init {
        bindTimer()
    }

    private val _isPiPModeActive : MutableLiveData<Boolean> = MutableLiveData(false)
    override val isPiPModeActive : LiveData<Boolean> = _isPiPModeActive

    private val _activatePiP : MutableLiveData<Boolean> = MutableLiveData(false)
    override val activatePiP: LiveData<Boolean> = _activatePiP

    private val _isCurrentlyPlaying : MutableLiveData<Boolean> = MutableLiveData(false)
    override val isCurrentlyPlaying : LiveData<Boolean> = _isCurrentlyPlaying

    private val _currentTime : MutableLiveData<String> = MutableLiveData(TIME_ZERO)
    override val currentTime: LiveData<String> = _currentTime

    override fun onPlayClick() {
        _isCurrentlyPlaying.postValue(true)
        timer.unpause()
    }

    override fun onPauseClick() {
        _isCurrentlyPlaying.postValue(false)
        timer.pause()
    }

    override fun onRestartClick() {
        _currentTime.postValue(TIME_ZERO)
        timer.restartTimer()
        onPauseClick()
    }

    override fun isPiPActive(inPictureInPictureMode: Boolean) {
        _isPiPModeActive.postValue(inPictureInPictureMode)
    }

    override fun activatePiP(playing: Boolean) {
        _activatePiP.postValue(playing)
    }

    private fun bindTimer() = viewModelScope.launch(Dispatchers.IO) {
        timer.startTime().collect(::convertMillisToString)
    }

    private fun convertMillisToString(millis: Long) {
        _currentTime.postValue(millis.toSecondsMinutesFormatted())
    }
}