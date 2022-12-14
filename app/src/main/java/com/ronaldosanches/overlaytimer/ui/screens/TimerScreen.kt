package com.ronaldosanches.overlaytimer.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ronaldosanches.overlaytimer.R
import com.ronaldosanches.overlaytimer.shared.Constants
import com.ronaldosanches.overlaytimer.ui.theme.OverlayColors
import com.ronaldosanches.overlaytimer.ui.theme.OverlayDimension
import com.ronaldosanches.overlaytimer.ui.theme.OverlayTypography
import com.ronaldosanches.overlaytimer.ui.viewmodel.ITimerViewModel

@Composable
fun TimerScreen(viewModel: ITimerViewModel, colors: OverlayColors,
                typography: OverlayTypography, dimension: OverlayDimension) {
    val playing = viewModel.isCurrentlyPlaying.observeAsState().value ?: false
    val currentTime = viewModel.currentTime.observeAsState().value ?: String()
    val pipActive = viewModel.isPiPModeActive.observeAsState().value ?: false
    CreateTimerView(viewModel, colors, typography, dimension, playing, currentTime = currentTime, pipActive)
}

@Composable
fun CreateTimerView(viewModel: ITimerViewModel, colors: OverlayColors,
                    typography: OverlayTypography, dimension: OverlayDimension,
                    playing: Boolean, currentTime: String, pipActive: Boolean) {

    val context = LocalContext.current
    val contentDescriptionTime = context.getString(
        R.string.custom_clock_accessibility_description, currentTime.substring(0, 2),
        currentTime.substring(3, 5))
    val stateDescriptionTime = if (playing) {
        context.getString(R.string.play_state)
    } else {
        context.getString(R.string.pause_state)
    }
            ConstraintLayout(modifier = Modifier
        .background(colors.backgroundColor)
        .testTag(Constants.Tags.PIP_OFF_CONTAINER)
        .clearAndSetSemantics {
            contentDescription = contentDescriptionTime
            stateDescription = stateDescriptionTime
            customActions = createAccessibilityActions(context, playing, viewModel)
        }
    ) {

        val (timeBox, play, restart, pip, center) = createRefs()
        Box(modifier = Modifier.constrainAs(center) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        })
        if(!pipActive) {
            IconButton(
                modifier = Modifier
                    .constrainAs(pip) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                onClick = {
                    viewModel.activatePiP(playing)
                }) {
                Icon(
                    painterResource(id = R.drawable.ic_pip_enter),
                    contentDescription = stringResource(id = R.string.pip_button_content_description),
                    tint = colors.buttonIconColor,
                )
            }
        }

        if(pipActive) {
            Text(text = currentTime, color = colors.timeTextColor, style = typography.pipTimer,
                modifier = Modifier
                    .testTag(Constants.Tags.PIP_ON_CONTAINER)
                    .constrainAs(timeBox) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        } else {
            Box(contentAlignment= Alignment.Center,
                modifier = Modifier
                    .background(colors.timeBackground, shape = CircleShape)
                    .constrainAs(timeBox) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    }
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        val currentHeight = placeable.height
                        var heightCircle = currentHeight

                        if (placeable.width > heightCircle) {
                            heightCircle = placeable.width
                        }
                        layout(heightCircle, heightCircle) {
                            placeable.placeRelative(0, (heightCircle - currentHeight) / 2)
                        }

                    }
            ) {
                Text(
                    style = typography.fullScreenTimer,
                    text = currentTime,
                    textAlign = TextAlign.Center,
                    color = colors.timeTextColor,
                    modifier = Modifier
                        .padding(dimension.defaultPadding)
                        .defaultMinSize(24.dp),
                )
            }
        }
        if(!pipActive) {

            IconButton(
                modifier = Modifier
                    .drawBehind {
                        drawCircle(
                            color = colors.buttonBackground,
                            radius = this.size.width / 2
                        )
                    }
                    .constrainAs(play) {
                        start.linkTo(timeBox.start)
                        bottom.linkTo(timeBox.bottom)
                    },
                onClick = {
                    if(playing) {
                        viewModel.onPauseClick()
                    } else {
                        viewModel.onPlayClick()
                    }
                }) {
                if(playing) {
                    Icon(
                        painterResource(id = R.drawable.ic_pause),
                        contentDescription = stringResource(id = R.string.pause_text),
                        tint = colors.buttonIconColor,
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.ic_play),
                        contentDescription = stringResource(id = R.string.play_text),
                        tint = colors.buttonIconColor,
                    )
                }
            }
            IconButton(
                modifier = Modifier
                    .drawBehind {
                        drawCircle(
                            color = colors.buttonBackground,
                            radius = this.size.width / 2
                        )
                    }
                    .constrainAs(restart) {
                        top.linkTo(timeBox.top)
                        end.linkTo(timeBox.end)
                    },
                onClick = {
                    viewModel.onRestartClick()
                }) {
                Icon(
                    painterResource(id = R.drawable.ic_refresh),
                    contentDescription = stringResource(id = R.string.restart_text),
                    tint = colors.buttonIconColor,
                )
            }
        }
    }
}

private fun createAccessibilityActions(
    context: Context,
    isPlaying: Boolean,
    viewModel: ITimerViewModel,
    ) : List<CustomAccessibilityAction> {
    return mutableListOf(
        if(isPlaying) {
            CustomAccessibilityAction(
                label = context.getString(
                    R.string.pause_text
                )
            ) {
                viewModel.onPauseClick()
                true
            }
        } else {
            CustomAccessibilityAction(
                label = context.getString(
                    R.string.play_text
                )
            ) {
                viewModel.onPlayClick()
                true
            }
        },
        CustomAccessibilityAction(
            label = context.getString(R.string.restart_text)
        ) {
            viewModel.onRestartClick()
            true
        }
    ).also { if (isPlaying) it.add(
        CustomAccessibilityAction(
            label = context.getString(R.string.pip_button_content_description)
        ) {
            viewModel.activatePiP(isPlaying)
            true
        }
    ) }.toList()
}