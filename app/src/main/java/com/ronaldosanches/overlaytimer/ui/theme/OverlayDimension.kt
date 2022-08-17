package com.ronaldosanches.overlaytimer.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class OverlayDimension(
    val defaultPadding : Dp = 32.dp,
)

internal val LocalDimensions = staticCompositionLocalOf { OverlayDimension() }