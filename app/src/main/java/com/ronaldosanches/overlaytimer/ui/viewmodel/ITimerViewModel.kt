package com.ronaldosanches.overlaytimer.ui.viewmodel

import androidx.lifecycle.LiveData

interface ITimerViewModel {
    val currentTime : LiveData<String>
    val isCurrentlyPlaying : LiveData<Boolean>
    val isPiPModeActive: LiveData<Boolean>
    val activatePiP : LiveData<Boolean>
    fun onPlayClick()
    fun onPauseClick()
    fun onRestartClick()
    fun isPiPActive(inPictureInPictureMode: Boolean)
    fun activatePiP(playing: Boolean)
}