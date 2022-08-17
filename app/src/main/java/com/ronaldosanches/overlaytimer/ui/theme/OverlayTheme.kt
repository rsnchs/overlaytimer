package com.ronaldosanches.overlaytimer.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

object OverlayTheme {
    val colors : OverlayColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current
    val typography : OverlayTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
    val dimension : OverlayDimension
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current
}

@Composable
fun OverlayTheme(
    colors: OverlayColors = OverlayTheme.colors,
    typography: OverlayTypography = OverlayTheme.typography,
    dimension: OverlayDimension = OverlayTheme.dimension,
    content: @Composable () -> Unit
) {
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }
    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalTypography provides typography,
        LocalDimensions provides dimension
    ){
        content()
    }
}