package com.ronaldosanches.overlaytimer.shared

import java.util.concurrent.TimeUnit

fun Long.toSecondsMinutesFormatted() : String {
    return String.format("%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this)%60,
        TimeUnit.MILLISECONDS.toSeconds(this)%60
    )
}