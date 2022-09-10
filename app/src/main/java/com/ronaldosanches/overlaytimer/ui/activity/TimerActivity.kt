package com.ronaldosanches.overlaytimer.ui.activity

import android.app.PendingIntent.*
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.ronaldosanches.overlaytimer.BuildConfig
import com.ronaldosanches.overlaytimer.R
import com.ronaldosanches.overlaytimer.shared.Constants
import com.ronaldosanches.overlaytimer.ui.screens.TimerScreen
import com.ronaldosanches.overlaytimer.ui.theme.OverlayTheme
import com.ronaldosanches.overlaytimer.ui.theme.darkColors
import com.ronaldosanches.overlaytimer.ui.theme.lightColors
import com.ronaldosanches.overlaytimer.ui.viewmodel.TimerViewModel
import com.ronaldosanches.overlaytimer.ui.viewmodel.TimerViewModelPreview
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TimerActivity : ComponentActivity() {

    private val viewModel by viewModels<TimerViewModel>()

    private var broadcastControls : BroadcastReceiver? = null

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewModel.isPiPActive(isInPictureInPictureMode)
        if(isInPictureInPictureMode) {
            broadcastControls = object : BroadcastReceiver() {
                override fun onReceive(p0: Context?, p1: Intent?) {
                   if(p1 != null && p1.action == Constants.ActionKeys.ACTION_MEDIA) {
                        when(p1.getIntExtra(Constants.IntentExtras.CONTROL_TYPE, 0)) {
                            Constants.ControlType.PLAY -> {
                                viewModel.onPlayClick()
                                updatePictureInPictureState(true)
                            }
                            Constants.ControlType.PAUSE -> {
                                viewModel.onPauseClick()
                                updatePictureInPictureState(false)
                            }
                            Constants.ControlType.RESTART -> {
                                viewModel.onRestartClick()
                                updatePictureInPictureState(false)
                            }
                        }
                    }
                }
            }
            this@TimerActivity.registerReceiver(broadcastControls, IntentFilter(Constants.ActionKeys.ACTION_MEDIA))
        } else {
            this@TimerActivity.unregisterReceiver(broadcastControls)
            broadcastControls = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCenter.start(
            application, BuildConfig.OVERLAY_TIMER_APP_CENTER_KEY,
            Analytics::class.java, Crashes::class.java
        )
        setContent {
            OverlayTheme {
                val colors = if(isSystemInDarkTheme()) darkColors() else lightColors()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    TimerScreen(viewModel = viewModel, colors = colors,
                        OverlayTheme.typography, OverlayTheme.dimension)
                }
            }
        }

        viewModel.isCurrentlyPlaying.observe(this) {
            updatePictureInPictureState(it)
        }

        viewModel.activatePiP.observe(this) {
            if(it) {
                updatePictureInPictureState(true)
                enterPictureInPictureMode(PictureInPictureParams.Builder().build())
            } else {
                updatePictureInPictureState(false)
                enterPictureInPictureMode(PictureInPictureParams.Builder().build())
            }
        }
    }

    private fun updatePictureInPictureState(isPlaying: Boolean) {
        if(isPlaying) {
            updatePictureInPictureAction(R.drawable.ic_pause, R.string.pause_text, Constants.ControlType.PAUSE, Constants.RequestCode.PAUSE)
        } else {
            updatePictureInPictureAction(R.drawable.ic_play, R.string.play_text, Constants.ControlType.PLAY, Constants.RequestCode.PLAY)
        }
    }

    private fun updatePictureInPictureAction(@DrawableRes iconId: Int, @StringRes title: Int,
                                             controlType: Int, requestCode: Int) {
        val actions = listOf(
            createRemoteAction(iconId,title,controlType,requestCode),
            createRemoteAction(R.drawable.ic_refresh, R.string.restart_text,
                Constants.ControlType.RESTART, Constants.RequestCode.RESTART)
        )
        val rational = Rational(Constants.Dimensions.RATIO_SMALL,Constants.Dimensions.RATIO_BIG)
        val params = PictureInPictureParams.Builder()
            .setAspectRatio(rational)
            .setActions(actions)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            params.setAutoEnterEnabled(true)
        }
        setPictureInPictureParams(params.build())
    }

    private fun createRemoteAction(@DrawableRes iconId: Int,
                                   @StringRes title: Int,
                                   controlType: Int,
                                   requestCode: Int) : RemoteAction {
        val flags = FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        val intent = getBroadcast(this, requestCode,
            Intent(Constants.ActionKeys.ACTION_MEDIA).putExtra(Constants.IntentExtras.CONTROL_TYPE, controlType), flags)
        val icon = Icon.createWithResource(this, iconId)
        return RemoteAction(icon,getString(title),getString(title),intent)
    }
}
@Preview(showBackground = true, device = Devices.PIXEL_XL, showSystemUi = true)
@Composable
fun PreviewSmall() {
    OverlayTheme {
        TimerScreen(viewModel = TimerViewModelPreview(playing = true, pipMode = true), colors = lightColors(),
            OverlayTheme.typography, OverlayTheme.dimension)
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4_XL, showSystemUi = true)
@Composable
fun PreviewBig() {
    OverlayTheme {
        TimerScreen(viewModel = TimerViewModelPreview(playing = false, pipMode = false), colors = lightColors(),
            OverlayTheme.typography, OverlayTheme.dimension)
    }
}

@Preview(showBackground = true, device = Devices.AUTOMOTIVE_1024p, widthDp = 1024, heightDp = 720, showSystemUi = true)
@Composable
fun PreviewTablet() {
    OverlayTheme {
        TimerScreen(viewModel = TimerViewModelPreview(playing = false, pipMode = false), colors = darkColors(),
            OverlayTheme.typography, OverlayTheme.dimension)
    }
}
