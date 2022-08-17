package com.ronaldosanches.overlaytimer.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

open class OverlayColors(
    backgroundColor: Color,
    timeBackground: Color,
    timeTextColor: Color,
    buttonBackground: Color,
    buttonIconColor: Color,
    isLight: Boolean,
) {
    var backgroundColor by mutableStateOf(backgroundColor)
        private set
    var timeBackground by mutableStateOf(timeBackground)
        private set
    var timeTextColor by mutableStateOf(timeTextColor)
        private set
    var buttonBackground by mutableStateOf(buttonBackground)
        private set
    var buttonIconColor by mutableStateOf(buttonIconColor)
        private set
    var isLight by mutableStateOf(isLight)
        internal set

    fun copy(
        backgroundColor: Color = this.backgroundColor,
        timeBackground: Color = this.timeBackground,
        timeTextColor: Color = this.timeTextColor,
        buttonBackground: Color = this.buttonBackground,
        buttonIconColor: Color = this.buttonIconColor,
        isLight: Boolean = this.isLight
    ): OverlayColors = OverlayColors(
        backgroundColor,
        timeBackground,
        timeTextColor,
        buttonBackground,
        buttonIconColor,
        isLight,
    )

    fun updateColorsFrom(other: OverlayColors) {
        backgroundColor = other.backgroundColor
        timeBackground = other.timeBackground
        timeTextColor = other.timeTextColor
        buttonBackground = other.buttonBackground
        buttonIconColor = other.buttonIconColor
    }
}

private val backgroundColorLight = Color(0xFFFFFFFF)
private val timeBackgroundLight = Color(0xFFB8B8B8)
private val timeTextColorLight = Color(0xFF000000)
private val buttonBackgroundLight = Color(0xFF858585)
private val buttonIconColorLight = Color(0xFF000000)
private val backgroundColorDark = Color(0xFF333333)
private val timeBackgroundDark = Color(0xFF474747)
private val timeTextColorDark = Color(0xFFFFFFFF)
private val buttonBackgroundDark = Color(0xFF7a7a7a)
private val buttonIconColorDark = Color(0xFFFFFFFF)

fun lightColors() : OverlayColors = OverlayColors(
    backgroundColor = backgroundColorLight,
    timeBackground = timeBackgroundLight,
    timeTextColor = timeTextColorLight,
    buttonBackground = buttonBackgroundLight,
    buttonIconColor = buttonIconColorLight,
    isLight = true
)

fun darkColors() : OverlayColors = OverlayColors(
    backgroundColor = backgroundColorDark,
    timeBackground = timeBackgroundDark,
    timeTextColor = timeTextColorDark,
    buttonBackground = buttonBackgroundDark,
    buttonIconColor = buttonIconColorDark,
    isLight = false
)

val LocalColors = staticCompositionLocalOf { lightColors() }