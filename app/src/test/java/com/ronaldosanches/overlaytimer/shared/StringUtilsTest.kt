package com.ronaldosanches.overlaytimer.shared

import org.junit.Assert.assertEquals
import org.junit.Test


class StringUtilsTest {

    @Test
    fun `when string is valid should return valid time`() {
        //arrange
        val fiveMinutesThirtySeconds = (5*1000*60 + 30*1000).toLong()
        val expected = "05:30"

        //act
        val actual = fiveMinutesThirtySeconds.toSecondsMinutesFormatted()

        //assert
        assertEquals(expected, actual)
    }

    @Test
    fun `on sixty minutes should return zero`() {
        //arrange
        val sixtyMinutesOneSecond = (60*1000*60 + 1000*1).toLong()
        val expected = "00:01"

        //act
        val actual = sixtyMinutesOneSecond.toSecondsMinutesFormatted()

        assertEquals(expected, actual)
    }
}