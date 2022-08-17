package com.ronaldosanches.overlaytimer.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ronaldosanches.overlaytimer.R


private val freedoka = FontFamily(
    Font(R.font.fredokaoneregular, FontWeight.Normal)
)
data class OverlayTypography(
    val fullScreenTimer : TextStyle = TextStyle(
        fontFamily = freedoka,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp
    ),
    val pipTimer : TextStyle = TextStyle(
        fontFamily = freedoka,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )
)

internal val LocalTypography = staticCompositionLocalOf { OverlayTypography() }